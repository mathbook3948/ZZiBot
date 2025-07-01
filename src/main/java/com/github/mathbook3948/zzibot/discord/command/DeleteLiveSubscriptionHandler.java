package com.github.mathbook3948.zzibot.discord.command;

import com.github.mathbook3948.zzibot.dto.zzibot.LiveSubscription;
import com.github.mathbook3948.zzibot.exception.LiveSubscriptionLimitExceededException;
import com.github.mathbook3948.zzibot.mapper.LiveSubscriptionMapper;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputAutoCompleteEvent;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.discordjson.json.ApplicationCommandOptionChoiceData;
import discord4j.rest.util.Permission;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class DeleteLiveSubscriptionHandler {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(DeleteLiveSubscriptionHandler.class);

    private final String COMMAND = "알림해제";

    @Autowired
    private LiveSubscriptionMapper liveSubscriptionMapper;

    @Autowired
    @Lazy
    private DeleteLiveSubscriptionHandler self;

    public DeleteLiveSubscriptionHandler(GatewayDiscordClient client) {
        client.on(ChatInputAutoCompleteEvent.class, this::handleAutoComplete)
                .subscribe();

        client.on(ChatInputInteractionEvent.class)
                .filter(event -> event.getCommandName().equals(COMMAND))
                .flatMap(this::handle)
                .subscribe();
    }

    private Mono<Void> handle(ChatInputInteractionEvent event) {
        return Mono.justOrEmpty(event.getInteraction().getMember())
                .flatMap(member -> member.getBasePermissions()
                        .flatMap(perms -> {
                            if (!perms.contains(Permission.ADMINISTRATOR)) {
                                return event.reply("관리자만 사용할 수 있습니다.").withEphemeral(true);
                            }

                            return event.getInteraction().getChannel()
                                    .ofType(MessageChannel.class)
                                    .flatMap(channel -> {
                                        String message;
                                        try {
                                            self.handletransactional(event);
                                            message = "알림 해제를 성공하였습니다.";
                                        } catch (Exception e) {
                                            logger.error("error in /알림해제 handler: {}", e.getMessage());
                                            message = "알 수 없는 오류가 발생하였습니다.";
                                        }

                                        return event.reply()
                                                .withEphemeral(true)
                                                .withContent(message);
                                    });
                        })
                );
    }


    private Mono<Void> handleAutoComplete(ChatInputAutoCompleteEvent event) {
        List<ApplicationCommandOptionChoiceData> choices = new ArrayList<>();

        try {
            String guildId = event.getInteraction().getGuildId().map(Snowflake::asString).orElse(null);
            String keyword = event.getFocusedOption()
                    .getValue()
                    .map(ApplicationCommandInteractionOptionValue::asString)
                    .orElse(null);

            if (!event.getCommandName().equals(COMMAND)) return Mono.empty();
            if (guildId == null) return Mono.empty();
            if (!event.getFocusedOption().getName().equals("채널명")) return Mono.empty();


            List<Map<String, Object>> list = liveSubscriptionMapper.selectLiveSubscriptionWithGuildIDAndSearch(Map.of("guildId", guildId, "search", keyword));

            for(Map<String, Object> map : list) {
                String channelId = map.get("channel_id").toString();
                String channelName = map.get("channel_name").toString();

                choices.add(ApplicationCommandOptionChoiceData.builder().name(channelName).value(channelId).build());
            }
        } catch (Exception e) {
            logger.info("no result");
        }

        return event.respondWithSuggestions(choices);
    }

    @Transactional
    protected void handletransactional(ChatInputInteractionEvent event) throws Exception {
        String guildId = event.getInteraction().getGuildId().map(Snowflake::asString).orElse(null);
        String channelId = event.getOption("채널명")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse(null);

        if (guildId == null || channelId == null) {
            throw new IllegalArgumentException("길드 ID 또는 채널 ID가 누락되었습니다.");
        }

        liveSubscriptionMapper.deleteLiveSubscriptionWithChannelId(Map.of("guildId", guildId, "channelId", channelId));
    }
}

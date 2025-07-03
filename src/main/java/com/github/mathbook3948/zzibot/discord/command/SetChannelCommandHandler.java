package com.github.mathbook3948.zzibot.discord.command;

import com.github.mathbook3948.zzibot.dto.zzibot.LiveAlarmChannel;
import com.github.mathbook3948.zzibot.enums.DiscordLogEventContent;
import com.github.mathbook3948.zzibot.mapper.LiveAlarmChannelMapper;
import com.github.mathbook3948.zzibot.util.DiscordUtil;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.rest.util.Permission;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class SetChannelCommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(SetChannelCommandHandler.class);

    private final String COMMAND = "알림채널설정";

    @Autowired
    private GatewayDiscordClient client;

    @Autowired
    private LiveAlarmChannelMapper liveAlarmChannelMapper;

    @Autowired
    private DiscordUtil discordUtil;

    @PostConstruct
    public void register() {
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
                                discordUtil.insertDiscordLog(event, false, COMMAND, DiscordLogEventContent.REQUIRE_ADMINISTRATOR);
                                return event.reply("관리자만 사용할 수 있습니다.").withEphemeral(true);
                            }

                            return event.getInteraction().getChannel()
                                    .ofType(MessageChannel.class)
                                    .flatMap(channel -> {
                                        String message;
                                        try {
                                            String channelId = channel.getId().asString();
                                            String guildId = event.getInteraction().getGuildId()
                                                    .map(Snowflake::asString)
                                                    .orElse(null);

                                            liveAlarmChannelMapper.insertLiveAlarmChannel(
                                                    LiveAlarmChannel.builder()
                                                            .guildId(guildId)
                                                            .channelId(channelId)
                                                            .build()
                                            );

                                            discordUtil.insertDiscordLog(event, true, COMMAND, "");

                                            message = "해당 채널로 알림이 설정되었습니다.";
                                        } catch (Exception e) {
                                            discordUtil.insertDiscordLog(event, false, COMMAND, e.getMessage());
                                            logger.error("Error in SetChannelCommandHandler: {}", e.getMessage());
                                            message = "알 수 없는 오류가 발생하였습니다.";
                                        }

                                        return event.reply()
                                                .withEphemeral(true)
                                                .withContent(message);
                                    });
                        })
                );
    }

}

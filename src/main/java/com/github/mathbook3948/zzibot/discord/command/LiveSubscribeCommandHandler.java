package com.github.mathbook3948.zzibot.discord.command;

import com.github.mathbook3948.zzibot.dto.zzibot.LiveSubscription;
import com.github.mathbook3948.zzibot.exception.LiveSubscriptionLimitExceededException;
import com.github.mathbook3948.zzibot.mapper.LiveSubscriptionMapper;
import com.github.mathbook3948.zzibot.util.ChzzkUtil;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.entity.channel.MessageChannel;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

@Component
public class LiveSubscribeCommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(LiveSubscribeCommandHandler.class);

    @Autowired
    private GatewayDiscordClient client;

    @Autowired
    private LiveSubscriptionMapper liveSubscriptionMapper;

    @PostConstruct
    public void register() {
        client.on(ChatInputInteractionEvent.class)
                .filter(event -> event.getCommandName().equals("live-subscribe"))
                .flatMap(this::handle)
                .subscribe();
    }

    private Mono<Void> handle(ChatInputInteractionEvent event) {
        String url = event.getOption("url")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(val -> val.asString())
                .orElse(null);

        if (url == null || url.isBlank()) {
            return event.reply()
                    .withEphemeral(true)
                    .withContent("URL이 필요합니다.");
        }

        return event.getInteraction().getChannel()
                .ofType(MessageChannel.class)
                .flatMap(channel -> {
                    String message = "";

                    try {
                        String guildId = event.getInteraction().getGuildId().map(id -> id.asString()).orElse(null);

                        String channelId = ChzzkUtil.extractChannelIdFromUrl(url);

                        LiveSubscription ls = LiveSubscription.builder().guildId(guildId).channelId(channelId).build();

                        List<LiveSubscription> list = liveSubscriptionMapper.selectLiveSubscriptionWithGuildID(ls);

                        if(list.size() >= 5) {
                            throw new LiveSubscriptionLimitExceededException(null);
                        }

                        liveSubscriptionMapper.insertLiveSubscription(ls);

                        message = "구독에 성공하였습니다.";
                    } catch (LiveSubscriptionLimitExceededException lslee) {
                        message = lslee.getMessage();
                    } catch (Exception e) {
                        logger.error("error in /live-subscribe handler: {}", e.getMessage());
                        message = "알 수 없는 오류가 발생하였습니다.";
                    }

                    return event.reply()
                            .withEphemeral(true)
                            .withContent(message);
                }).then();
    }

}

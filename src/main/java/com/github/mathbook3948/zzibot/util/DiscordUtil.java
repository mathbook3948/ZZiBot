package com.github.mathbook3948.zzibot.util;

import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.channel.TextChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class DiscordUtil {

    @Autowired
    private GatewayDiscordClient client;

    @Async
    public void sendMessageAsync(String channelId, String content) {
        client.getChannelById(Snowflake.of(channelId))
                .ofType(TextChannel.class)
                .flatMap(channel -> channel.createMessage(content))
                .subscribe();
    }
}

package com.github.mathbook3948.zzibot.util;

import com.github.mathbook3948.zzibot.chzzk.ChzzkChannelClient;
import com.github.mathbook3948.zzibot.dto.chzzk.ChannelResponseContent;
import com.github.mathbook3948.zzibot.dto.chzzk.ChzzkResponse;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import discord4j.rest.util.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class DiscordUtil {

    @Autowired
    private GatewayDiscordClient client;

    @Autowired
    private ChzzkChannelClient chzzkChannelClient;

    private final String URL = "https://chzzk.naver.com/";

    @Async
    public void sendMessageAsync(String discordChannelId, String chzzkChannelId) {
        ChzzkResponse<ChannelResponseContent> res = chzzkChannelClient.getLiveStatus(chzzkChannelId);

        String embedUrl = URL + chzzkChannelId;
        String channelName = res.getContent().getChannelName();
        String channelImageUrl = res.getContent().getChannelImageUrl();

        client.getChannelById(Snowflake.of(discordChannelId))
                .ofType(TextChannel.class)
                .flatMap(channel -> channel.createMessage(
                        MessageCreateSpec.builder()
                                .addEmbed(EmbedCreateSpec.builder()
                                        .title("🎥 치지직 라이브 알림")
                                        .url(embedUrl)
                                        .description("**" + channelName + "**님이 방송을 시작했습니다!")
                                        .thumbnail(channelImageUrl)
                                        .color(Color.MOON_YELLOW)
                                        .build())
                                .build()))
                .subscribe();
    }
}

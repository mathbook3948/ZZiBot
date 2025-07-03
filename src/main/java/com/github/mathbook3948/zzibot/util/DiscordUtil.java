package com.github.mathbook3948.zzibot.util;

import com.github.mathbook3948.zzibot.chzzk.ChzzkChannelClient;
import com.github.mathbook3948.zzibot.dto.chzzk.ChannelResponseContent;
import com.github.mathbook3948.zzibot.dto.chzzk.ChzzkResponse;
import com.github.mathbook3948.zzibot.dto.zzibot.log.DiscordLogDTO;
import com.github.mathbook3948.zzibot.enums.DiscordLogEventContent;
import com.github.mathbook3948.zzibot.mapper.DiscordLogMapper;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
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

    @Autowired
    private DiscordLogMapper discordLogMapper;

    private final String URL = "https://chzzk.naver.com/";

    @Async
    public void sendMessageAsync(String discordChannelId, String chzzkChannelId, String liveTitle) {
        ChzzkResponse<ChannelResponseContent> res = chzzkChannelClient.getLiveStatus(chzzkChannelId);

        String embedUrl = URL + chzzkChannelId;
        String channelName = res.getContent().getChannelName();
        String channelImageUrl = res.getContent().getChannelImageUrl();

        client.getChannelById(Snowflake.of(discordChannelId))
                .ofType(TextChannel.class)
                .flatMap(channel -> channel.createMessage(
                        MessageCreateSpec.builder()
                                .addEmbed(EmbedCreateSpec.builder()
                                        .title("**" + channelName + "**님이 방송을 시작했습니다!")
                                        .url(embedUrl)
                                        .description(liveTitle)
                                        .thumbnail(channelImageUrl)
                                        .color(Color.MOON_YELLOW)
                                        .build())
                                .build()))
                .subscribe();
    }

    @Async
    public void insertDiscordLog(ChatInputInteractionEvent event, boolean isSuccess, String command, String content) {
        DiscordLogDTO log = new DiscordLogDTO();

        log.setDiscord_log_is_success(isSuccess);
        log.setDiscord_log_content(content);
        log.setDiscord_log_command(command);

        String guildId = event.getInteraction().getGuildId()
                .map(Snowflake::asString)
                .orElse(null);

        String channelId = event.getInteraction().getChannel()
                .map(channel -> channel.getId().asString())
                .block();

        event.getInteraction().getMember().ifPresent(member -> {
            log.setDiscord_log_user_id(member.getId().asString());
            log.setDiscord_log_user_tag(member.getTag());
        });

        log.setDiscord_log_guild_id(guildId);
        log.setDiscord_log_channel_id(channelId);

        discordLogMapper.insertDiscordLog(log);
    }

    @Async
    public void insertDiscordLog(ChatInputInteractionEvent event, boolean isSuccess, String command, DiscordLogEventContent content) {
        DiscordLogDTO log = new DiscordLogDTO();

        log.setDiscord_log_is_success(isSuccess);
        log.setDiscord_log_content(content.getValue());
        log.setDiscord_log_command(command);

        String guildId = event.getInteraction().getGuildId()
                .map(Snowflake::asString)
                .orElse(null);

        String channelId = event.getInteraction().getChannel()
                .map(channel -> channel.getId().asString())
                .block();

        event.getInteraction().getMember().ifPresent(member -> {
            log.setDiscord_log_user_id(member.getId().asString());
            log.setDiscord_log_user_tag(member.getTag());
        });

        log.setDiscord_log_guild_id(guildId);
        log.setDiscord_log_channel_id(channelId);

        discordLogMapper.insertDiscordLog(log);
    }

}

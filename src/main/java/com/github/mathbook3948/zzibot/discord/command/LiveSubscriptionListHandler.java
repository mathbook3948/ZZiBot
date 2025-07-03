package com.github.mathbook3948.zzibot.discord.command;

import com.github.mathbook3948.zzibot.dto.zzibot.LiveAlarmChannel;
import com.github.mathbook3948.zzibot.enums.DiscordLogEventContent;
import com.github.mathbook3948.zzibot.mapper.LiveAlarmChannelMapper;
import com.github.mathbook3948.zzibot.mapper.LiveSubscriptionMapper;
import com.github.mathbook3948.zzibot.util.DiscordUtil;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.EmbedCreateSpec;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Component
public class LiveSubscriptionListHandler {

    private static final Logger logger = LoggerFactory.getLogger(LiveSubscriptionListHandler.class);

    private final String COMMAND = "알림목록";

    private final String embedUrl = "https://chzzk.naver.com/";

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");


    @Autowired
    private GatewayDiscordClient client;

    @Autowired
    private LiveSubscriptionMapper liveSubscriptionMapper;

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
        return event.getInteraction().getChannel()
                .ofType(MessageChannel.class)
                .flatMap(channel -> {
                    try {
                        String guildId = event.getInteraction().getGuildId().map(Snowflake::asString).orElse(null);
                        List<Map<String, Object>> list = liveSubscriptionMapper
                                .selectLiveSubscriptionWithGuildIDAndSearch(Map.of("guildId", guildId));

                        if (list.isEmpty()) {
                            discordUtil.insertDiscordLog(event, true, COMMAND, DiscordLogEventContent.ALARM_NOT_FOUND);

                            return event.reply()
                                    .withEphemeral(true)
                                    .withContent("등록된 알림 채널이 없습니다.");
                        }

                        StringBuilder desc = new StringBuilder();
                        for (Map<String, Object> row : list) {
                            String name = row.get("channel_name").toString();
                            String cid = row.get("channel_id").toString();
                            Object createdObj = row.get("created_at");

                            String formattedCreated = "알 수 없음";
                            if (createdObj instanceof Timestamp ts) {
                                LocalDateTime createdLocal = ts.toLocalDateTime();
                                ZonedDateTime zoned = createdLocal.atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneId.of("Asia/Seoul"));
                                formattedCreated = formatter.format(zoned);
                            }

                            desc.append("• ")
                                    .append("[**").append(name).append("**](").append(embedUrl).append(cid).append(") ")
                                    .append("- 등록일: ").append(formattedCreated)
                                    .append("\n");
                        }

                        discordUtil.insertDiscordLog(event, true, COMMAND, "");
                        return event.reply()
                                .withEphemeral(true)
                                .withEmbeds(EmbedCreateSpec.builder()
                                        .title("등록된 알림 채널 목록")
                                        .description(desc.toString())
                                        .build());
                    } catch (Exception e) {
                        discordUtil.insertDiscordLog(event, false, COMMAND, e.getMessage());
                        logger.error("Error in LiveSubscriptionListHandler: {}", e.getMessage(), e);
                        return event.reply()
                                .withEphemeral(true)
                                .withContent("조회 중 오류가 발생했습니다.");
                    }
                }).then();
    }
}

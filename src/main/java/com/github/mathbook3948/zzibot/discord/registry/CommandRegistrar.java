package com.github.mathbook3948.zzibot.discord.registry;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Component
public class CommandRegistrar {

    @Value("${discord.token}")
    private String botToken;

    @Value("${discord.application.id}")
    private String applicationId;

    @Value("${discord.test.guild.id}")
    private String guildId;

    private final WebClient client = WebClient.create("https://discord.com/api/v10");

    @PostConstruct
    public void registerPingCommand() {
        registerLiveSubscription();
        registerSetChannel();
    }

    private void registerLiveSubscription() {
        Map<String, Object> command = Map.of(
                "name", "live-subscribe",
                "description", "치지직 라이브 알림을 받을 채널을 등록합니다.",
                "type", 1, // CHAT_INPUT
                "options", List.of(
                        Map.of(
                                "type", 3, // STRING
                                "name", "url",
                                "description", "치지직 채널 URL",
                                "required", true
                        )
                )
        );

        client.post()
                .uri("/applications/{applicationId}/guilds/{guildId}/commands", applicationId, guildId)
                .header("Authorization", "Bot " + botToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    private void registerSetChannel() {
        Map<String, Object> command = Map.of(
                "name", "set-channel",
                "description", "이 채널을 봇 기본 채널로 설정합니다.",
                "type", 1 // CHAT_INPUT
        );

        client.post()
                .uri("/applications/{applicationId}/guilds/{guildId}/commands", applicationId, guildId)
                .header("Authorization", "Bot " + botToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}

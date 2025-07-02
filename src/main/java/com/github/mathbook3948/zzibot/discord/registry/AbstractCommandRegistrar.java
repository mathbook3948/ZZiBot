package com.github.mathbook3948.zzibot.discord.registry;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mathbook3948.zzibot.dto.zzibot.CommandDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
public abstract class AbstractCommandRegistrar {

    @Value("${discord.token:}")
    private String botToken;

    @Value("${discord.application.id:}")
    private String applicationId;

    @Value("${discord.test.guild.id:}")
    private String guildId;

    @Autowired
    @Qualifier("discordWebClient")
    private WebClient client;

    @Autowired
    private ObjectMapper objectMapper;

    @SuppressWarnings("unchecked")
    protected Mono<Void> setGuildCommand(CommandDTO config) {
        Map<String, Object> command = objectMapper.convertValue(config, Map.class);

        return client.post()
                .uri("/applications/{applicationId}/guilds/{guildId}/commands", applicationId, guildId)
                .header("Authorization", "Bot " + botToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .retrieve()
                .toBodilessEntity()
                .then();
    }

    @SuppressWarnings("unchecked")
    protected Mono<Void> setGlobalCommand(CommandDTO config) {
        Map<String, Object> command = objectMapper.convertValue(config, Map.class);

        return client.post()
                .uri("/applications/{applicationId}/commands", applicationId)
                .header("Authorization", "Bot " + botToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(command)
                .retrieve()
                .toBodilessEntity()
                .then();
    }
}

package com.github.mathbook3948.zzibot.discord.registry;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mathbook3948.zzibot.dto.zzibot.CommandDTO;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public abstract class AbstractCommandRegistrar {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(AbstractCommandRegistrar.class);

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
    protected void setGlobalCommand(CommandDTO config) {
        Map<String, Object> command = objectMapper.convertValue(config, Map.class);

        String jsonBody;
        try {
            jsonBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(command);
            logger.info("Registering Global Command - JSON Body:\n{}", jsonBody);
        } catch (Exception e) {
            logger.error("Failed to serialize command JSON", e);
            jsonBody = "{}";
        }

        try {
            String response = client.post()
                    .uri("/applications/{applicationId}/commands", applicationId)
                    .header("Authorization", "Bot " + botToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(command)
                    .retrieve()
                    .onStatus(status -> !status.is2xxSuccessful(),
                            res -> res.bodyToMono(String.class).flatMap(body -> {
                                logger.error("Failed to register command: {}", body);
                                return Mono.error(new RuntimeException("Discord API error: " + body));
                            })
                    )
                    .bodyToMono(String.class)
                    .block();

            logger.info("Discord API response: {}", response != null ? response : "(empty)");
        } catch (Exception ex) {
            logger.error("Global command registration failed", ex);
        }
    }

    @SuppressWarnings("unchecked")
    protected void setGuildCommand(CommandDTO config) {
        Map<String, Object> command = objectMapper.convertValue(config, Map.class);

        String jsonBody;
        try {
            jsonBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(command);
            logger.info("Registering Guild Command - JSON Body:\n{}", jsonBody);
        } catch (Exception e) {
            logger.error("Failed to serialize command JSON", e);
            jsonBody = "{}";
        }

        try {
            String response = client.post()
                    .uri("/applications/{applicationId}/guilds/{guildId}/commands", applicationId, guildId)
                    .header("Authorization", "Bot " + botToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(command)
                    .retrieve()
                    .onStatus(status -> !status.is2xxSuccessful(),
                            res -> res.bodyToMono(String.class).flatMap(body -> {
                                logger.error("Failed to register guild command: {}", body);
                                return Mono.error(new RuntimeException("Discord API error: " + body));
                            })
                    )
                    .bodyToMono(String.class)
                    .block();

            logger.info("Discord API guild response: {}", response != null ? response : "(empty)");
        } catch (Exception ex) {
            logger.error("Guild command registration failed", ex);
        }
    }
}

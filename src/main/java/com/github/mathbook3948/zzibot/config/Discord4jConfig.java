package com.github.mathbook3948.zzibot.config;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Discord4jConfig {

    private static final Logger logger = LoggerFactory.getLogger(Discord4jConfig.class);

    @Value("${discord.token}")
    private String token;

    @Bean
    public GatewayDiscordClient gatewayDiscordClient() {
        logger.info("GatewayDiscordClient initialized====================================");
        return DiscordClient.create(token)
                .login()
                .blockOptional()
                .orElseThrow();
    }
}
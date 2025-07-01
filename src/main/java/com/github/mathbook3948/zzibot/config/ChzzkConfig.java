package com.github.mathbook3948.zzibot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ChzzkConfig {

    @Value("${api.chzzk.url}")
    private String baseUrl;

    @Bean(name = "chzzkWebClient")
    public WebClient chzzkWebClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}

package com.github.mathbook3948.zzibot.chzzk;

import com.github.mathbook3948.zzibot.dto.chzzk.LiveStatusResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ChzzkLiveStatusClient {

    private final WebClient webClient;
    private final String version;

    public ChzzkLiveStatusClient(
            @Value("${api.chzzk.url}") String baseUrl,
            @Value("${api.chzzk.version}") String version
    ) {
        this.version = version;
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public LiveStatusResponse getLiveStatus(String channelId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/polling/{version}/channels/{channelId}/live-status")
                        .build(version, channelId))
                .retrieve()
                .bodyToMono(LiveStatusResponse.class)
                .block();
    }
}

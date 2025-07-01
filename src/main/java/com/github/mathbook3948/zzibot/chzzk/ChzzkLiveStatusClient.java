package com.github.mathbook3948.zzibot.chzzk;

import com.github.mathbook3948.zzibot.dto.chzzk.ChzzkResponse;
import com.github.mathbook3948.zzibot.dto.chzzk.LiveStatusResponseContent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ChzzkLiveStatusClient {

    private final WebClient webClient;
    private final String version = "v3.1";

    public ChzzkLiveStatusClient(
            @Value("${api.chzzk.url}") String baseUrl
    ) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public ChzzkResponse<LiveStatusResponseContent> getLiveStatus(String channelId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/polling/{version}/channels/{channelId}/live-status")
                        .build(version, channelId))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ChzzkResponse<LiveStatusResponseContent>>() {})
                .block();
    }
}

package com.github.mathbook3948.zzibot.chzzk;

import com.github.mathbook3948.zzibot.dto.chzzk.ChannelResponseContent;
import com.github.mathbook3948.zzibot.dto.chzzk.ChzzkResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ChzzkChannelClient {

    private final String version = "v1";

    @Autowired
    @Qualifier("chzzkWebClient")
    private WebClient webClient;

    public ChzzkResponse<ChannelResponseContent> getLiveStatus(String channelId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/service/{version}/channels/{channelId}")
                        .build(version, channelId))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ChzzkResponse<ChannelResponseContent>>() {})
                .block();
    }
}

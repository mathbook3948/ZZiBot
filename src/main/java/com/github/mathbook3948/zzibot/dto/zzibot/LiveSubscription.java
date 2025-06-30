package com.github.mathbook3948.zzibot.dto.zzibot;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Alias("LiveSubscription")
public class LiveSubscription {
    private String guildId;
    private String channelId;

    @Builder.Default
    private LocalDateTime createdAt = null;
}

package com.github.mathbook3948.zzibot.dto.zzibot;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@Builder
@Alias("LiveAlarmChannel")
public class LiveAlarmChannel {

    private String channelId;
    private String guildId;
}

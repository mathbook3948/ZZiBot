package com.github.mathbook3948.zzibot.dto.zzibot.common;

import com.github.mathbook3948.zzibot.interfaces.ResponseContent;
import lombok.Data;

@Data
public class MainContentDTO implements ResponseContent {

    //봇이 들어간 채널 수
    private Integer guildCount;
    //Quartz 돌고 있는 치지직 채널 수
    private Integer connectedChannelCount;
}

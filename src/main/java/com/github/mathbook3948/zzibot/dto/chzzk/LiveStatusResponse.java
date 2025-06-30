package com.github.mathbook3948.zzibot.dto.chzzk;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.util.List;

@Getter
@Setter
@Alias("LiveStatusResponse")
public class LiveStatusResponse {
    private int code;
    private String message;
    private LiveStatusResponseContent content;
}

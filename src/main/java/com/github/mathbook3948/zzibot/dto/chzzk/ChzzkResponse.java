package com.github.mathbook3948.zzibot.dto.chzzk;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@Alias("ChzzkResponse")
public class ChzzkResponse<T extends ChzzkContentBase> {
    private int code;
    private String message;
    private T content;
}

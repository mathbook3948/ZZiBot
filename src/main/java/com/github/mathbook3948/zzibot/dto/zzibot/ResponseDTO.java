package com.github.mathbook3948.zzibot.dto.zzibot;

import com.github.mathbook3948.zzibot.interfaces.ResponseContent;
import lombok.Data;

@Data
public class ResponseDTO<T extends ResponseContent> {
    private boolean result;
    private String msg;
    private T content;
}

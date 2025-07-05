package com.github.mathbook3948.zzibot.dto.zzibot.auth;

import com.github.mathbook3948.zzibot.interfaces.ResponseContent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginContentDTO implements ResponseContent {
    private String id;
    private String accessToken;
    private String refreshToken;
}

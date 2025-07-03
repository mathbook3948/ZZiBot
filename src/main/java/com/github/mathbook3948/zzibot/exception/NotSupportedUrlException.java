package com.github.mathbook3948.zzibot.exception;

public class NotSupportedUrlException extends LiveSubscriptionException {
    public NotSupportedUrlException(String message) {
        super(message == null || message.isEmpty()
                ? "잘못된 url입니다. 다시 한번 확인해주세요."
                : message);
    }
}

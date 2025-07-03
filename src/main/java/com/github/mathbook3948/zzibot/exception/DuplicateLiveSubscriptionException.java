package com.github.mathbook3948.zzibot.exception;

public class DuplicateLiveSubscriptionException extends LiveSubscriptionException {
    public DuplicateLiveSubscriptionException(String message) {
        super(message == null || message.isEmpty()
                ? "이미 구독 되어있는 채널입니다."
                : message);
    }
}

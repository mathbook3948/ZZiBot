package com.github.mathbook3948.zzibot.exception;

public class LiveSubscriptionLimitExceededException extends LiveSubscriptionException {
    public LiveSubscriptionLimitExceededException(String message) {
        super(message == null || message.isEmpty()
                ? "채널당 최대 구독 수(5개)를 초과했습니다."
                : message);
    }
}

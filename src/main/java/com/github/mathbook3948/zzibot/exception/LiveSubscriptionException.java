package com.github.mathbook3948.zzibot.exception;

public abstract class LiveSubscriptionException extends RuntimeException{
    public LiveSubscriptionException(String message) {
        super(message == null || message.equalsIgnoreCase("") ? "알 수 없는 오류가 발생했습니다." : message);
    }
}

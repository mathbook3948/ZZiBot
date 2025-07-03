package com.github.mathbook3948.zzibot.enums;

public enum DiscordLogEventContent {

    // 권한
    REQUIRE_ADMINISTRATOR("관리자 권한이 없어서 실패"),

    // 입력 오류
    MISSING_GUILD_OR_CHANNEL_ID("길드 ID 또는 채널 ID 누락"),

    // 명령 성공
    DELETE_SUBSCRIPTION_SUCCESS("알림 해제 성공"),

    // 명령 실패
    DELETE_SUBSCRIPTION_ERROR("알림 해제 중 예외 발생"),

    // 자동완성 실패
    AUTOCOMPLETE_NO_RESULT("자동완성 결과 없음"),

    ALARM_NOT_FOUND("등록된 알림이 없음"),

    // 인증 실패
    MEMBER_NOT_FOUND("요청한 멤버를 찾을 수 없음");

    private final String value;

    DiscordLogEventContent(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

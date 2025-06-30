package com.github.mathbook3948.zzibot.util;

import java.net.URI;

public class ChzzkUtil {

    /**
     * 치지직 방송 URL에서 채널 ID를 추출한다.
     * <p>
     * 예: {@code https://chzzk.naver.com/64d76089fba26b180d9c9e48a32600d9} → {@code 64d76089fba26b180d9c9e48a32600d9}
     * <p>
     * 경로(Path)의 첫 유의미 세그먼트를 반환하며, 쿼리 파라미터 등은 무시된다.
     *
     * @param url 치지직 방송 URL
     * @return 추출된 채널 ID, 실패 시 {@code null}
     */
    public static String extractChannelIdFromUrl(String url) {
        if (url == null) return null;

        try {
            URI uri = new URI(url);
            String path = uri.getPath();
            if (path == null) return null;

            String[] segments = path.split("/");
            for (String segment : segments) {
                if (!segment.isBlank()) {
                    return segment;
                }
            }
        } catch (Exception e) {
            return null;
        }

        return null;
    }
}

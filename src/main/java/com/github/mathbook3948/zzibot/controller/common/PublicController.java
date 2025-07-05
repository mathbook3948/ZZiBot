package com.github.mathbook3948.zzibot.controller.common;

import com.github.mathbook3948.zzibot.service.common.PublicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Open API", description = "공개 API 모음")
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private PublicService publicService;

    @Operation(summary = "서버 상태 확인", description = "서비스 상태 확인용 API. 응답: OK")
    @GetMapping("/status")
    public ResponseEntity<String> status() {
        return ResponseEntity.ok("OK");
    }

    @Operation(summary = "데이터베이스 상태 확인", description = "서비스 상태 확인용 API. 응답: OK: {now()}")
    @GetMapping("/db/status")
    public ResponseEntity<String> dbStatus() {
        return ResponseEntity.ok("OK: " + publicService.now());
    }

    @Operation(summary = "메인 화면 데이터 조회", description = "메인 화면 데이터 조회")
    @GetMapping("/main")
    public ResponseEntity<?> getMainContent() {
        return ResponseEntity.ok(publicService.getMainData());
    }
}

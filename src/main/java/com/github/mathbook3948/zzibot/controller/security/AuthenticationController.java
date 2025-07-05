package com.github.mathbook3948.zzibot.controller.security;

import com.github.mathbook3948.zzibot.dto.zzibot.auth.LoginDTO;
import com.github.mathbook3948.zzibot.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Hidden
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthService authService;

    @PostMapping("/admin/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO params) {
        return ResponseEntity.ok(authService.login(params));
    }
}

package com.github.mathbook3948.zzibot.controller.security;

import com.github.mathbook3948.zzibot.dto.zzibot.LoginDTO;
import com.github.mathbook3948.zzibot.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO params) {
        return ResponseEntity.ok(authService.login(params));
    }
}

package com.example.java_spring_boot.controller;

import com.example.java_spring_boot.dto.request.LoginRequest;
import com.example.java_spring_boot.dto.response.LoginResponse;
import com.example.java_spring_boot.service.LoginService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request
    ) {
        LoginResponse response = loginService.createToken(request);
        return ResponseEntity.ok(response);
    }

    @Hidden
    @GetMapping("/parseToken")
    public ResponseEntity<Map<String, Object>> parseToken(
            @RequestHeader String authorization
    ) {
        Map<String, Object> jwtPayload = loginService.parseToken(authorization);
        return ResponseEntity.ok(jwtPayload);
    }

    @PostMapping("/refreshAccessToken")
    public ResponseEntity<Map<String, String>> refreshAccessToken(
            @RequestBody Map<String, String> request
    ) {
        String refreshToken = request.get("refreshToken");
        String accessToken = loginService.refreshAccessToken(refreshToken);
        Map<String, String> response = Map.of("accessToken", accessToken);
        return ResponseEntity.ok(response);
    }
}

package com.example.java_spring_boot.service.Impl;

import com.example.java_spring_boot.dto.model.AppUserDetails;
import com.example.java_spring_boot.dto.request.LoginRequest;
import com.example.java_spring_boot.dto.response.LoginResponse;
import com.example.java_spring_boot.service.LoginService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {
    private final Key secretKey;
    private final int accessTokenTtlSeconds;
    private final int refreshTokenTtlSeconds;
    private final JwtParser jwtParser;
    private final AuthenticationProvider authenticationProvider;

    public LoginServiceImpl(Key secretKey, int accessTokenTtlSeconds, int refreshTokenTtlSeconds, JwtParser jwtParser,
                            AuthenticationProvider authenticationProvider) {
        this.secretKey = secretKey;
        this.accessTokenTtlSeconds = accessTokenTtlSeconds;
        this.refreshTokenTtlSeconds = refreshTokenTtlSeconds;
        this.jwtParser = jwtParser;
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    public LoginResponse createToken(LoginRequest request) {
        // 封裝帳密
        Authentication authToken = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        // 執行帳密認證
        authToken = authenticationProvider.authenticate(authToken);
        // 認證成功後取得結果
        AppUserDetails userDetails = (AppUserDetails) authToken.getPrincipal();
        // 產生token
        String accessToken = createAccessToken(userDetails.getUsername());
        String refreshToken = createRefreshToken(userDetails.getUsername());
        LoginResponse response = new LoginResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setUserId(userDetails.getId());
        response.setEmail(userDetails.getUsername());
        response.setUserAuthority(userDetails.getUserAuthority());
        response.setPremium(userDetails.getIsPremium());
        response.setTrailExpiration(userDetails.getTrailExpiration());

        return response;
    }

    public String refreshAccessToken(String refreshToken) {
        Map<String, Object> payload = parseToken(refreshToken);
        String username = (String) payload.get("username");
        return createAccessToken(username);
    }

    private String createAccessToken(String username) {
        // 有效時間
        long expirationMillis = Instant.now()
                .plusSeconds(accessTokenTtlSeconds)
                .getEpochSecond() * 1000;

        // 設置標準內容與自定義內容
        Claims claims = Jwts.claims();
        claims.setSubject("Access Token");  // 主旨
        claims.setIssuer("Amanda"); // 發行者
        claims.setIssuedAt(new Date()); // 核發時間
        claims.setExpiration(new Date(expirationMillis));   // 到期時間

        // username作為自定義內容放入JWT，簽名後就產生出來了
        claims.put("username", username);

        // 簽名後產生token
        return Jwts.builder()
                .setClaims(claims)
                .signWith(secretKey)
                .compact();
    }

    private String createRefreshToken(String username) {
        long expirationMillis = Instant.now()
                .plusSeconds(refreshTokenTtlSeconds)
                .getEpochSecond() * 1000;

        // 設置標準內容與自定義內容
        Claims claims = Jwts.claims();
        claims.setSubject("Refresh Token");  // 主旨
        claims.setIssuer("Amanda"); // 發行者
        claims.setIssuedAt(new Date()); // 核發時間
        claims.setExpiration(new Date(expirationMillis));   // 到期時間

        // 自定義內容放入JWT，簽名後就產生出來了
        claims.put("username", username);

        // 簽名後產生token
        return Jwts.builder()
                .setClaims(claims)
                .signWith(secretKey)
                .compact();
    }

    @Override
    public Map<String, Object> parseToken(String token) {
        String accessToken = token.replace("Bearer ", "");
        Claims claims = jwtParser.parseClaimsJws(accessToken).getBody();
        return new HashMap<>(claims);
    }
}

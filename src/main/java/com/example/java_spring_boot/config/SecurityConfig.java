package com.example.java_spring_boot.config;

import com.example.java_spring_boot.enums.UserAuthorityEnum;
import com.example.java_spring_boot.filter.JwtAuthenticationFilter;
import com.example.java_spring_boot.service.Impl.LoginServiceImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtAuthenticationFilter authFilter
    )throws Exception {
        return http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(HttpMethod.GET, "/users").hasAnyAuthority(UserAuthorityEnum.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/users/*").authenticated()
                        .requestMatchers(HttpMethod.GET).permitAll()
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/parseToken").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder)
            throws Exception {
        var provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        return provider;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public LoginServiceImpl loginServiceImpl(
            @Value("${security.jwt.key}") String key,
            @Value("${security.access-token-ttl-seconds}") int accessTokenTtlSeconds,
            @Value("${security.refresh-token-ttl-seconds}") int refreshTokenTtlSeconds,
            AuthenticationProvider authenticationProvider) {
        // key的字串要夠長，不然在製作密鑰時會出現錯誤訊息：The specified key byte array is 240 bits which is not secure enough for any JWT HMAC-SHA algorithm.
        var jwtSecretKey = Keys.hmacShaKeyFor(key.getBytes());
        var jwtParser = Jwts.parserBuilder().setSigningKey(jwtSecretKey).build();
        return new LoginServiceImpl(jwtSecretKey, accessTokenTtlSeconds, refreshTokenTtlSeconds, jwtParser, authenticationProvider);
    }
}
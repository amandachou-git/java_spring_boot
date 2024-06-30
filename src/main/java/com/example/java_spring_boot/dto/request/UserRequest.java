package com.example.java_spring_boot.dto.request;

import com.example.java_spring_boot.enums.UserAuthorityEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record UserRequest(
        @Schema(description = "The user's email", example = "abc@gmail.com", required = true)
        @NotNull
        String email,
        @Schema(description = "The user's name", example = "John")
        @NotNull
        String name,
        @Schema(description = "The user's password", example = "password", minLength = 8)
        @NotNull
        String password,
        @Schema(description = "The user's authority", example = "ADMIN")
        @NotNull
        UserAuthorityEnum authority,
        @Schema(description = "The user's enabled status", example = "true")
        @NotNull
        boolean isEnabled,
        @Schema(description = "The user's premium status", example = "true")
        @NotNull
        boolean isPremium
) {
}

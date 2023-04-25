package com.jareer.short_url.dto.auth;

public record NewPasswordDTO(
        String code,
        String password,
        String confirmPassword) {
}

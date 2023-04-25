package com.jareer.short_url.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * A DTO for the {@link com.jareer.short_url.entities.AuthUser} entity
 */

public record AuthUserCreateDTO(
        @NotBlank String username,
        @NotBlank String password,
        @NotBlank
        @Email(regexp = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-]+)(\\.[a-zA-Z]{2,5}){1,2}$") String email) implements Serializable {
}
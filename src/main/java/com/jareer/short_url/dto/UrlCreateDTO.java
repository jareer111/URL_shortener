package com.jareer.short_url.dto;

import com.jareer.short_url.entities.URL;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link URL} entity
 */


public record UrlCreateDTO(
        @NotBlank String path,
        @Future LocalDateTime expiresAt,
        String description) implements Serializable {
}
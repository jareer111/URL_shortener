package com.jareer.short_url.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link com.jareer.short_url.entities.Url} entity
 */


public record UrlCreateDto(
        @NotBlank String path,
         @Future LocalDateTime expiresAt,
        String description) implements Serializable {
}
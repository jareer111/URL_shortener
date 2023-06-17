package com.jareer.short_url.dto.report;

import com.jareer.short_url.entities.URL;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UrlReport {

    private String code;
    private String description;
    private String path;
    private boolean expired;
    private DayOfWeek dayOfWeek;

    public UrlReport(URL url) {
        this.code = url.getCode();
        this.dayOfWeek = url.getCreatedAt().toLocalDate().getDayOfWeek();
        this.description = url.getDescription();
        this.path = url.getPath();
        this.expired = url.getExpiresAt().isBefore(LocalDateTime.now());
    }
}

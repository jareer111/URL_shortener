package com.jareer.short_url.controllers;

import com.jareer.short_url.dto.UrlCreateDto;
import com.jareer.short_url.dto.report.WeeklyReportDTO;
import com.jareer.short_url.entities.Url;
import com.jareer.short_url.services.UrlService;
import com.jareer.short_url.utils.Report;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UrlController {

    private final UrlService urlService;
    private final Report report;

    @PostMapping("/api/url")
//    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Url> create(@NonNull @Valid @RequestBody UrlCreateDto dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(urlService.create(dto));
    }

//    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/url")
    public ResponseEntity<List<Url>> getAll() {
        return ResponseEntity.ok(urlService.getAll());
    }

    @GetMapping("/{code}")
    public ResponseEntity<Url> getUrlByTokenAndRedirect(@PathVariable String code) {
        return ResponseEntity.ok(urlService.getByCode(code));
    }

    @GetMapping("/report")
    public ResponseEntity<WeeklyReportDTO> report() {
        return ResponseEntity.ok(this.report.report());
    }
}

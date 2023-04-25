package com.jareer.short_url.services;

import com.jareer.short_url.dto.UrlCreateDto;
import com.jareer.short_url.entities.Url;
import org.springframework.lang.NonNull;

import java.util.List;

public interface UrlService {
    Url create(@NonNull UrlCreateDto dto);

    List<Url> getAll();

    Url getByCode(@NonNull String code);

    List<Url> lastWeek();
}

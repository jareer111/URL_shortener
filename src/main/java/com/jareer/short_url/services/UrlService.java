package com.jareer.short_url.services;

import com.jareer.short_url.dto.UrlCreateDTO;
import com.jareer.short_url.entities.URL;
import org.springframework.lang.NonNull;

import java.util.List;

public interface UrlService {
    URL create(@NonNull UrlCreateDTO dto);

    List<URL> getAll();

    URL getByCode(@NonNull String code);

    List<URL> lastWeek();
}

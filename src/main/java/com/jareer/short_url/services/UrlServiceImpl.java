package com.jareer.short_url.services;

import com.jareer.short_url.config.security.SessionUser;
import com.jareer.short_url.dto.UrlCreateDTO;
import com.jareer.short_url.entities.URL;
import com.jareer.short_url.exceptions.NotFoundException;
import com.jareer.short_url.exceptions.UrlExpiredException;
import com.jareer.short_url.mappers.UrlMapper;
import com.jareer.short_url.repositories.UrlRepository;
import com.jareer.short_url.utils.BaseUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@Slf4j
public record UrlServiceImpl(
        UrlMapper urlMapper,
        UrlRepository urlRepository,
        BaseUtils utils,
        SessionUser sessionUser) implements UrlService {

    @Override
    public URL create(@NonNull UrlCreateDTO dto) {
        log.info("Url Created With : {}, userId : {}", dto, sessionUser.id());
        URL url = urlMapper.toEntity(dto);
        url.setCode(utils.hashUrl(dto.path()));
        return urlRepository.save(url);
    }

    @Override
    public List<URL> getAll() {
        log.info("Urls List Requested userId : {}", sessionUser.id());
        return urlRepository.findByCreatedBy(sessionUser.id());
    }

    @Override
    public URL getByCode(@NotNull String code) {
        URL url = urlRepository.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Url Not Found"));
        if (url.getExpiresAt().isBefore(LocalDateTime.now()))
            throw new UrlExpiredException("Url expired");
        return url;
    }

    @Override
    public List<URL> lastWeek() {
        LocalDateTime monday = LocalDateTime.now().minusWeeks(1).with(DayOfWeek.MONDAY).with(LocalTime.MIN);
        LocalDateTime sunday = LocalDateTime.now().minusWeeks(1).with(DayOfWeek.SUNDAY).with(LocalTime.MAX);
        return urlRepository.weeklyReport(monday, sunday);
    }
}

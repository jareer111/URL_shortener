package com.jareer.short_url.mappers;

import com.jareer.short_url.dto.UrlCreateDto;
import com.jareer.short_url.entities.Url;
import org.mapstruct.Mapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface UrlMapper {
    Url toEntity(@NonNull UrlCreateDto dto);
}

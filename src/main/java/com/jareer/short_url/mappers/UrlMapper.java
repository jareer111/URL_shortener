package com.jareer.short_url.mappers;

import com.jareer.short_url.dto.UrlCreateDTO;
import com.jareer.short_url.entities.URL;
import org.mapstruct.Mapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface UrlMapper {
    URL toEntity(@NonNull UrlCreateDTO dto);
}

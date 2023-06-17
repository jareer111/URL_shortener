package com.jareer.short_url.mappers;

import com.jareer.short_url.dto.auth.AuthUserCreateDTO;
import com.jareer.short_url.entities.AuthUser;
import org.mapstruct.Mapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;


@Component
@Mapper(componentModel = "spring")
public interface AuthUserMapper {
    AuthUser toEntity(@NonNull AuthUserCreateDTO dto);

}

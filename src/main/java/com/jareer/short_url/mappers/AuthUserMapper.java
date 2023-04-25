package com.jareer.short_url.mappers;

import com.jareer.short_url.dto.auth.AuthUserCreateDTO;
import com.jareer.short_url.entities.AuthUser;
import org.mapstruct.Mapper;
import org.springframework.lang.NonNull;

@Mapper(componentModel = "spring")
public interface AuthUserMapper {
    AuthUser toEntity(@NonNull AuthUserCreateDTO dto);

}

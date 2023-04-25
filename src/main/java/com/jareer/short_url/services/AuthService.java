package com.jareer.short_url.services;

import com.jareer.short_url.dto.auth.*;
import com.jareer.short_url.entities.AuthUser;
import org.springframework.lang.NonNull;

public interface AuthService {

    TokenResponse generateToken(@NonNull TokenRequest tokenRequest);

    AuthUser create(@NonNull AuthUserCreateDTO dto);

    boolean activateUserByOTP(String code);

    TokenResponse refreshToken(@NonNull RefreshTokenRequest refreshTokenRequest);

    String sendActionCode(@NonNull ActivationCodeResendDTO dto);

}

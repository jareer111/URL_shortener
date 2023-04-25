package com.jareer.short_url.services;

import com.jareer.short_url.config.security.JwtTokenUtil;
import com.jareer.short_url.dto.auth.*;
import com.jareer.short_url.entities.AuthUser;
import com.jareer.short_url.entities.AuthUserOtp;
import com.jareer.short_url.exceptions.NotFoundException;
import com.jareer.short_url.mappers.AuthUserMapper;
import com.jareer.short_url.repositories.AuthUserOtpRepository;
import com.jareer.short_url.repositories.AuthUserRepository;
import com.jareer.short_url.utils.BaseUtils;
import com.jareer.short_url.utils.MailSenderService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

import static com.jareer.short_url.enums.TokenType.REFRESH;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final AuthUserMapper authUserMapper;
    private final AuthUserRepository authUserRepository;
    private final AuthUserOtpRepository authUserOtpRepository;
    private final PasswordEncoder passwordEncoder;
    private final BaseUtils utils;
    private final MailSenderService mailService;
    private final JwtTokenUtil jwtTokenUtil;
    private static final String basePATH = "http://localhost:8080/api/auth/activate/";


    @Override
    public TokenResponse generateToken(@NonNull TokenRequest tokenRequest) {
        String username = tokenRequest.username();
        String password = tokenRequest.password();
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(username, password);
        authenticationManager.authenticate(authentication);
        return jwtTokenUtil.generateToken(username);
    }

    @Override
    public AuthUser create(@NotNull AuthUserCreateDTO dto) {
        AuthUser authUser = authUserMapper.toEntity(dto);
        authUser.setPassword(passwordEncoder.encode(dto.password()));
        authUser.setRole("USER");
        AuthUserOtp authUserOtp = AuthUserOtp.childBuilder()
                .userID(authUser.getId())
                .code(utils.activationCode(authUser.getId()))
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .otpType(AuthUserOtp.OtpType.ACCOUNT_ACTIVATE)
                .build();

        Map<String, String> model = Map.of(
                "username", authUser.getUsername(),
                "to", authUser.getEmail(),
                "subject", "Activate Your Account",
                "url", basePATH + authUserOtp.getCode(),
                "id", "url_id",
                "path", "classpath:/static/img/url.png");
        authUserOtpRepository.save(authUserOtp);
        mailService.sendActivationMail(model);
        return authUserRepository.save(authUser);
    }

    @Override
    /*@Transactional (noRollbackFor = RuntimeException.class)*/
    public boolean activateUserByOTP(@NonNull String code) {
        AuthUserOtp authUserOtp = authUserOtpRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("OTP code is invalid"));

        if (authUserOtp.getExpiresAt().isBefore(LocalDateTime.now())) {
            authUserOtp.setDeleted(true);
            authUserOtpRepository.save(authUserOtp);
            throw new RuntimeException("OTP code is invalid");
        }
        Long userID = authUserOtp.getUserID();
        authUserRepository.activeUser(userID);
        return true;
    }

    @Override
    public TokenResponse refreshToken(@NotNull RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.refreshToken();
        if (!jwtTokenUtil.isValid(refreshToken, REFRESH))
            throw new CredentialsExpiredException("Token is invalid");
        String username = jwtTokenUtil.getUsername(refreshToken, REFRESH);
        authUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found "));

        TokenResponse tokenResponse = TokenResponse.builder()
                .refreshToken(refreshToken)
                .refreshTokenExpiry(jwtTokenUtil.getExpiry(refreshToken, REFRESH))
                .build();
        return jwtTokenUtil.generateAccessToken(username, tokenResponse);
    }

    @Override
    public String sendActionCode(@NotNull ActivationCodeResendDTO dto) {
        AuthUser authUser = authUserRepository.findByEmail(dto.email())
                .orElseThrow(() -> new NotFoundException("User not found with Email"));
        authUserOtpRepository.deleteOTPsByUserID(authUser.getId(), AuthUserOtp.OtpType.ACCOUNT_ACTIVATE);

        AuthUserOtp authUserOtp = AuthUserOtp.childBuilder()
                .userID(authUser.getId())
                .code(utils.activationCode(authUser.getId()))
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .otpType(AuthUserOtp.OtpType.ACCOUNT_ACTIVATE)
                .build();
        Map<String, String> model = Map.of(
                "username", authUser.getUsername(),
                "to", authUser.getEmail(),
                "subject", "Activate Your Account",
                "url", basePATH + authUserOtp.getCode(),
                "id", "url_id",
                "path", "/static/img/url.png");
        authUserOtpRepository.save(authUserOtp);
        mailService.sendActivationMail(model);
        return "Activation Link Sent";
    }

}

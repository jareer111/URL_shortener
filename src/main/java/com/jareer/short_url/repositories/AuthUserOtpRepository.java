package com.jareer.short_url.repositories;

import com.jareer.short_url.entities.AuthUserOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AuthUserOtpRepository extends JpaRepository<AuthUserOtp, Long> {
    @Query("select a from AuthUserOtp a where a.code = ?1 and  a.deleted = false")
    Optional<AuthUserOtp> findByCode(String code);


    @Modifying
    @Query("update AuthUserOtp a set a.deleted = true where a.createdBy = :userID and a.otpType = :otpType")
    void deleteOTPsByUserID(@Param("userID") Long userID, AuthUserOtp.OtpType otpType);
}
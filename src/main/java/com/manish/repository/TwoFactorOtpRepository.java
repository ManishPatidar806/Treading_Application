package com.manish.repository;

import com.manish.model.TwoFactorOtp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TwoFactorOtpRepository extends JpaRepository<TwoFactorOtp, String> {
    TwoFactorOtp findByUserId(Long userId);
}

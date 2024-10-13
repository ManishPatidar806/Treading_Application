package com.manish.repository;

import com.manish.model.ForgotPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPasswordToken , String> {
    ForgotPasswordToken findByUserId(Long userId);
}

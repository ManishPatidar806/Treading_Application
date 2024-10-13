package com.manish.service;

import com.manish.domain.VERIFICATION_TYPE;
import com.manish.model.ForgotPasswordToken;
import com.manish.model.User;

public interface ForgotPasswordService {

    ForgotPasswordToken createToken(User user , String id , String otp , VERIFICATION_TYPE VERIFICATIONTYPE, String sendTo);

    ForgotPasswordToken findById(String id);

    ForgotPasswordToken findByUser(Long userId);

    void deleteToken(ForgotPasswordToken token);



}

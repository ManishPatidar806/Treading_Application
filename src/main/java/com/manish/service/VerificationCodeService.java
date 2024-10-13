package com.manish.service;

import com.manish.domain.VERIFICATION_TYPE;
import com.manish.model.User;
import com.manish.model.VerificationCode;

public interface VerificationCodeService {
    VerificationCode sendVerificationCode(User user , VERIFICATION_TYPE VERIFICATIONTYPE);

    VerificationCode getVerificationCodeById(Long id);

    VerificationCode getVerificationCodeByUser(Long userId);


    void deleteVerificationCodeById(VerificationCode verificationCode);




}

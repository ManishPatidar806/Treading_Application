package com.manish.service;

import com.manish.domain.VERIFICATION_TYPE;
import com.manish.model.ForgotPasswordToken;
import com.manish.model.User;
import com.manish.repository.ForgotPasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    @Autowired
    private ForgotPasswordRepository forgotPasswordRepository;

    @Override
    public ForgotPasswordToken createToken(User user, String id, String otp, VERIFICATION_TYPE VERIFICATIONTYPE, String sendTo) {
        ForgotPasswordToken forgotPasswordToken = new ForgotPasswordToken();
        forgotPasswordToken.setUser(user);
        forgotPasswordToken.setSendTo(sendTo);
        forgotPasswordToken.setVERIFICATIONTYPE(VERIFICATIONTYPE);
        forgotPasswordToken.setOtp(otp);
        forgotPasswordToken.setId(id);
        return forgotPasswordRepository.save(forgotPasswordToken);
    }

    @Override
    public ForgotPasswordToken findById(String id) {
        Optional<ForgotPasswordToken> token =  forgotPasswordRepository.findById(id);
        return token.orElse(null);
    }

    @Override
    public ForgotPasswordToken findByUser(Long userId) {
        return forgotPasswordRepository.findByUserId(userId);
    }


    @Override
    public void deleteToken(ForgotPasswordToken token) {
forgotPasswordRepository.delete(token);

    }
}

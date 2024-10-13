package com.manish.service;

import com.manish.domain.VERIFICATION_TYPE;
import com.manish.model.User;
import com.manish.model.VerificationCode;
import com.manish.repository.VerificationCodeRepository;
import com.manish.utils.OtpUtils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;


@Service
public class VerificationCodeServiceImpl implements VerificationCodeService{

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Override
    public VerificationCode sendVerificationCode(User user, VERIFICATION_TYPE VERIFICATIONTYPE) {
        VerificationCode verificationCode1 = new VerificationCode();
        verificationCode1.setOtp(OtpUtils.generateOtp());
        verificationCode1.setVerificationType(VERIFICATIONTYPE);
        verificationCode1.setUser(user);

        return verificationCodeRepository.save(verificationCode1);
    }

    @SneakyThrows
    @Override
    public VerificationCode getVerificationCodeById(Long id) {
        Optional<VerificationCode>verificationCode = verificationCodeRepository.findById(id);
        if (verificationCode.isPresent()) {
            return verificationCode.get();
        }

        throw  new Exception("Verification code not found");

    }

    @Override
    public VerificationCode getVerificationCodeByUser(Long userId) {
        return verificationCodeRepository.findByUserId(userId);
    }

    @Override
    public void deleteVerificationCodeById(VerificationCode verificationCode) {
    verificationCodeRepository.delete(verificationCode);
    }
}

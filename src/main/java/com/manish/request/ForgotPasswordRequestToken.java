package com.manish.request;

import com.manish.domain.VERIFICATION_TYPE;
import lombok.Data;

@Data
public class ForgotPasswordRequestToken {

    private String sendTo;
    private VERIFICATION_TYPE VERIFICATIONTYPE;



}

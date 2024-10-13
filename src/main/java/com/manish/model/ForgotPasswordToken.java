package com.manish.model;


import com.manish.domain.VERIFICATION_TYPE;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ForgotPasswordToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @OneToOne
    private User user;

    private String otp;
    private VERIFICATION_TYPE VERIFICATIONTYPE;

    private String sendTo;




}

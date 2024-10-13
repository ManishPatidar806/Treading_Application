package com.manish.controller;

import com.manish.request.ForgotPasswordRequestToken;
import com.manish.domain.VERIFICATION_TYPE;
import com.manish.model.ForgotPasswordToken;
import com.manish.model.User;
import com.manish.model.VerificationCode;
import com.manish.request.ResetPasswordRequest;
import com.manish.response.ApiResponse;
import com.manish.response.AuthResponse;
import com.manish.service.EmailService;
import com.manish.service.ForgotPasswordService;
import com.manish.service.UserService;
import com.manish.service.VerificationCodeService;
import com.manish.utils.OtpUtils;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private  UserService userService;

    @Autowired
    private EmailService emailService;
    private String jwt;
    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @GetMapping("/api/users/profile")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt){
        User user = userService.findUserProfileByJwt(jwt);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }



    @PostMapping("/api/users/verification/{VERIFICATIONTYPE}/send-otp")
    public ResponseEntity<String> sendVerificationOtp(
            @RequestHeader("Authorization") String jwt,
            @PathVariable VERIFICATION_TYPE VERIFICATIONTYPE) throws MessagingException {
        User user = userService.findUserProfileByJwt(jwt);

        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUser(user.getId());
        if (verificationCode == null) {
            verificationCode = verificationCodeService.sendVerificationCode(user, VERIFICATIONTYPE);

        }
        if(VERIFICATIONTYPE.equals(VERIFICATION_TYPE.EMAIL)){
            emailService.sendVerificationOtpEmail(user.getEmail(), verificationCode.getOtp());
        }


        return new ResponseEntity<String>("verification otp send Successfully.........", HttpStatus.OK);
    }

    @PatchMapping("/api/users/enable-two-factor/verify-otp/{otp}")
    public ResponseEntity<User> enableTwoFactorAuthentication(
            @PathVariable String otp,
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUser(user.getId());

        String sendTo = verificationCode.getVerificationType().equals(VERIFICATION_TYPE.EMAIL)?
                verificationCode.getEmail(): verificationCode.getMobile();

        boolean isVerified = verificationCode.getOtp().equals(otp);

            if (isVerified) {
                User updatedUser = userService.enableTwoFactorAuthentication(verificationCode.getVerificationType()
                ,sendTo,user);

             verificationCodeService.deleteVerificationCodeById(verificationCode);
             return new ResponseEntity<>(updatedUser, HttpStatus.OK);

            }
        throw new Exception("Wrong Otp");

    }




    @PostMapping("/auth/users/reset-password/send-otp")
    public ResponseEntity<AuthResponse> sendForgotPasswordOtp(

            @RequestBody ForgotPasswordRequestToken req) throws MessagingException {

        User user = userService.findUserByEmail(req.getSendTo());
        String otp = OtpUtils.generateOtp();
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();

        ForgotPasswordToken token = forgotPasswordService.findByUser(user.getId());

        if(token==null){
            token = forgotPasswordService.createToken(user,id, otp, req.getVERIFICATIONTYPE(),req.getSendTo());

        }
        if(req.getVERIFICATIONTYPE().equals(VERIFICATION_TYPE.EMAIL)){
            emailService.sendVerificationOtpEmail(user.getEmail(), token.getOtp());

        }


        AuthResponse response = new AuthResponse();
        response.setSession(token.getId());
        response.setMessage("Password Reset Otp Sent Successfully...........");


        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PatchMapping("/auth/users/reset-password/verify-otp")
    public ResponseEntity<ApiResponse> resetPassword(
            @RequestParam String id,
            @RequestParam ResetPasswordRequest req,
            @RequestHeader("Authorization") String jwt) throws Exception {

        ForgotPasswordToken forgotPasswordToken = forgotPasswordService.findById(id);

        boolean isVerified =forgotPasswordToken.getOtp().equals(req.getOtp());
        if(isVerified){
            userService.updatePassword(forgotPasswordToken.getUser(),req.getPassword());
            ApiResponse res = new ApiResponse();
            res.setMessage("Password Update Successfully.............");
            return new ResponseEntity<>(res,HttpStatus.ACCEPTED);
        }

        throw new Exception("Wrong Otp");



    }






















}

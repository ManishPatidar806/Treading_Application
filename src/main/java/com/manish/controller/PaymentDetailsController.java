package com.manish.controller;

import com.manish.model.PaymentDetails;
import com.manish.model.User;
import com.manish.service.PaymentDetailsService;
import com.manish.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PaymentDetailsController {
    @Autowired
    private PaymentDetailsService paymentDetailsService;
    @Autowired
    private UserService userService;

    @PostMapping("/payment-details")
    public ResponseEntity<PaymentDetails> addPaymentDetails(@RequestBody PaymentDetails paymentDetailsRequest,
                                                            @RequestHeader("Authorization") String jwt) {

        User user = userService.findUserProfileByJwt(jwt);
        PaymentDetails paymentDetails = paymentDetailsService.addPaymentDetails(
                paymentDetailsRequest.getAccountNumber(),
                paymentDetailsRequest.getAccountHolderName(),
                paymentDetailsRequest.getIfscCode(),
                paymentDetailsRequest.getBankName(),
                user
        );
        return new ResponseEntity<>(paymentDetails , HttpStatus.CREATED);
    }

    @GetMapping("/payment-details")
    public ResponseEntity<PaymentDetails> getUsersPaymentDetails(
                                                            @RequestHeader("Authorization") String jwt) {

        User user = userService.findUserProfileByJwt(jwt);
        PaymentDetails paymentDetails = paymentDetailsService.getUserPaymentDetails(user);
        return new ResponseEntity<>(paymentDetails , HttpStatus.CREATED);
    }

}

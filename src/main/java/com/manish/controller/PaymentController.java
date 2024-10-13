package com.manish.controller;

import com.manish.domain.PAYMENT_METHOD;
import com.manish.model.PaymentOrder;
import com.manish.model.User;
import com.manish.response.PaymentResponse;
import com.manish.service.PaymentService;
import com.manish.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserService userService;


    @PostMapping("/api/payment/{paymentMethod}/amount/{amount}")
    public ResponseEntity<PaymentResponse> paymentHandler(
            @PathVariable PAYMENT_METHOD paymentMethod,
            @PathVariable Long amount,
            @RequestHeader("Authorization") String jwt
            ){

        User user = userService.findUserProfileByJwt(jwt);

        PaymentResponse paymentResponse ;
        PaymentOrder order = paymentService.createOrder(user,amount,paymentMethod);
        if (paymentMethod.equals(PAYMENT_METHOD.RAZORPAY)){
            paymentResponse=paymentService.createRazorpayPaymentLing(user,amount ,order.getId());

        }else{
            paymentResponse = paymentService.createStringPaymentLing(user,amount, order.getId());
        }

    return new ResponseEntity<>(paymentResponse , HttpStatus.CREATED);

    }

}

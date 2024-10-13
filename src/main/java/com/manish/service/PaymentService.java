package com.manish.service;

import com.manish.domain.PAYMENT_METHOD;
import com.manish.model.PaymentOrder;
import com.manish.model.User;
import com.manish.response.PaymentResponse;

public interface PaymentService {
    PaymentOrder createOrder(User user, Long amount, PAYMENT_METHOD paymentMethod);
    PaymentOrder getPaymentOrderById(Long id);

    Boolean proccedPaymentOrder(PaymentOrder paymentOrder , String paymentId);

    PaymentResponse createRazorpayPaymentLing(User user , Long amount ,Long orderId);

    PaymentResponse createStringPaymentLing(User user , Long amount,Long orderId);


}

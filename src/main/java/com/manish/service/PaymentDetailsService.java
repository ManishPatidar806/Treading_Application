package com.manish.service;

import com.manish.model.PaymentDetails;
import com.manish.model.User;

public interface PaymentDetailsService {

    public PaymentDetails addPaymentDetails(String accountNumber , String accountHolderName,
                                            String ifsc, String bankName, User user
    );

    public PaymentDetails getUserPaymentDetails(User user);



}

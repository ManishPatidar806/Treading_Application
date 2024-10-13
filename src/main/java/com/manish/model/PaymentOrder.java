package com.manish.model;


import com.manish.domain.PAYMENT_METHOD;
import com.manish.domain.PAYMENT_ORDER_STATUS;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data

public class PaymentOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long amount;

    private PAYMENT_ORDER_STATUS status;

    private PAYMENT_METHOD paymentMethod;

    @ManyToOne
    private User user;

}

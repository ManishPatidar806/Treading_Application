package com.manish.model;


import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    @OneToOne
    private User user;

    private BigDecimal balance=BigDecimal.ZERO;







}

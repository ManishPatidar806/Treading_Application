package com.manish.model;


import com.manish.domain.WITHDRAWL_STATUS;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Withdrawal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private WITHDRAWL_STATUS status;

    private Long amount;
    @ManyToOne
    private User user;

    private LocalDateTime date = LocalDateTime.now();


}

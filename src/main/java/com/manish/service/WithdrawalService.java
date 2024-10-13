package com.manish.service;

import com.manish.model.User;
import com.manish.model.Withdrawal;

import java.util.List;

public interface WithdrawalService {

    Withdrawal requestyWithdrawal(Long amount , User user);

    Withdrawal procedWithdrawal(Long withdrawalId , boolean accept);
    List<Withdrawal> getUsersWithdrawalHistory(User user);

    List<Withdrawal> getAllWithdrawalRequest();

}

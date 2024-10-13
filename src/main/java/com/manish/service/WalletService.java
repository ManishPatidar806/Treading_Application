package com.manish.service;

import com.manish.model.Order;
import com.manish.model.User;
import com.manish.model.Wallet;

public interface WalletService {

    Wallet getUserWallet(User user);

    Wallet addBalance(Wallet wallet , Long money);

    Wallet findWalletById(Long id);
    Wallet walletToWalletTransfer(User sender , Wallet receiverWallet,Long amount);
    Wallet payOrderPayment(Order order, User user);


}

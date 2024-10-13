package com.manish.service;

import com.manish.domain.ORDER_TYPE;
import com.manish.model.Order;
import com.manish.model.User;
import com.manish.model.Wallet;
import com.manish.repository.WalletRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WalletRepository walletRepository;


    @Override
    public Wallet getUserWallet(User user) {
        Wallet wallet = walletRepository.findByUserId(user.getId());
        if(wallet == null) {
            Wallet newWallet= new Wallet();
            newWallet.setUser(user);
            walletRepository.save(newWallet);
        }
        return wallet;
    }

    @Override
    public Wallet addBalance(Wallet wallet, Long money) {
        BigDecimal balance = wallet.getBalance();
        BigDecimal newBalance = balance.add(BigDecimal.valueOf(money));
        wallet.setBalance(newBalance);
        return walletRepository.save(wallet);
    }
    @SneakyThrows
    @Override
    public Wallet findWalletById(Long id) {
        Optional<Wallet> walletOptional =  walletRepository.findById(id);
        if(walletOptional.isPresent()) {

            return walletOptional.get();
        }
        throw new Exception("Wallet not Found.....");
    }
    @SneakyThrows
    @Override
    public Wallet walletToWalletTransfer(User sender, Wallet receiverWallet, Long amount) {
      Wallet senderWallet = getUserWallet(sender);
      if(senderWallet.getBalance().compareTo(BigDecimal.valueOf(amount)) < 0) {
          throw new Exception("Insufficient Balance...........");
      }

      BigDecimal balance = senderWallet.getBalance().subtract(BigDecimal.valueOf(amount));
      senderWallet.setBalance(balance);
      walletRepository.save(senderWallet);

      BigDecimal transferAmount = receiverWallet.getBalance().add(BigDecimal.valueOf(amount));
      receiverWallet.setBalance(transferAmount);
      walletRepository.save(receiverWallet);


        return senderWallet;
    }
    @SneakyThrows
    @Override
    public Wallet payOrderPayment(Order order, User user) {
        Wallet wallet = getUserWallet(user);

        if (order.getOrderType().equals(ORDER_TYPE.Buy)) {
            BigDecimal newBalance = wallet.getBalance().subtract(order.getPrice());
            if (newBalance.compareTo(order.getPrice()) < 0) {
                throw new Exception("Insufficient Funds for this Transaction.......");

            }
            wallet.setBalance(newBalance);

        }else if (order.getOrderType().equals(ORDER_TYPE.Sell)) {
            BigDecimal newBalance = wallet.getBalance().add(order.getPrice());
            wallet.setBalance(newBalance);

        }
            walletRepository.save(wallet);
            return wallet;

    }
}

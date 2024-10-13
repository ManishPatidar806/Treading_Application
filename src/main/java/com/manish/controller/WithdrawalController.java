package com.manish.controller;

import com.manish.domain.WALLET_TRANSACTION_TYPE;
import com.manish.model.User;
import com.manish.model.Wallet;
import com.manish.model.WalletTransaction;
import com.manish.model.Withdrawal;
import com.manish.service.UserService;
import com.manish.service.WalletService;
import com.manish.service.WithdrawalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/withdrawal")
public class WithdrawalController {

    @Autowired
    private WithdrawalService withdrawalService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

//   private TransactionService;

    @PostMapping("/api/withdrawal/{amount}")
    public ResponseEntity<?> withdrawal(@RequestHeader("Authorization") String jwt
            , @PathVariable("amount") Long amount) {

        User user = userService.findUserProfileByJwt(jwt);
        Wallet userWallet = walletService.getUserWallet(user);

        Withdrawal withdrawal = withdrawalService.requestyWithdrawal(amount, user);

        walletService.addBalance(userWallet,-withdrawal.getAmount());

//        WalletTransaction walletTransaction = wallletTransactionSevice.createTransaction(userWallet,
//                WALLET_TRANSACTION_TYPE.WITHDRAWL,null,"bank account withdrawl",withdrawal.getAmount());
//

        return new ResponseEntity<>(withdrawal, HttpStatus.OK);

    }


    @PostMapping("/api/admin/withdrawal/{id}/proceed/{accept}")
    public ResponseEntity<?> proceedWithdrawal(@RequestHeader("Authorization") String jwt
            , @PathVariable Long id, @PathVariable boolean accept) {

        User user = userService.findUserProfileByJwt(jwt);

        Withdrawal withdrawal = withdrawalService.procedWithdrawal(id,accept);

        Wallet userWallet = walletService.getUserWallet(user);

        if (!accept){
            walletService.addBalance(userWallet,withdrawal.getAmount());
        }

        return new ResponseEntity<>(withdrawal, HttpStatus.OK);

    }


    @PostMapping("/api/withdrawal")
    public ResponseEntity <List<Withdrawal>> getWithdrawalHistory(
            @RequestHeader("Authorization") String jwt
           ) {

        User user = userService.findUserProfileByJwt(jwt);

        List<Withdrawal>withdrawal = withdrawalService.getUsersWithdrawalHistory(user);


        return new ResponseEntity<>(withdrawal, HttpStatus.OK);

    }



    @PostMapping("/api/admin/withdrawal")
    public ResponseEntity <List<Withdrawal>> getAllWithdrawalRequest(
            @RequestHeader("Authorization") String jwt
    ) {

        User user = userService.findUserProfileByJwt(jwt);

        List<Withdrawal>withdrawal = withdrawalService.getAllWithdrawalRequest();


        return new ResponseEntity<>(withdrawal, HttpStatus.OK);

    }




}

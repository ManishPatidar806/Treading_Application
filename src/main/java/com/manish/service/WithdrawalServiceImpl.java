package com.manish.service;

import com.manish.domain.WITHDRAWL_STATUS;
import com.manish.model.User;
import com.manish.model.Withdrawal;
import com.manish.repository.WithdrawalRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WithdrawalServiceImpl implements WithdrawalService {

    @Autowired
    private WithdrawalRepository withdrawalRepository;



    @Override
    public Withdrawal requestyWithdrawal(Long amount, User user) {
    Withdrawal withdrawal = new Withdrawal();
    withdrawal.setAmount(amount);
    withdrawal.setUser(user);
    withdrawal.setStatus(WITHDRAWL_STATUS.PENDING);

        return withdrawalRepository.save(withdrawal);
    }
    @SneakyThrows
    @Override
    public Withdrawal procedWithdrawal(Long withdrawalId, boolean accept) {
        Optional<Withdrawal> withdrawal = withdrawalRepository.findById(withdrawalId);
        if(withdrawal.isEmpty()){
            throw new Exception("withDrawal not found");
        }
        Withdrawal withdrawal1 = withdrawal.get();

        withdrawal1.setDate(LocalDateTime.now());
        if(accept){
            withdrawal1.setStatus(WITHDRAWL_STATUS.SUCCESS);
        }else{
            withdrawal1.setStatus(WITHDRAWL_STATUS.DECLINE);
        }

        return withdrawalRepository.save(withdrawal1);
    }

    @Override
    public List<Withdrawal> getUsersWithdrawalHistory(User user) {
        return withdrawalRepository.findByUserId(user.getId());
    }

    @Override
    public List<Withdrawal> getAllWithdrawalRequest() {
        return withdrawalRepository.findAll();
    }
}

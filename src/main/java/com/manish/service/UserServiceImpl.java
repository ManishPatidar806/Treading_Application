package com.manish.service;

import com.manish.config.JwtProvider;
import com.manish.domain.VERIFICATION_TYPE;
import com.manish.model.TwoFactorAuth;
import com.manish.model.User;
import com.manish.repository.UserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;

    @SneakyThrows
    @Override
    public User findUserProfileByJwt(String jwt) {
    String email = JwtProvider.getEmailFromToken(jwt);
    User user = userRepository.findByEmail(email);
    if(user == null) {
        throw new Exception("User not found");
    }

        return user;
    }
    @SneakyThrows
    @Override
    public User findUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if(user == null) {
            throw new Exception("User not found");
        }

        return user;
    }
    @SneakyThrows
    @Override
    public User findUserById(Long id){
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) {
            throw new Exception("user not found");
        }
        return user.get();
    }

    @Override
    public User enableTwoFactorAuthentication(VERIFICATION_TYPE VERIFICATIONTYPE, String sendTo, User user) {
        TwoFactorAuth twoFactorAuth = new TwoFactorAuth();
        twoFactorAuth.setEnabled(true);
        twoFactorAuth.setSendTo(VERIFICATIONTYPE);
        user.setTwoFactorAuth(twoFactorAuth);
        return userRepository.save(user);
    }


    @Override
    public User updatePassword(User user, String newPassword) {
        user.setPassword(newPassword);
        return userRepository.save(user);
    }
}

package com.manish.service;

import com.manish.domain.VERIFICATION_TYPE;
import com.manish.model.User;

public interface UserService {

public User findUserProfileByJwt(String jwt);
public User findUserByEmail(String email);
public User findUserById(Long id);
public User enableTwoFactorAuthentication(VERIFICATION_TYPE VERIFICATIONTYPE, String sendTo , User user);
User updatePassword(User user , String newPassword);




}

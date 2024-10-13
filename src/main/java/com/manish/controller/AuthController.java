package com.manish.controller;

import com.manish.config.JwtProvider;
import com.manish.model.TwoFactorOtp;
import com.manish.model.User;
import com.manish.repository.UserRepository;
import com.manish.response.AuthResponse;
import com.manish.service.CustomeUserDetails;
import com.manish.service.EmailService;
import com.manish.service.TwoFactorOtpService;
import com.manish.service.WatchlistService;
import com.manish.utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomeUserDetails customeUserDetails;

    @Autowired
    private TwoFactorOtpService twoFactorOtpService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private WatchlistService watchlistService;


@PostMapping("/signup")
    public ResponseEntity<AuthResponse> register(@RequestBody User user) throws Exception {


        User isEmailExist = userRepository.findByEmail(user.getEmail());
        if(isEmailExist!=null){
            throw new Exception("Email is already used in another account");
        }


    User newUser = new User();
    newUser.setEmail(user.getEmail());
    newUser.setPassword(user.getPassword());
    newUser.setFullName(user.getFullName());
    User savedUser = userRepository.save(newUser);

    watchlistService.createWatchlist(savedUser);
    Authentication auth = new UsernamePasswordAuthenticationToken(
            user.getEmail(),
            user.getPassword()

    );

    SecurityContextHolder.getContext().setAuthentication(auth);
    String jwt = JwtProvider.generateToken(auth);
    AuthResponse res = new AuthResponse();
    res.setJwt(jwt);
    res.setStatus(true);
    res.setMessage("Register Successfully");
       return  new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> login(@RequestBody User user) throws Exception {
        String username = user.getEmail();
        String password = user.getPassword();
       Authentication auth =  authenticate(username,password);
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwt = JwtProvider.generateToken(auth);
        User authuser = userRepository.findByEmail(username);
        if (user.getTwoFactorAuth().isEnabled()){
            AuthResponse res = new AuthResponse();
            res.setMessage("Two factor auth is enabled");
            res.setTwoFactorAuthEnabled(true);
            String otp = OtpUtils.generateOtp();
            TwoFactorOtp oldTwoFactorOtp = twoFactorOtpService.findByUser(authuser.getId());
            if(oldTwoFactorOtp!=null){
                twoFactorOtpService.deleteTwoFactorOtp(oldTwoFactorOtp);
            }
            TwoFactorOtp newTwoFactorOtp = twoFactorOtpService.createTwoFactorOtp(authuser,otp,jwt);
            emailService.sendVerificationOtpEmail(username , otp);
            res.setSession(newTwoFactorOtp.getId());
            return new ResponseEntity<>(res,HttpStatus.ACCEPTED);
        }


        AuthResponse res = new AuthResponse();
        res.setJwt(jwt);
        res.setStatus(true);
        res.setMessage("Login Successfully");


        return  new ResponseEntity<>(res, HttpStatus.CREATED);

    }

    private Authentication authenticate(String username , String password){
        UserDetails userDetails = customeUserDetails.loadUserByUsername(username);
        if(userDetails==null){
            throw new BadCredentialsException("Invalid username");
        }
        if(!password.equals(userDetails.getPassword())){
            throw new BadCredentialsException("Invalid Password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails ,password,userDetails.getAuthorities());
    }


    @PostMapping("/two-factor/otp/{otp}")
public ResponseEntity<AuthResponse> verifySignInOtp(@PathVariable String otp , @RequestParam String id) throws Exception {


   TwoFactorOtp twoFactorOtp = twoFactorOtpService.findById(id);

   if(twoFactorOtpService.verifyTwoFactorOtp(twoFactorOtp,otp)){
       AuthResponse res = new AuthResponse();
       res.setMessage("Two factor authentication is verified");
       res.setTwoFactorAuthEnabled(true);
       res.setJwt(twoFactorOtp.getJwt());
       return new ResponseEntity<>(res,HttpStatus.OK);
   }
   throw new Exception("invalid otp");
}

}

package com.manish.controller;

import com.manish.model.Coin;
import com.manish.model.User;
import com.manish.model.Watchlist;
import com.manish.service.CoinService;
import com.manish.service.UserService;
import com.manish.service.WatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/watchlist")
public class WatchlistController {
    @Autowired
    private  WatchlistService watchlistService;

    @Autowired
    private  UserService userService;
    @Autowired
    private CoinService coinService;


    @GetMapping("/user")
    public ResponseEntity<Watchlist> getUserWatchlist(
            @RequestHeader("Authorization") String jwt
    ) {

        User user = userService.findUserProfileByJwt(jwt);
        Watchlist watchlist = watchlistService.findUserWatchlist(user.getId());
        return ResponseEntity.ok(watchlist);


    }

    @PostMapping("/{watchlistId}")
    public ResponseEntity<Watchlist> getWatchlistById(
            @PathVariable Long watchlistId
    ) {
        Watchlist watchlist = watchlistService.findById(watchlistId);
        return ResponseEntity.ok(watchlist);
    }

    @GetMapping("/add/coin/{coinId}")
    public ResponseEntity<Coin> getUserWatchlist(
            @RequestHeader("Authorization") String jwt,
            @PathVariable String coinId
    ) {

        User user = userService.findUserProfileByJwt(jwt);
        Coin coin = coinService.findById(coinId);
        Coin addCoin = watchlistService.addItemToWatchList(coin, user);

        return ResponseEntity.ok(addCoin);


    }

}

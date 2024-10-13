package com.manish.service;

import com.manish.model.Coin;
import com.manish.model.User;
import com.manish.model.Watchlist;

public interface WatchlistService {

    Watchlist findUserWatchlist(Long userId);
    Watchlist createWatchlist(User user);
    Watchlist findById(Long id);

    Coin addItemToWatchList(Coin coin , User user);





}

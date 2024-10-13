package com.manish.service;

import com.manish.model.Coin;
import com.manish.model.User;
import com.manish.model.Watchlist;
import com.manish.repository.CoinRepository;
import com.manish.repository.WatchlistRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WatchlistServiceImpl implements WatchlistService {

    @Autowired
    private WatchlistRepository watchlistRepository;

    @Autowired
    private CoinRepository coinRepository;


    @SneakyThrows
    @Override
    public Watchlist findUserWatchlist(Long userId) {
        Watchlist watchlist = watchlistRepository.findByUserId(userId);
        if(watchlist == null) {
            throw  new Exception("WatchList Not found");

        }
        return watchlist;
    }

    @Override
    public Watchlist createWatchlist(User user) {
       Watchlist watchlist = new Watchlist();
       watchlist.setUser(user);

        return watchlistRepository.save(watchlist);
    }

    @SneakyThrows
    @Override
    public Watchlist findById(Long id) {
        Optional<Watchlist> watchlist = watchlistRepository.findById(id);
        if (watchlist.isEmpty()){
            throw new Exception("WatchList not found");
        }
        return watchlist.get();
    }

    @Override
    public Coin addItemToWatchList(Coin coin, User user) {
       Watchlist watchlist = findUserWatchlist(user.getId());
       if(watchlist.getCoins().contains(coin)) {
           watchlist.getCoins().remove(coin);
       }
       else{
           watchlist.getCoins().add(coin);
       }
         watchlistRepository.save(watchlist);
       return coin;
    }
}

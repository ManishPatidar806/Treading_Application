package com.manish.service;

import com.manish.model.Asset;
import com.manish.model.Coin;
import com.manish.model.User;
import com.manish.repository.AssetRepository;
import jakarta.persistence.Id;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class AssetServiceImpl implements AssetService {

    @Autowired
private AssetRepository assetRepository;

    @Override
    public Asset createAsset(User user, Coin coin, double quantity) {
        Asset asset = new Asset();
        asset.setUser(user);
        asset.setCoin(coin);
        asset.setQuantity(quantity);
        asset.setBuyprice(coin.getCurrentPrice());

        return assetRepository.save(asset);
    }
    @SneakyThrows
    @Override
    public Asset getAssetById(Long assetId) {

        return assetRepository.findById(assetId).
                orElseThrow(()-> new Exception("Asset not found"));
    }

    @Override
    public Asset getAssetByUserIdAndAssetId(Long userId, Long assetId) {



        return null;
    }

    @Override
    public List<Asset> getUserAssets(Long userId) {
        return assetRepository.findByUserId(userId);
    }

    @Override
    public Asset updateAsset(Long assetId, double quantity) {
      Asset oldAsset = getAssetById(assetId);
       oldAsset.setQuantity(quantity+oldAsset.getQuantity());
        return assetRepository.save(oldAsset);
    }

    @Override
    public Asset findAssetByUserIdAndCoinId(Long userId, String coinId) {
        return assetRepository.findByUserIdAndCoinId(userId,coinId);
    }

    @Override
    public void deleteAsset(Long assetId) {
        assetRepository.deleteById(assetId);

    }
}

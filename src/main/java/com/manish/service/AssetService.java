package com.manish.service;

import com.manish.model.Asset;
import com.manish.model.Coin;
import com.manish.model.User;

import java.util.List;

public interface AssetService {

    Asset createAsset(User user , Coin coin, double quantity);
    Asset getAssetById(Long assetId);
    Asset getAssetByUserIdAndAssetId(Long userId, Long assetId);
    List<Asset> getUserAssets(Long userId);

    Asset updateAsset(Long assetId, double quantity);
    Asset findAssetByUserIdAndCoinId(Long userId, String coinId);
    void deleteAsset(Long assetId);





}

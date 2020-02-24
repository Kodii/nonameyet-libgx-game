package com.nonameyet.environment;

public enum AssetName {

    MAP_TMX("map/map.tmx");

    private String assetName;

    AssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getAssetName() {
        return assetName;
    }
}

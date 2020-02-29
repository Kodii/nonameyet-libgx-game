package com.nonameyet.environment;

public enum AssetName {

    MAP_TOWN_TMX("maps/town.tmx"),
    MAP_TOP_WORLD_TMX("maps/topworld.tmx"),
    PLAYER_PNG("sprites/characters/Paladin.png");

    private String assetName;

    AssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getAssetName() {
        return assetName;
    }
}

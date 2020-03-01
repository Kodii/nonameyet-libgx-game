package com.nonameyet.environment;

public enum AssetName {

    // main menu
    MAIN_MENU_BACKGROUND("menu/ws_Old_Paper_Texture_1920x1080.jpg"),
    OLD_NEWSPAPER_FONT("fonts/Old_Newspaper_Font.ttf"),

    MAP_TOWN_TMX("maps/town.tmx"),
    MAP_TOP_WORLD_TMX("maps/topworld.tmx"),
    PLAYER_PNG("sprites/characters/Rogue.png");

    private String assetName;

    AssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getAssetName() {
        return assetName;
    }
}

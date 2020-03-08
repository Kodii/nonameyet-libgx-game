package com.nonameyet.environment;

public enum AssetName {

    // main menu
    MAIN_MENU_BACKGROUND("menu/ws_Old_Paper_Texture_1920x1080.jpg"),
    OLD_NEWSPAPER_FONT("fonts/Old_Newspaper_Font.ttf"),

    MAP_TOWN_TMX("from_web/maps/town.tmx"),
    MAP_TOP_WORLD_TMX("from_web/maps/topworld.tmx"),
    MAP_SPAWN_TMX("maps/spawn/spawn.tmx"),
    PLAYER_PNG("from_web/sprites/characters/Rogue.png");

    private String assetName;

    AssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getAssetName() {
        return assetName;
    }
}

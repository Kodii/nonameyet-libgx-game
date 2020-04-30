package com.nonameyet.assets;

public enum AssetName {

    // main menu
    MAIN_MENU_BACKGROUND("menu/background.png"),
    PIXEL_FONT("fonts/DisposableDroidBB.ttf"),

    MAP_TOWN_TMX("from_web/maps/town.tmx"),
    MAP_TOP_WORLD_TMX("from_web/maps/topworld.tmx"),
    MAP_SPAWN_TMX("maps/spawn/spawn.tmx"),

    // sprites
    PLAYER_PNG("from_web/sprites/characters/Rogue.png"),
    PLAYER_ATLAS("sprites/npc/player.atlas"),
    CHEST_ATLAS("sprites/items/chests/chest_grass.atlas"),
    TORCH_ATLAS("sprites/items/torch/torch.atlas"),

    // hud
    CAMERA_FRAME("sprites/hud/camera_frame/camera_frame.png"),
    CHEST_WINDOW("sprites/hud/chest_window/chest_window.png"),
    LIFE("sprites/hud/life/life.png"),
    SLOT("sprites/hud/slot/slot.png"),
    SLOT_HIGHLIGHT("sprites/hud/slot/slot_highlight.png"),

    // sound effects
    CHEST_OPEN_EFFECT("sound/effects/chest/chest-open.ogg"),
    CHEST_CLOSE_EFFECT("sound/effects/chest/chest-close.ogg"),
    // music
    TOWN_NIGHT_MUSIC("sound/music/town-night.mp3"),
    TOWN_DAY_MUSIC("sound/music/town-day.mp3");


    private String assetName;

    AssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getAssetName() {
        return assetName;
    }
}

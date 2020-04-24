package com.nonameyet.maps;

import com.nonameyet.assets.AssetName;
import com.nonameyet.screens.GameScreen;

class SpawnMap extends Map {
    private final String TAG = this.getClass().getSimpleName();

    public SpawnMap(GameScreen screen) {
        super(screen, MapFactory.MapType.TOWN, AssetName.MAP_SPAWN_TMX);
    }

    @Override
    public void dispose() {

    }
}

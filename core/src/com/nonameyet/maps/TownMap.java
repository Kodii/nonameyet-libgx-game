package com.nonameyet.maps;

import com.nonameyet.assets.AssetName;
import com.nonameyet.screens.GameScreen;

class TownMap extends Map {
    private final String TAG = this.getClass().getSimpleName();

    public TownMap(GameScreen screen) {
        super(screen, MapFactory.MapType.TOWN, AssetName.MAP_TOWN_TMX);
    }

    @Override
    public void dispose() {

    }
}

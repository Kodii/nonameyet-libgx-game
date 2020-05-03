package com.nonameyet.maps;

import com.nonameyet.assets.AssetName;
import com.nonameyet.screens.GameScreen;

class FirstMap extends Map {
    private final String TAG = this.getClass().getSimpleName();

    public FirstMap(GameScreen screen) {
        super(screen, MapFactory.MapType.FIRST, AssetName.MAP_FIRST_TMX);
    }

    @Override
    public void dispose() {

    }
}

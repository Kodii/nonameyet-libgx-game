package com.nonameyet.maps;

import com.nonameyet.assets.AssetName;
import com.nonameyet.screens.GameScreen;

class TopWorldMap extends Map {
    private static final String TAG = TopWorldMap.class.getSimpleName();

    public TopWorldMap(GameScreen screen) {
        super(screen, MapFactory.MapType.TOP_WORLD, AssetName.MAP_TOP_WORLD_TMX);
    }

    @Override
    public void unloadMusic() {

    }

    @Override
    public void loadMusic() {

    }

    @Override
    public void dispose() {

    }
}

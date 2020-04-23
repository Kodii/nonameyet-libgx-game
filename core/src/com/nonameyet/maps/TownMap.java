package com.nonameyet.maps;

import com.nonameyet.assets.AssetName;
import com.nonameyet.screens.GameScreen;

public class TownMap extends Map {
    private static final String TAG = TownMap.class.getSimpleName();

    public TownMap(GameScreen screen) {
        super(screen, MapFactory.MapType.TOWN, AssetName.MAP_TOWN_TMX);
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

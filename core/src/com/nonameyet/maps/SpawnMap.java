package com.nonameyet.maps;

import com.nonameyet.environment.AssetName;
import com.nonameyet.screens.GameScreen;

public class SpawnMap extends Map {
    private static final String TAG = SpawnMap.class.getSimpleName();

    public SpawnMap(GameScreen screen) {
        super(screen, MapFactory.MapType.TOWN, AssetName.MAP_SPAWN_TMX);
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
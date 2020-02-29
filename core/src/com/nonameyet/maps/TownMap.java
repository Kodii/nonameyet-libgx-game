package com.nonameyet.maps;

import com.nonameyet.screens.GameScreen;

public class TownMap extends Map {

    public TownMap(GameScreen screen) {
        super(screen, MapFactory.MapType.TOWN);
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

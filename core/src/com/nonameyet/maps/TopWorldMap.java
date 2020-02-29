package com.nonameyet.maps;

import com.nonameyet.screens.GameScreen;

public class TopWorldMap extends Map {


    public TopWorldMap(GameScreen screen) {
        super(screen, MapFactory.MapType.TOP_WORLD);
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
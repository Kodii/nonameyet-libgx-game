package com.nonameyet.maps;

import com.nonameyet.assets.AssetName;

class SpawnMap extends Map {
    private final String TAG = this.getClass().getSimpleName();

    public SpawnMap() {
        super(MapFactory.MapType.SPAWN, AssetName.MAP_SPAWN_TMX);
    }

    @Override
    public void dispose() {

    }
}

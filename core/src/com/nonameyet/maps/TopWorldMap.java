package com.nonameyet.maps;

import com.nonameyet.assets.AssetName;

class TopWorldMap extends Map {
    private final String TAG = this.getClass().getSimpleName();

    public TopWorldMap() {
        super(MapFactory.MapType.TOP_WORLD, AssetName.MAP_TOP_WORLD_TMX);
    }

    @Override
    public void dispose() {

    }
}

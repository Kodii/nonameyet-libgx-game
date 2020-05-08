package com.nonameyet.maps;

import com.nonameyet.assets.AssetName;

class TownMap extends Map {
    private final String TAG = this.getClass().getSimpleName();

    public TownMap() {
        super(MapFactory.MapType.TOWN, AssetName.MAP_TOWN_TMX);
    }

    @Override
    public void dispose() {

    }
}

package com.nonameyet.maps;

import com.nonameyet.assets.AssetName;

class FirstMap extends Map {
    private final String TAG = this.getClass().getSimpleName();

    public FirstMap() {
        super(MapFactory.MapType.FIRST, AssetName.MAP_FIRST_TMX);
    }

    @Override
    public void dispose() {

    }
}

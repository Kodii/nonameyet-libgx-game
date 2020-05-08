package com.nonameyet.maps;

import com.badlogic.gdx.Gdx;

public class MapFactory {
    private static final String TAG = MapFactory.class.getSimpleName();

    public enum MapType {
        TOP_WORLD,
        TOWN,
        SPAWN,
        FIRST
    }

    static Map getMap(MapType mapType) {
        Map map = null;
        Gdx.app.debug(TAG, "Loading map: " + mapType + " !");
        switch (mapType) {
            case TOP_WORLD:
                map = new TopWorldMap();
                break;
            case TOWN:
                map = new TownMap();
                break;
            case SPAWN:
                map = new SpawnMap();
                break;
            case FIRST:
                map = new FirstMap();
                break;
            default:
                break;
        }
        return map;
    }

}

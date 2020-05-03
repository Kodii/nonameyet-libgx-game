package com.nonameyet.maps;

import com.badlogic.gdx.Gdx;
import com.nonameyet.screens.GameScreen;

public class MapFactory {
    private static final String TAG = MapFactory.class.getSimpleName();

    public enum MapType {
        TOP_WORLD,
        TOWN,
        SPAWN,
        FIRST
    }

    static Map getMap(GameScreen screen, MapType mapType) {
        Map map = null;
        Gdx.app.debug(TAG, "Loading map: " + mapType + " !");
        switch (mapType) {
            case TOP_WORLD:
                map = new TopWorldMap(screen);
                break;
            case TOWN:
                map = new TownMap(screen);
                break;
            case SPAWN:
                map = new SpawnMap(screen);
                break;
            case FIRST:
                map = new FirstMap(screen);
                break;
            default:
                break;
        }
        return map;
    }

}

package com.nonameyet.maps;

import com.badlogic.gdx.Gdx;
import com.nonameyet.screens.GameScreen;

import java.util.Hashtable;

public class MapFactory {
    private static final String TAG = MapFactory.class.getSimpleName();

    //All maps for the game
    private static Hashtable<MapType, Map> _maps = new Hashtable<>();

    public static enum MapType {
        TOP_WORLD,
        TOWN
    }

    static public Map getMap(GameScreen screen, MapType mapType) {
        Map map = null;
        Gdx.app.debug(TAG, "Loading map: " + mapType + " !");
        switch (mapType) {
            case TOP_WORLD:
//                map = _maps.get(MapType.TOP_WORLD);
//                if (map == null) {
                map = new TopWorldMap(screen);
//                    _maps.put(MapType.TOP_WORLD, map);
//                }
                break;
            case TOWN:
//                map = _maps.get(MapType.TOWN);
//                if (map == null) {
                map = new TownMap(screen);
//                    _maps.put(MapType.TOWN, map);
//                }
                break;
            default:
                break;
        }
        return map;
    }

    public static void clearCache() {
        for (Map map : _maps.values()) {
            map.dispose();
        }
        _maps.clear();
    }
}

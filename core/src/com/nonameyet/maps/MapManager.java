package com.nonameyet.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.nonameyet.screens.GameScreen;
import com.nonameyet.worldcontact.WorldContactListener;

public class MapManager {
    private static final String TAG = MapManager.class.getSimpleName();

    private GameScreen _screen;

    private boolean _mapChanged = false;
    private Map _currentMap;
    private MapFactory.MapType _currentMapType;

    public MapManager(GameScreen screen) {
        this._screen = screen;
    }

    public void loadMap(MapFactory.MapType mapType) {

        if (_screen.getWorld() != null)
            _screen.getWorld().dispose();
        _screen.setWorld(new World(new Vector2(0, 0), true));

        Map map = MapFactory.getMap(_screen, mapType);

        if (map == null) {
            Gdx.app.debug(TAG, "Map does not exist!  ");
            return;
        }

        _currentMapType = mapType;
        _currentMap = map;

        _screen.getWorld().setContactListener(new WorldContactListener(_screen));

        _mapChanged = false;
    }

    public MapFactory.MapType getCurrentMapType() {
        return _currentMapType;
    }

    public void setCurrentMapType(MapFactory.MapType currentMapType) {
        this._currentMapType = currentMapType;
    }

    public TiledMap getCurrentTiledMap() {
        if (_currentMap == null) {
            loadMap(MapFactory.MapType.SPAWN);
        }
        return _currentMap.getCurrentTiledMap();
    }

    public boolean isMapChanged() {
        return _mapChanged;
    }

    public void setMapChanged(boolean mapChanged) {
        this._mapChanged = mapChanged;
    }


}

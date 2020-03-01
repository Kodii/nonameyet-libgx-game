package com.nonameyet.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.nonameyet.screens.GameScreen;
import com.nonameyet.tools.WorldContactListener;

public class MapManager {
    private static final String TAG = MapManager.class.getSimpleName();

    private GameScreen _screen;

    private Camera _camera;
    private boolean _mapChanged = false;
    private Map _currentMap;

    public MapManager(GameScreen screen) {
        this._screen = screen;
    }

    public void loadMap(MapFactory.MapType mapType) {

        if (_screen.world != null)
            _screen.world.dispose();
        _screen.world = new World(new Vector2(0, 0), true);

        Map map = MapFactory.getMap(_screen, mapType);

        if (map == null) {
            Gdx.app.debug(TAG, "Map does not exist!  ");
            return;
        }

        _currentMap = map;

        _screen.world.setContactListener(new WorldContactListener(_screen));

        _mapChanged = true;

        Gdx.app.debug(TAG, "Player Start:  todo");
    }

    public TiledMap getCurrentTiledMap() {
        if (_currentMap == null) {
            loadMap(MapFactory.MapType.TOWN);
        }
        return _currentMap.getCurrentTiledMap();
    }

    public Camera getCamera() {
        return _camera;
    }

    public void setCamera(Camera _camera) {
        this._camera = _camera;
    }

    public boolean isMapChanged() {
        return _mapChanged;
    }

    public Map getCurrentMap() {
        return _currentMap;
    }

}

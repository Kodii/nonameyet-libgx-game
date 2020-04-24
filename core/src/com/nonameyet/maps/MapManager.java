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

    private boolean mapChanged = false;
    private Map currentMap;
    private MapFactory.MapType currentMapType;

    private WorldContactListener worldContactListener;

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

        currentMapType = mapType;
        currentMap = map;
        worldContactListener = new WorldContactListener(_screen);
        _screen.getWorld().setContactListener(worldContactListener);

        mapChanged = false;
    }

    public MapFactory.MapType getCurrentMapType() {
        return currentMapType;
    }

    public void setCurrentMapType(MapFactory.MapType currentMapType) {
        this.currentMapType = currentMapType;
    }

    public TiledMap getCurrentTiledMap() {
        if (currentMap == null) {
            loadMap(MapFactory.MapType.SPAWN);
        }
        return currentMap.getCurrentTiledMap();
    }

    public boolean isMapChanged() {
        return mapChanged;
    }

    public void setMapChanged(boolean mapChanged) {
        this.mapChanged = mapChanged;
    }

    public WorldContactListener getWorldContactListener() {
        return worldContactListener;
    }
}

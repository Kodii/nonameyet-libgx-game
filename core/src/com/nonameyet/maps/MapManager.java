package com.nonameyet.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.nonameyet.screens.GameScreen;
import com.nonameyet.sprites.Chest;
import com.nonameyet.worldcontact.WorldContactListener;

public class MapManager {
    private static final String TAG = MapManager.class.getSimpleName();
    private GameScreen screen;

    private boolean mapChanged = false;
    private Map currentMap;
    private MapFactory.MapType currentMapType;

    private WorldContactListener worldContactListener;

    private Chest chest;

    public MapManager(GameScreen screen) {
        this.screen = screen;
    }

    public void loadMap(MapFactory.MapType mapType) {

        if (screen.getWorld() != null)
            screen.getWorld().dispose();
        screen.setWorld(new World(new Vector2(0, 0), true));

        Map map = MapFactory.getMap(screen, mapType);

        if (map == null) {
            Gdx.app.debug(TAG, "Map does not exist!  ");
            return;
        }

        currentMapType = mapType;
        currentMap = map;
        worldContactListener = new WorldContactListener(screen);
        screen.getWorld().setContactListener(worldContactListener);

        createEntities();

        mapChanged = false;
    }

    public void createEntities() {
        chest = new Chest(screen);
    }

    public void updateEntities(float dt) {
        chest.update(dt);
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

    public MapLayer getChestSpawnLayer() {
        return currentMap.chestSpawnLayer;
    }

    public MapLayer getPlayerSpawnLayer() {
        return currentMap.playerSpawnLayer;
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

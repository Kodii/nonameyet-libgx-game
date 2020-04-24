package com.nonameyet.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.nonameyet.audio.AudioManager;
import com.nonameyet.screens.GameScreen;
import com.nonameyet.sprites.Chest;
import com.nonameyet.sprites.LightsEvent;
import com.nonameyet.sprites.Torch;
import com.nonameyet.worldcontact.WorldContactListener;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import static com.nonameyet.utils.Constants.PPM;

public class MapManager {
    private static final String TAG = MapManager.class.getSimpleName();
    private GameScreen screen;

    private boolean mapChanged = false;
    private Map currentMap;
    private MapFactory.MapType currentMapType;

    private WorldContactListener worldContactListener;

    private Chest chest;
    private final Array<Torch> torches = new Array<>(20);

    // events
    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);

    public MapManager(GameScreen screen) {
        this.screen = screen;

        addPropertyChangeListener(AudioManager.getInstance());
    }

    public void loadMap(MapFactory.MapType mapType) {

        if (screen.getWorld() != null)
            screen.getWorld().dispose();
        screen.setWorld(new World(new Vector2(0, 0), true));

        Map map = MapFactory.getMap(screen, mapType);

        if (map == null) {
            Gdx.app.debug(TAG, "Map does not exist!");
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

        for (MapObject object : getTorchesSpawnLayer().getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            torches.add(new Torch(screen, new Vector2(rect.getX() / PPM, rect.getY() / PPM)));
        }

        changes.firePropertyChange(LightsEvent.class.getSimpleName(), null, LightsEvent.OFF);
    }

    public void updateEntities(float dt) {
        input();

        chest.update(dt);

        for (Torch torch : torches) {
            torch.update(dt);
        }
    }

    public void drawEntities(Batch batch) {
        chest.draw(batch);

        for (Torch torch : torches) {
            torch.draw(batch);
        }
    }

    public void input() {
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_3)) {
            changes.firePropertyChange(LightsEvent.class.getSimpleName(), null, LightsEvent.ON);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.NUM_4)) {
            changes.firePropertyChange(LightsEvent.class.getSimpleName(), null, LightsEvent.OFF);
        }
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

    public MapLayer getTorchesSpawnLayer() {
        return currentMap.torchesSpawnLayer;
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

    public void addPropertyChangeListener(
            PropertyChangeListener p) {
        changes.addPropertyChangeListener(p);
    }

    public void removePropertyChangeListener(
            PropertyChangeListener p) {
        changes.removePropertyChangeListener(p);
    }

}

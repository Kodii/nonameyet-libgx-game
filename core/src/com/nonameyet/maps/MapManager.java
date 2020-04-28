package com.nonameyet.maps;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.nonameyet.b2d.B2dContactListener;
import com.nonameyet.screens.GameScreen;
import com.nonameyet.sprites.Chest;
import com.nonameyet.sprites.Player;
import com.nonameyet.sprites.Torch;
import com.nonameyet.ui.clock.DayTimeEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import static com.nonameyet.utils.Constants.PPM;

public class MapManager implements Disposable, PropertyChangeListener {
    private final String TAG = this.getClass().getSimpleName();
    private final GameScreen screen;

    private Map currentMap;
    private MapFactory.MapType currentMapType;
    private boolean mapChanged = false;

    private B2dContactListener b2dContactListener;

    private Player player;
    private Chest chest;
    private final Array<Torch> torches = new Array<>(20);

    // events
    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);

    public MapManager(GameScreen screen) {
        this.screen = screen;

        screen.getPlayerHUD().getClockUI().addPropertyChangeListener(this);
    }

    public void loadMap(MapFactory.MapType mapType) {

        if (screen.getWorld() != null)
            screen.getWorld().dispose();
        screen.setWorld(new World(new Vector2(0, 0), true));

        screen.setRayHandler(new RayHandler(screen.getWorld()));
        screen.getRayHandler().setAmbientLight(1.0f);
        RayHandler.useDiffuseLight(true);

        Map map = MapFactory.getMap(screen, mapType);

        if (map == null) {
            Gdx.app.debug(TAG, "Map does not exist!");
            return;
        }

        currentMapType = mapType;
        currentMap = map;

        b2dContactListener = new B2dContactListener(screen);
        screen.getWorld().setContactListener(b2dContactListener);

        // ecs player
        Rectangle rect = getPlayerSpawnLayer().getObjects().getByType(RectangleMapObject.class).get(0).getRectangle();
        screen.getEcsEngine().createPlayer(new Vector2(rect.getX(), rect.getY()));


        createPlayer();
        createEntities();

        mapChanged = false;
    }

    private void createPlayer() {
        player = new Player(screen);
    }

    public void createEntities() {
        chest = new Chest(screen);

        for (MapObject object : getTorchesSpawnLayer().getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            torches.add(new Torch(screen, new Vector2(rect.getX() / PPM, rect.getY() / PPM)));
        }
    }

    public void updateEntities(float dt) {
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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Gdx.app.debug(TAG, "MapManager --> propertyChange(): " + evt.getPropertyName() + ", getNewValue(): " + evt.getNewValue());

        if (evt.getPropertyName().equals(DayTimeEvent.class.getSimpleName()))
            changeDayState((DayTimeEvent) evt.getNewValue());

    }

    private void changeDayState(DayTimeEvent event) {

        switch (event) {

            case DAWN:
                screen.getRayHandler().setAmbientLight(1, 0.92f, 0.61f, 1f);
                break;
            case AFTERNOON:
                screen.getRayHandler().setAmbientLight(1f, 1f, 1f, 1f);
                break;
            case DUSK:
                screen.getRayHandler().setAmbientLight(0.7f, 0.5f, 0.7f, 1f);
                break;
            case NIGHT:
                screen.getRayHandler().setAmbientLight(0.3f, 0.3f, 0.7f, 1f);
                break;
        }
    }

    public B2dContactListener getB2dContactListener() {
        return b2dContactListener;
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

    public Player getPlayer() {
        return player;
    }

    @Override
    public void dispose() {
        screen.getPlayerHUD().getClockUI().removePropertyChangeListener(this);
    }
}

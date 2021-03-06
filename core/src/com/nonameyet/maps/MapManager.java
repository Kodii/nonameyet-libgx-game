package com.nonameyet.maps;

import box2dLight.Light;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.nonameyet.b2d.CollisionSystem;
import com.nonameyet.events.DayTimeEvent;
import com.nonameyet.screens.GameScreen;
import com.nonameyet.utils.Collision;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static com.nonameyet.utils.Constants.PPM;

public class MapManager implements Disposable, PropertyChangeListener {
    private final String TAG = this.getClass().getSimpleName();
    private final GameScreen screen;

    private Map currentMap;
    private MapFactory.MapType currentMapType;
    private boolean mapChanged = false;

    private CollisionSystem collisionSystem;

    public MapManager(GameScreen screen) {
        this.screen = screen;

        screen.getPlayerHUD().getClockUI().addPropertyChangeListener(this);
    }

    public void loadMap(MapFactory.MapType mapType) {
        screen.getEcsEngine().removeAllEntities();
        screen.removeSystems();

        if (screen.getWorld() != null)
            screen.getWorld().dispose();

        screen.setWorld(new World(new Vector2(0, 0), true));

        screen.setRayHandler(new RayHandler(screen.getWorld(), 256, 144));
        screen.getRayHandler().getLightMapTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        screen.getRayHandler().setAmbientLight(1.0f);
        Light.setGlobalContactFilter(Collision.lightsFilter());
        RayHandler.useDiffuseLight(true);

        Map map = MapFactory.getMap(mapType);

        if (map == null) {
            Gdx.app.debug(TAG, "Map does not exist!");
            return;
        }

        currentMapType = mapType;
        currentMap = map;

        collisionSystem = new CollisionSystem(screen);
        screen.getWorld().setContactListener(collisionSystem);

        mapChanged = false;
    }

    public void createEntites() {
        createScenery();

        createPortals();

        createPlayer();

        createChest();

        createTorches();

        createNpcs();

        createBlacksmithArea();

        screen.getPlayerHUD().getClockUI().manualPropertyChangeEvent();

    }

    private void createScenery() {
        // ecs scenery
        Array.ArrayIterator<RectangleMapObject> rectangleMapObjects = new Array.ArrayIterator<>(currentMap.collisionLayer.getObjects().getByType(RectangleMapObject.class));
        while (rectangleMapObjects.hasNext()) {
            RectangleMapObject object = rectangleMapObjects.next();
            Rectangle rect = object.getRectangle();
            screen.getEcsEngine().createScenery(rect);
        }
    }

    private void createPortals() {
        // ecs portal
        Array.ArrayIterator<RectangleMapObject> rectangleMapObjects = new Array.ArrayIterator<>(currentMap.portalLayer.getObjects().getByType(RectangleMapObject.class));
        while (rectangleMapObjects.hasNext()) {
            RectangleMapObject object = rectangleMapObjects.next();
            Rectangle rect = object.getRectangle();
            screen.getEcsEngine().createPortal(rect);
        }
    }

    private void createPlayer() {
        // ecs player
        if (currentMap.playerSpawnLayer != null) {
            Rectangle playerPositionPoint = currentMap.playerSpawnLayer.getObjects().getByType(RectangleMapObject.class).get(0).getRectangle();
            screen.getEcsEngine().createPlayer(new Vector2(playerPositionPoint.getX() / PPM, playerPositionPoint.getY() / PPM));
        }
    }

    private void createChest() {
        // ecs chest
        if (currentMap.chestSpawnLayer != null) {
            Rectangle chestPositionPoint = currentMap.chestSpawnLayer.getObjects().getByType(RectangleMapObject.class).get(0).getRectangle();
            screen.getEcsEngine().createChest(new Vector2(chestPositionPoint.getX() / PPM, chestPositionPoint.getY() / PPM));
        }
    }

    private void createTorches() {
        // ecs torches
        if (currentMap.torchesSpawnLayer != null) {

            Array.ArrayIterator<RectangleMapObject> rectangleMapObjects = new Array.ArrayIterator<>(currentMap.torchesSpawnLayer.getObjects().getByType(RectangleMapObject.class));
            while (rectangleMapObjects.hasNext()) {
                RectangleMapObject object = rectangleMapObjects.next();
                Rectangle rect = object.getRectangle();
                screen.getEcsEngine().createTorch(new Vector2(rect.getX() / PPM, rect.getY() / PPM));
            }
        }
    }

    private void createNpcs() {
        // ecs npcs
        if (currentMap.npcSpawnLayer != null) {

            Array.ArrayIterator<RectangleMapObject> rectangleMapObjects = new Array.ArrayIterator<>(currentMap.npcSpawnLayer.getObjects().getByType(RectangleMapObject.class));
            while (rectangleMapObjects.hasNext()) {
                RectangleMapObject object = rectangleMapObjects.next();
                Rectangle rect = object.getRectangle();
                Vector2 position = new Vector2(rect.getX() / PPM, rect.getY() / PPM);

                switch (object.getName()) {
                    case "elder":
                        screen.getEcsEngine().createElder(position);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void createBlacksmithArea() {
        // ecs blacksmith
        if (currentMap.blacksmithSpawnLayer != null) {

            Array.ArrayIterator<RectangleMapObject> rectangleMapObjects = new Array.ArrayIterator<>(currentMap.blacksmithSpawnLayer.getObjects().getByType(RectangleMapObject.class));
            while (rectangleMapObjects.hasNext()) {
                RectangleMapObject object = rectangleMapObjects.next();
                Rectangle rect = object.getRectangle();
                Vector2 position = new Vector2(rect.getX() / PPM, rect.getY() / PPM);

                switch (object.getName()) {
                    case "blacksmith":
                        screen.getEcsEngine().createBlacksmith(position);
                        break;
                    case "anvil":
                        screen.getEcsEngine().createAnvil(position);
                        break;
                    case "owen":
                        screen.getEcsEngine().createOwen(position);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Gdx.app.debug(TAG, "MapManager --> propertyChange(): " + evt.getPropertyName() + ", getNewValue(): " + evt.getNewValue());

        if (evt.getPropertyName().equals(DayTimeEvent.NAME))
            changeDayState((DayTimeEvent) evt.getNewValue());

    }

    private void changeDayState(DayTimeEvent event) {

        switch (event) {

            case DAWN:
                screen.getRayHandler().setAmbientLight(1, 0.92f, 0.65f, 1f);
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

    public CollisionSystem getCollisionSystem() {
        return collisionSystem;
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

    public MapLayer getPlayerLayer() {
        return currentMap.playerSpawnLayer;
    }

    @Override
    public void dispose() {
        screen.getPlayerHUD().getClockUI().removePropertyChangeListener(this);
    }
}

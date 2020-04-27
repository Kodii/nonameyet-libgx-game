package com.nonameyet.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapGroupLayer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.nonameyet.assets.AssetName;
import com.nonameyet.assets.Assets;
import com.nonameyet.b2d.BodyBuilder;
import com.nonameyet.screens.GameScreen;
import com.nonameyet.utils.Collision;

import static com.nonameyet.utils.Constants.PPM;

abstract class Map implements Disposable {
    private final String TAG = this.getClass().getSimpleName();

    //Map layers
    protected final static String COLLISION_LAYER = "MAP_COLLISION_LAYER";
    protected final static String PORTAL_LAYER = "MAP_PORTAL_LAYER";
    protected final static String PLAYER_SPAWN_LAYER = "PLAYER_SPAWN_LAYER";
    // Chests layers
    protected final static String CHEST_SPAWN_LAYER = "CHEST_SPAWN_LAYER";
    protected final static String TORCHES_SPAWN_LAYER = "TORCHES_SPAWN_LAYER";

    protected MapLayer collisionLayer = null;
    protected MapLayer portalLayer = null;
    protected MapLayer playerSpawnLayer = null;

    protected MapLayer chestSpawnLayer = null;
    protected MapLayer torchesSpawnLayer = null;

    protected TiledMap currentTiledMap = null;
    protected MapFactory.MapType currentMapType;

    private final World world;

    public Map(GameScreen screen, MapFactory.MapType mapType, AssetName mapTmx) {
        Gdx.app.debug(TAG, "Create a new map: " + mapType);

        currentTiledMap = Assets.manager.get(mapTmx.getAssetName());
        currentMapType = mapType;
        world = screen.getWorld();

        collisionLayer = currentTiledMap.getLayers().get(COLLISION_LAYER);
        if (collisionLayer == null) {
            Gdx.app.debug(TAG, "No collision layer!");
        }
        portalLayer = currentTiledMap.getLayers().get(PORTAL_LAYER);
        if (portalLayer == null) {
            Gdx.app.debug(TAG, "No portal layer!");
        }

        playerSpawnLayer = currentTiledMap.getLayers().get(PLAYER_SPAWN_LAYER);
        if (playerSpawnLayer == null) {
            Gdx.app.debug(TAG, "No player spawn layer!");
        }

        create();

        MapGroupLayer chestGroup = (MapGroupLayer) currentTiledMap.getLayers().get("chest");
        if (chestGroup != null) {

            chestSpawnLayer = chestGroup.getLayers().get(CHEST_SPAWN_LAYER);
            if (chestSpawnLayer == null) {
                Gdx.app.debug(TAG, "No chest spawn layer!");
            }
        } else {
            Gdx.app.debug(TAG, "No chest group, skip!");
        }

        MapGroupLayer lightsGroup = (MapGroupLayer) currentTiledMap.getLayers().get("lights");
        if (lightsGroup != null) {

            torchesSpawnLayer = lightsGroup.getLayers().get(TORCHES_SPAWN_LAYER);
            if (torchesSpawnLayer == null) {
                Gdx.app.debug(TAG, "No torches spawn layer!");
            }
        } else {
            Gdx.app.debug(TAG, "No lights group, skip!");
        }
    }

    private void create() {
        createBodiesForCollision();
        createBodiesForPortal();
    }

    private void createBodiesForPortal() {
        for (MapObject object : portalLayer.getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            BodyBuilder.staticRectangleBody(
                    world,
                    new Vector2((rect.getX() + rect.getWidth() / 2) / PPM, (rect.getY() + rect.getHeight() / 2) / PPM),
                    new Vector2(rect.getWidth(), rect.getHeight()),
                    "PORTAL",
                    Collision.OBSTACLE);
        }
    }

    private void createBodiesForCollision() {
        for (MapObject object : collisionLayer.getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            BodyBuilder.staticRectangleBody(
                    world,
                    new Vector2((rect.getX() + rect.getWidth() / 2) / PPM, (rect.getY() + rect.getHeight() / 2) / PPM),
                    new Vector2(rect.getWidth(), rect.getHeight()),
                    "COLLISION",
                    Collision.OBSTACLE);
        }
    }

    TiledMap getCurrentTiledMap() {
        return currentTiledMap;
    }
}

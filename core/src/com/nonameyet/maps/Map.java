package com.nonameyet.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapGroupLayer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.nonameyet.assets.AssetName;
import com.nonameyet.assets.Assets;
import com.nonameyet.screens.GameScreen;

import static com.nonameyet.utils.Constants.PPM;

public abstract class Map implements Disposable {
    private static final String TAG = Map.class.getSimpleName();

    private World world;

    protected TiledMap currentTiledMap = null;

    //Map layers
    protected final static String COLLISION_LAYER = "MAP_COLLISION_LAYER";
    protected final static String PORTAL_LAYER = "MAP_PORTAL_LAYER";
    protected final static String PLAYER_SPAWN_LAYER = "PLAYER_SPAWN_LAYER";

    // Chests layers
    //    protected final static String CHEST_CLOSE_LAYER = "CHEST_CLOSE_LAYER";
    protected final static String CHEST_COLLISION_LAYER = "CHEST_COLLISION_LAYER";

    protected MapLayer collisionLayer = null;
    protected MapLayer portalLayer = null;
    protected MapLayer playerSpawnLayer = null;

    //    protected MapLayer chestCloseLayer = null;
    protected MapLayer chestCollisionLayer = null;

    protected MapFactory.MapType currentMapType;

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
//        chestCloseLayer = (TiledMapTileLayer) currentTiledMap.getLayers().get(CHEST_CLOSE_LAYER);
//        if (chestCloseLayer == null) {
//            Gdx.app.debug(TAG, "No chest close layer!");
//        }

        chestCollisionLayer = ((MapGroupLayer) currentTiledMap.getLayers().get("chest")).getLayers().get(CHEST_COLLISION_LAYER);
        if (chestCollisionLayer == null) {
            Gdx.app.debug(TAG, "No chest collision layer!");
        }

        create();
        createChest();
    }

    private void create() {

        //create body and fixture variables
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        Body body;

//        create bodies/fixtures for MAP_COLLISION_LAYER
        for (MapObject object : collisionLayer.getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / PPM, (rect.getY() + rect.getHeight() / 2) / PPM);

            body = world.createBody(bdef);
            body.setUserData("COLLISION");

            PolygonShape shape = new PolygonShape();
            shape.setAsBox(rect.getWidth() / 2 / PPM, rect.getHeight() / 2 / PPM);
            fdef.shape = shape;
            body.createFixture(fdef);

            shape.dispose();

        }

        //create bodies/fixtures for MAP_PORTAL_LAYER
        for (MapObject object : portalLayer.getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / PPM, (rect.getY() + rect.getHeight() / 2) / PPM);

            body = world.createBody(bdef);
            body.setUserData("PORTAL");

            fdef.isSensor = true;

            PolygonShape shape = new PolygonShape();
            shape.setAsBox(rect.getWidth() / 2 / PPM, rect.getHeight() / 2 / PPM);
            fdef.shape = shape;
            body.createFixture(fdef);

            shape.dispose();
        }
    }

    private void createChest() {

        //create body and fixture variables
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //        create bodies/fixtures for CHEST_COLLISION_LAYER
        for (MapObject object : chestCollisionLayer.getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / PPM, (rect.getY() + rect.getHeight() / 2) / PPM);

            body = world.createBody(bdef);
            body.setUserData("CHEST_COLLISION");

            PolygonShape shape = new PolygonShape();
            shape.setAsBox(rect.getWidth() / 2 / PPM, rect.getHeight() / 2 / PPM);
            fdef.shape = shape;
            body.createFixture(fdef);

            shape.dispose();
        }
    }

    abstract public void unloadMusic();

    abstract public void loadMusic();

    TiledMap getCurrentTiledMap() {
        return currentTiledMap;
    }
}

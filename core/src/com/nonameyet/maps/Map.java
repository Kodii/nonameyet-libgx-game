package com.nonameyet.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.nonameyet.environment.AssetName;
import com.nonameyet.screens.GameScreen;

import static com.nonameyet.utils.Constants.PPM;

public abstract class Map implements Disposable {
    private static final String TAG = Map.class.getSimpleName();


    private World _world;

    //Map layers
    protected final static String COLLISION_LAYER = "MAP_COLLISION_LAYER";
    protected final static String PORTAL_LAYER = "MAP_PORTAL_LAYER";

    protected TiledMap _currentTiledMap = null;

    protected MapLayer _collisionLayer = null;
    protected MapLayer _portalLayer = null;

    protected MapFactory.MapType _currentMapType;


    public Map(GameScreen screen, MapFactory.MapType mapType, AssetName mapTmx) {
        Gdx.app.debug(TAG, "Create a new map: " + mapType);

        _currentTiledMap = screen.game.getAssets().manager.get(mapTmx.getAssetName());
        _currentMapType = mapType;
        _world = screen.getWorld();

        _collisionLayer = _currentTiledMap.getLayers().get(COLLISION_LAYER);
        if (_collisionLayer == null) {
            Gdx.app.debug(TAG, "No collision layer!");
        }
        _portalLayer = _currentTiledMap.getLayers().get(PORTAL_LAYER);
        if (_portalLayer == null) {
            Gdx.app.debug(TAG, "No portal layer!");
        }

        create();
    }


    private void create() {

        //create body and fixture variables
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        Body body;

//        create bodies/fixtures for MAP_COLLISION_LAYER
        for (MapObject object : _collisionLayer.getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / PPM, (rect.getY() + rect.getHeight() / 2) / PPM);

            body = _world.createBody(bdef);
            body.setUserData("COLLISION");

            PolygonShape shape = new PolygonShape();
            shape.setAsBox(rect.getWidth() / 2 / PPM, rect.getHeight() / 2 / PPM);
            fdef.shape = shape;
            body.createFixture(fdef);

            shape.dispose();

        }

        //create bodies/fixtures for MAP_PORTAL_LAYER
        for (MapObject object : _portalLayer.getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / PPM, (rect.getY() + rect.getHeight() / 2) / PPM);

            body = _world.createBody(bdef);
            body.setUserData("PORTAL");

            fdef.isSensor = true;

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
        return _currentTiledMap;
    }
}

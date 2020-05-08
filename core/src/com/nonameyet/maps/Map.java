package com.nonameyet.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapGroupLayer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Disposable;
import com.nonameyet.assets.AssetName;
import com.nonameyet.assets.Assets;

abstract class Map implements Disposable {
    private final String TAG = this.getClass().getSimpleName();

    //Map layers
    protected final static String COLLISION_LAYER = "MAP_COLLISION_LAYER";
    protected final static String PORTAL_LAYER = "MAP_PORTAL_LAYER";
    protected final static String PLAYER_SPAWN_LAYER = "PLAYER_SPAWN_LAYER";
    // Chests layers
    protected final static String CHEST_SPAWN_LAYER = "CHEST_SPAWN_LAYER";

    // Lights layers
    protected final static String TORCHES_SPAWN_LAYER = "TORCHES_SPAWN_LAYER";

    // Npc layers
    protected final static String NPC_SPAWN_LAYER = "NPC_SPAWN_LAYER";
    protected final static String BLACKSMITH_SPAWN_LAYER = "BLACKSMITH_SPAWN_LAYER";

    protected MapLayer collisionLayer = null;
    protected MapLayer portalLayer = null;
    protected MapLayer playerSpawnLayer = null;

    protected MapLayer chestSpawnLayer = null;
    protected MapLayer torchesSpawnLayer = null;
    protected MapLayer npcSpawnLayer = null;
    protected MapLayer blacksmithSpawnLayer = null;

    protected TiledMap currentTiledMap = null;
    protected MapFactory.MapType currentMapType;

    public Map(MapFactory.MapType mapType, AssetName mapTmx) {
        Gdx.app.debug(TAG, "Create a new map: " + mapType);

        currentTiledMap = Assets.manager.get(mapTmx.getAssetName());
        currentMapType = mapType;

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

        MapGroupLayer npcGroup = (MapGroupLayer) currentTiledMap.getLayers().get("npc");
        if (npcGroup != null) {
            npcSpawnLayer = npcGroup.getLayers().get(NPC_SPAWN_LAYER);
            if (npcSpawnLayer == null) {
                Gdx.app.debug(TAG, "No npc spawn layer!");
            }

        } else {
            Gdx.app.debug(TAG, "No npc group, skip!");
        }

        MapGroupLayer blacksmithGroup = (MapGroupLayer) currentTiledMap.getLayers().get("blacksmith");
        if (blacksmithGroup != null) {
            blacksmithSpawnLayer = blacksmithGroup.getLayers().get(BLACKSMITH_SPAWN_LAYER);
            if (blacksmithSpawnLayer == null) {
                Gdx.app.debug(TAG, "No blacksmith spawn layer!");
            }

        } else {
            Gdx.app.debug(TAG, "No blacksmith group, skip!");
        }
    }

    TiledMap getCurrentTiledMap() {
        return currentTiledMap;
    }
}

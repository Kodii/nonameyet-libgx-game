package com.nonameyet.environment;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable {

    public final AssetManager manager;

    public Assets() {
        manager = new AssetManager();
        manager.setLoader(TiledMap.class, new TmxMapLoader(manager.getFileHandleResolver()));
    }

    public void load() {
        manager.load(AssetName.MAP_TOWN_TMX.getAssetName(), TiledMap.class);
        manager.load(AssetName.MAP_TOP_WORLD_TMX.getAssetName(), TiledMap.class);
        manager.load(AssetName.PLAYER_PNG.getAssetName(), Texture.class);
    }

    @Override
    public void dispose() {
        manager.dispose();
    }
}

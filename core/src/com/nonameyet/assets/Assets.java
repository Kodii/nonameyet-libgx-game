package com.nonameyet.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable {

    public static AssetManager manager;

    static {
        manager = new AssetManager();
        manager.setLoader(TiledMap.class, new TmxMapLoader(manager.getFileHandleResolver()));
    }

    public static void load() {
        manager.load(AssetName.MAIN_MENU_BACKGROUND.getAssetName(), Texture.class);

        manager.load(AssetName.MAP_TOWN_TMX.getAssetName(), TiledMap.class);
        manager.load(AssetName.MAP_TOP_WORLD_TMX.getAssetName(), TiledMap.class);
        manager.load(AssetName.MAP_SPAWN_TMX.getAssetName(), TiledMap.class);

        // sprites
        manager.load(AssetName.PLAYER_PNG.getAssetName(), Texture.class);
        manager.load(AssetName.CHEST_ATLAS.getAssetName(), TextureAtlas.class);

        // hud
        manager.load(AssetName.CAMERA_FRAME.getAssetName(), Texture.class);
        manager.load(AssetName.CHEST_WINDOW.getAssetName(), Texture.class);
        manager.load(AssetName.LIFE.getAssetName(), Texture.class);

        // sound effects
        manager.load(AssetName.CHEST_OPEN_EFFECT.getAssetName(), Sound.class);
        manager.load(AssetName.CHEST_CLOSE_EFFECT.getAssetName(), Sound.class);
    }

    @Override
    public void dispose() {
        manager.dispose();
    }
}

package com.nonameyet.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Disposable;
import com.nonameyet.ecs.components.ParticleEffectComponent;

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
        manager.load(AssetName.MAP_FIRST_TMX.getAssetName(), TiledMap.class);

        // sprites
        manager.load(AssetName.PLAYER_PNG.getAssetName(), Texture.class);

        manager.load(AssetName.PLAYER_WITHOUT_ARM_ATLAS.getAssetName(), TextureAtlas.class);
        manager.load(AssetName.ARM_ATLAS.getAssetName(), TextureAtlas.class);

        manager.load(AssetName.PLAYER_ATLAS.getAssetName(), TextureAtlas.class);
        manager.load(AssetName.PLAYER2_ATLAS.getAssetName(), TextureAtlas.class);
        manager.load(AssetName.ELDER_ATLAS.getAssetName(), TextureAtlas.class);
        manager.load(AssetName.CHEST_ATLAS.getAssetName(), TextureAtlas.class);
        manager.load(AssetName.TORCH_ATLAS.getAssetName(), TextureAtlas.class);

        //blacksmith
        manager.load(AssetName.BLACKSMITH_ATLAS.getAssetName(), TextureAtlas.class);
        manager.load(AssetName.OWEN_ATLAS.getAssetName(), TextureAtlas.class);
        manager.load(AssetName.ANVIL_ATLAS.getAssetName(), TextureAtlas.class);

        manager.load(AssetName.NPC_BUBBLE.getAssetName(), TextureAtlas.class);

        // hud
        manager.load(AssetName.CAMERA_FRAME.getAssetName(), Texture.class);
        manager.load(AssetName.CHEST_WINDOW.getAssetName(), TextureAtlas.class);
        manager.load(AssetName.STATS_WINDOW.getAssetName(), TextureAtlas.class);
        manager.load(AssetName.LIFE.getAssetName(), Texture.class);

        // sound effects
        manager.load(AssetName.CHEST_OPEN_EFFECT.getAssetName(), Sound.class);
        manager.load(AssetName.CHEST_CLOSE_EFFECT.getAssetName(), Sound.class);
        // music
        manager.load(AssetName.TOWN_NIGHT_MUSIC.getAssetName(), Music.class);
        manager.load(AssetName.TOWN_DAY_MUSIC.getAssetName(), Music.class);

        // particle effects
        manager.load(AssetName.PARTICLE_SPRITE_ATLAS.getAssetName(), TextureAtlas.class);
        final ParticleEffectLoader.ParticleEffectParameter peParameter = new ParticleEffectLoader.ParticleEffectParameter();
        peParameter.atlasFile = AssetName.PARTICLE_SPRITE_ATLAS.getAssetName();
        for (ParticleEffectComponent.ParticleEffectType type : ParticleEffectComponent.ParticleEffectType.values()) {
            manager.load(type.getEffectFilePath(), ParticleEffect.class, peParameter);
        }
        manager.load(AssetName.PARTICLE_FOG.getAssetName(), ParticleEffect.class, peParameter);
    }

    @Override
    public void dispose() {
        manager.dispose();
    }
}

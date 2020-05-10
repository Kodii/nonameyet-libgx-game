package com.nonameyet.ecs.entities.items;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.nonameyet.assets.AssetName;
import com.nonameyet.assets.Assets;
import com.nonameyet.b2d.BodyBuilder;
import com.nonameyet.ecs.ECSEngine;
import com.nonameyet.ecs.components.TypeComponent;
import com.nonameyet.utils.Collision;


public class Apple extends ItemEntity {

    public Apple(ECSEngine ecsEngine, Vector2 spawnLocation) {
        super(ecsEngine, spawnLocation);
    }

    @Override
    void transformComponent() {
        transformCmp.position.set(spawnLocation.x, spawnLocation.y, 1);
        transformCmp.scale.set(0.5f, 0.5f);
        transformCmp.rotation = 0;
    }

    @Override
    void textureComponent() {
        TextureAtlas textureAtlas = Assets.manager.get(AssetName.APPLE_ATLAS.getAssetName());
        TextureRegion textureRegion = textureAtlas.findRegion("apple");
        textureCmp.region = new TextureRegion(textureRegion, 0, 0, textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
    }

    @Override
    void b2dBodyComponent() {
        b2dBodyCmp.body = BodyBuilder.createItemBody(
                ecsEngine.getScreen().getWorld(),
                new Vector2(spawnLocation.x, spawnLocation.y),
                new Vector2(textureCmp.region.getRegionWidth(), textureCmp.region.getRegionHeight()),
                this,
                Collision.ITEM);
        b2dBodyCmp.body.setTransform(b2dBodyCmp.body.getPosition(), (float) Math.toRadians(transformCmp.rotation));
        b2dBodyCmp.body.setFixedRotation(true);
    }

    @Override
    void typeComponent() {
        typeCmp.type = TypeComponent.ITEM;
    }

    @Override
    protected void resetTransform() {
        transformCmp.rotation = 0;
    }
}


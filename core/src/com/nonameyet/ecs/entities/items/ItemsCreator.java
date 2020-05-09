package com.nonameyet.ecs.entities.items;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.nonameyet.assets.AssetName;
import com.nonameyet.assets.Assets;
import com.nonameyet.b2d.BodyBuilder;
import com.nonameyet.ecs.ECSEngine;
import com.nonameyet.ecs.components.B2dBodyComponent;
import com.nonameyet.ecs.components.TextureComponent;
import com.nonameyet.ecs.components.TransformComponent;
import com.nonameyet.ecs.components.TypeComponent;
import com.nonameyet.utils.Collision;

public class ItemsCreator {

    public static void createSword(final ECSEngine ecsEngine, final Vector2 spawnLocation) {
        final Entity entity = new Entity();

        final TransformComponent transformCmp = ecsEngine.createComponent(TransformComponent.class);
        final TextureComponent textureCmp = ecsEngine.createComponent(TextureComponent.class);
        B2dBodyComponent b2dBodyCmp = ecsEngine.createComponent(B2dBodyComponent.class);
        final TypeComponent typeCmp = ecsEngine.createComponent(TypeComponent.class);

        transformCmp.position.set(spawnLocation.x, spawnLocation.y, 1);
        transformCmp.scale.set(0.5f, 0.5f);
        transformCmp.rotation = -90;
        TextureAtlas textureAtlas = Assets.manager.get(AssetName.SWORD_ATLAS.getAssetName());
        TextureRegion textureRegion = textureAtlas.findRegion("first_sword");
        textureCmp.region = new TextureRegion(textureRegion, 0, 0, textureRegion.getRegionWidth(), textureRegion.getRegionHeight());

        b2dBodyCmp.body = BodyBuilder.createWeaponBody(
                ecsEngine.getScreen().getWorld(),
                new Vector2(spawnLocation.x, spawnLocation.y),
                new Vector2(textureCmp.region.getRegionWidth(), textureCmp.region.getRegionHeight()),
                entity,
                Collision.ITEM);
        b2dBodyCmp.body.setTransform(b2dBodyCmp.body.getPosition(), (float) Math.toRadians(-90));
        b2dBodyCmp.body.setFixedRotation(true);


        typeCmp.type = TypeComponent.ITEM;

        entity.add(transformCmp);
        entity.add(textureCmp);
        entity.add(b2dBodyCmp);
        entity.add(typeCmp);

        ecsEngine.addEntity(entity);
    }

    public static void createApple(final ECSEngine ecsEngine, final Vector2 spawnLocation) {
        final Entity entity = new Entity();

        final TransformComponent transformCmp = ecsEngine.createComponent(TransformComponent.class);
        final TextureComponent textureCmp = ecsEngine.createComponent(TextureComponent.class);
        B2dBodyComponent b2dBodyCmp = ecsEngine.createComponent(B2dBodyComponent.class);
        final TypeComponent typeCmp = ecsEngine.createComponent(TypeComponent.class);

        transformCmp.position.set(spawnLocation.x, spawnLocation.y, 1);
        transformCmp.scale.set(0.5f, 0.5f);
        TextureAtlas textureAtlas = Assets.manager.get(AssetName.APPLE_ATLAS.getAssetName());
        TextureRegion textureRegion = textureAtlas.findRegion("apple");
        textureCmp.region = new TextureRegion(textureRegion, 0, 0, textureRegion.getRegionWidth(), textureRegion.getRegionHeight());

        b2dBodyCmp.body = BodyBuilder.createItemBody(
                ecsEngine.getScreen().getWorld(),
                new Vector2(spawnLocation.x, spawnLocation.y),
                new Vector2(textureCmp.region.getRegionWidth(), textureCmp.region.getRegionHeight()),
                entity,
                Collision.ITEM);
        b2dBodyCmp.body.setFixedRotation(true);

        typeCmp.type = TypeComponent.ITEM;

        entity.add(transformCmp);
        entity.add(textureCmp);
        entity.add(b2dBodyCmp);
        entity.add(typeCmp);

        ecsEngine.addEntity(entity);
    }

}

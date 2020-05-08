package com.nonameyet.ecs.entities.items;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.nonameyet.assets.AssetName;
import com.nonameyet.assets.Assets;
import com.nonameyet.ecs.ECSEngine;
import com.nonameyet.ecs.components.TextureComponent;
import com.nonameyet.ecs.components.TransformComponent;
import com.nonameyet.ecs.components.TypeComponent;

public class ItemsCreator {

    public static Entity createApple(final ECSEngine ecsEngine, final Vector2 spawnLocation) {

        final Entity entity = new Entity();

        final TransformComponent transformCmp = ecsEngine.createComponent(TransformComponent.class);
        final TextureComponent textureCmp = ecsEngine.createComponent(TextureComponent.class);
//        TriggerB2dBodyComponent triggerb2dbodyCmp = ecsEngine.createComponent(TriggerB2dBodyComponent.class);
        final TypeComponent typeCmp = ecsEngine.createComponent(TypeComponent.class);

        transformCmp.position.set(spawnLocation.x, spawnLocation.y, 1);
        transformCmp.scale.set(0.5f, 0.5f);
        TextureAtlas textureAtlas = Assets.manager.get(AssetName.APPLE_ATLAS.getAssetName());
        TextureRegion textureRegion = textureAtlas.findRegion("apple");
        textureCmp.region = new TextureRegion(textureRegion, 0, 0, textureRegion.getRegionWidth(), textureRegion.getRegionHeight());


//        triggerb2dbodyCmp.trigger = BodyBuilder.triggerItemBody(
//                ecsEngine.getScreen().getWorld(),
//                textureCmp.region.getRegionHeight(),
//                new Vector2(spawnLocation.x, spawnLocation.y),
//                5,
//                "APPLE",
//                Collision.ITEM);

        typeCmp.type = TypeComponent.SOCKET_ITEM;

        entity.add(transformCmp);
        entity.add(textureCmp);
//        entity.add(triggerb2dbodyCmp);
        entity.add(typeCmp);

        ecsEngine.addEntity(entity);

        return entity;
    }

}

package com.nonameyet.ecs.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.nonameyet.b2d.BodyBuilder;
import com.nonameyet.ecs.ECSEngine;
import com.nonameyet.ecs.components.B2dBodyComponent;
import com.nonameyet.ecs.components.TypeComponent;
import com.nonameyet.utils.Collision;

import static com.nonameyet.utils.Constants.PPM;

public class SceneryEntity extends Entity {

    public SceneryEntity(ECSEngine ecsEngine, Rectangle rect) {
        // Create the Entity and all the components that will go in the entity
        final Entity entity = ecsEngine.createEntity();

        B2dBodyComponent b2dbody = ecsEngine.createComponent(B2dBodyComponent.class);
        final TypeComponent type = ecsEngine.createComponent(TypeComponent.class);

        b2dbody.body = BodyBuilder.staticRectangleBody(
                ecsEngine.getScreen().getWorld(),
                new Vector2((rect.getX() + rect.getWidth() / 2) / PPM, (rect.getY() + rect.getHeight() / 2) / PPM),
                new Vector2(rect.getWidth(), rect.getHeight()),
                "SCENERY",
                Collision.OBSTACLE);

        type.type = TypeComponent.SCENERY;

        entity.add(b2dbody);
        entity.add(type);
    }

}

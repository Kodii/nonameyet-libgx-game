package com.nonameyet.ecs.entities.items;


import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Transform;
import com.nonameyet.ecs.ECSEngine;
import com.nonameyet.ecs.SocketPositionMapper;
import com.nonameyet.ecs.components.B2dBodyComponent;
import com.nonameyet.ecs.components.TextureComponent;
import com.nonameyet.ecs.components.TransformComponent;
import com.nonameyet.ecs.components.TypeComponent;

public abstract class ItemEntity extends Entity {
    protected final ECSEngine ecsEngine;

    protected final Vector2 spawnLocation;
    protected TextureAtlas textureAtlas;

    protected final TransformComponent transformCmp;
    protected final TextureComponent textureCmp;
    protected final B2dBodyComponent b2dBodyCmp;
    protected final TypeComponent typeCmp;

    public boolean shouldb2dReset = false;

    public ItemEntity(final ECSEngine ecsEngine, final Vector2 spawnLocation) {
        this.ecsEngine = ecsEngine;
        this.spawnLocation = spawnLocation;

        transformCmp = ecsEngine.createComponent(TransformComponent.class);
        transformComponent();
        this.add(transformCmp);

        textureCmp = ecsEngine.createComponent(TextureComponent.class);
        textureComponent();
        this.add(textureCmp);

        b2dBodyCmp = ecsEngine.createComponent(B2dBodyComponent.class);
        b2dBodyComponent();
        this.add(b2dBodyCmp);

        typeCmp = ecsEngine.createComponent(TypeComponent.class);
        typeComponent();
        this.add(typeCmp);

        ecsEngine.addEntity(this);
    }

    abstract void transformComponent();

    abstract void textureComponent();

    abstract void b2dBodyComponent();

    abstract void typeComponent();

    public void pickUp() {
        typeCmp.type = TypeComponent.SOCKET_ITEM;
        textureComponent();
        SocketPositionMapper.resetFlip();
    }

    public void drop() {
        typeCmp.type = TypeComponent.ITEM;
        textureComponent();
        shouldb2dReset = true;
    }

    public void checkb2dResetFlag() {
        if (shouldb2dReset)
            resetTransform();
    }

    protected void resetTransform() {
        float rotation90 = -90f - b2dBodyCmp.body.getAngle();
        Transform transform = b2dBodyCmp.body.getTransform();
        b2dBodyCmp.body.setTransform(transform.getPosition(), (float) Math.toRadians(rotation90));

        shouldb2dReset = false;
    }
}

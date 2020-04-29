package com.nonameyet.ecs;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.nonameyet.ecs.components.BodyComponent;
import com.nonameyet.ecs.components.PlayerComponent;
import com.nonameyet.ecs.components.TextureComponent;
import com.nonameyet.ecs.entities.PlayerEntity;
import com.nonameyet.ecs.systems.PlayerCameraSystem;
import com.nonameyet.ecs.systems.PlayerMovementSystem;
import com.nonameyet.screens.GameScreen;

public class ECSEngine extends PooledEngine {
    public static final ComponentMapper<PlayerComponent> playerCmpMapper = ComponentMapper.getFor(PlayerComponent.class);
    public static final ComponentMapper<BodyComponent> b2dCmpMapper = ComponentMapper.getFor(BodyComponent.class);
    public static final ComponentMapper<TextureComponent> textureCmpMapper = ComponentMapper.getFor(TextureComponent.class);

    private final GameScreen screen;

    public ECSEngine(final GameScreen screen) {
        super();
        this.screen = screen;

        this.addSystem(new PlayerMovementSystem());
        this.addSystem(new PlayerCameraSystem(screen));
    }

    public GameScreen getScreen() {
        return screen;
    }

    public void createPlayer(Vector2 position) {
        PlayerEntity playerEntity = new PlayerEntity(this, position);
    }
}

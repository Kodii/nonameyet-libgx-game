package com.nonameyet.ecs;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.nonameyet.b2d.BodyBuilder;
import com.nonameyet.ecs.components.B2dComponent;
import com.nonameyet.ecs.components.PlayerComponent;
import com.nonameyet.ecs.systems.PlayerMovementSystem;
import com.nonameyet.screens.GameScreen;
import com.nonameyet.utils.Collision;

import static com.nonameyet.utils.Constants.PPM;

public class ECSEngine extends PooledEngine {
    public static final ComponentMapper<PlayerComponent> playerCmpMapper = ComponentMapper.getFor(PlayerComponent.class);
    public static final ComponentMapper<B2dComponent> b2dCmpMapper = ComponentMapper.getFor(B2dComponent.class);

    private final GameScreen screen;

    public ECSEngine(final GameScreen screen) {
        super();
        this.screen = screen;

        this.addSystem(new PlayerMovementSystem());
    }

    public void createPlayer(final Vector2 playerSpawnLocation) {
        final Entity player = this.createEntity();

        // add components
        PlayerComponent playerComponent = this.createComponent(PlayerComponent.class);
        playerComponent.speed = 3;
        player.add(playerComponent);

        B2dComponent b2dComponent = this.createComponent(B2dComponent.class);
        b2dComponent.body = BodyBuilder.dynamicRectangleBody(
                screen.getWorld(),
                new Vector2(playerSpawnLocation.x / PPM, playerSpawnLocation.y / PPM),
                new Vector2(16, 16),
                "PLAYER",
                Collision.PLAYER
        );
        player.add(b2dComponent);

        this.addEntity(player);
    }

}

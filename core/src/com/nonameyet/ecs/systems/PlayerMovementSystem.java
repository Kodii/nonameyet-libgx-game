package com.nonameyet.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.Body;
import com.nonameyet.ecs.ECSEngine;
import com.nonameyet.ecs.components.B2dComponent;
import com.nonameyet.ecs.components.PlayerComponent;

public class PlayerMovementSystem extends IteratingSystem {

    public PlayerMovementSystem() {
        super(Family.all(PlayerComponent.class, B2dComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final PlayerComponent playerComponent = ECSEngine.playerCmpMapper.get(entity);
        final B2dComponent b2dComponent = ECSEngine.b2dCmpMapper.get(entity);

        input(b2dComponent.body, playerComponent.speed);
    }

    public void input(Body body, float speed) {

        // movement
        final float speedX;
        final float speedY;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) speedX = -speed;
        else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) speedX = +speed;
        else speedX = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) speedY = -speed;
        else if (Gdx.input.isKeyPressed(Input.Keys.UP)) speedY = +speed;
        else speedY = 0;

        //control our _player using immediate impulses
        body.applyLinearImpulse(
                (speedX - body.getLinearVelocity().x * body.getMass()),
                (speedY - body.getLinearVelocity().y * body.getMass()),
                body.getWorldCenter().x,
                body.getWorldCenter().y,
                true
        );
    }

}

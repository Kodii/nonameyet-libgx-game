package com.nonameyet.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.nonameyet.ecs.ECSEngine;
import com.nonameyet.ecs.components.B2dComponent;
import com.nonameyet.ecs.components.PlayerComponent;
import com.nonameyet.input.GameKeyInputListener;
import com.nonameyet.input.GameKeys;
import com.nonameyet.input.InputManager;

public class PlayerMovementSystem extends IteratingSystem implements GameKeyInputListener {

    // movement
    float speed;
    float speedX;
    float speedY;

    public PlayerMovementSystem() {
        super(Family.all(PlayerComponent.class, B2dComponent.class).get());

        InputManager.getInstance().addInputListener(this);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final PlayerComponent playerComponent = ECSEngine.playerCmpMapper.get(entity);
        final B2dComponent b2dComponent = ECSEngine.b2dCmpMapper.get(entity);
        speed = playerComponent.speed;

        //control our _player using immediate impulses
        b2dComponent.body.applyLinearImpulse(
                (speedX - b2dComponent.body.getLinearVelocity().x * b2dComponent.body.getMass()),
                (speedY - b2dComponent.body.getLinearVelocity().y * b2dComponent.body.getMass()),
                b2dComponent.body.getWorldCenter().x,
                b2dComponent.body.getWorldCenter().y,
                true);
    }

    @Override
    public void keyPressed(InputManager inputManager, GameKeys key) {
        switch (key) {
            case UP:
                speedY = +speed;
                speedX = 0;
                break;
            case DOWN:
                speedY = -speed;
                speedX = 0;
                break;
            case LEFT:
                speedX = -speed;
                speedY = 0;
                break;
            case RIGHT:
                speedX = +speed;
                speedY = 0;
                break;
            default:
                break;

        }
    }

    @Override
    public void keyReleased(InputManager inputManager, GameKeys key) {
        switch (key) {
            case UP:
                speedY = inputManager.isKeyPressed(GameKeys.DOWN) ? -speed : 0;
                break;
            case DOWN:
                speedY = inputManager.isKeyPressed(GameKeys.UP) ? +speed : 0;
                break;
            case LEFT:
                speedX = inputManager.isKeyPressed(GameKeys.RIGHT) ? +speed : 0;
                break;
            case RIGHT:
                speedX = inputManager.isKeyPressed(GameKeys.LEFT) ? -speed : 0;
                break;
            default:
                break;

        }
    }
}

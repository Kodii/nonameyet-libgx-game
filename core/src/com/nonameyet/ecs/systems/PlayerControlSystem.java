package com.nonameyet.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.nonameyet.ecs.components.BodyComponent;
import com.nonameyet.ecs.components.PlayerComponent;
import com.nonameyet.ecs.components.PlayerStateComponent;
import com.nonameyet.input.GameKeyInputListener;
import com.nonameyet.input.GameKeys;
import com.nonameyet.input.InputManager;

public class PlayerControlSystem extends IteratingSystem implements GameKeyInputListener {

    // movement
    float speed;
    float speedX;
    float speedY;

    private ComponentMapper<PlayerComponent> pm = ComponentMapper.getFor(PlayerComponent.class);
    private ComponentMapper<BodyComponent> bm = ComponentMapper.getFor(BodyComponent.class);
    private ComponentMapper<PlayerStateComponent> psm = ComponentMapper.getFor(PlayerStateComponent.class);

    public PlayerControlSystem() {
        super(Family.all(PlayerComponent.class, BodyComponent.class, PlayerStateComponent.class).get());

        InputManager.getInstance().addInputListener(this);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final PlayerComponent player = pm.get(entity);
        final BodyComponent b2dbody = bm.get(entity);
        final PlayerStateComponent state = psm.get(entity);

        speed = player.speed;

        //control our _player using immediate impulses
        b2dbody.body.applyLinearImpulse(
                (speedX - b2dbody.body.getLinearVelocity().x * b2dbody.body.getMass()),
                (speedY - b2dbody.body.getLinearVelocity().y * b2dbody.body.getMass()),
                b2dbody.body.getWorldCenter().x,
                b2dbody.body.getWorldCenter().y,
                true);

        if (speedY > 0) state.set(PlayerStateComponent.STATE_RUNNING_UP);
        if (speedY < 0) state.set(PlayerStateComponent.STATE_RUNNING_DOWN);
        if (speedX > 0) state.set(PlayerStateComponent.STATE_RUNNING_RIGHT);
        if (speedX < 0) state.set(PlayerStateComponent.STATE_RUNNING_LEFT);

        if (speedY == 0 && (state.get() == PlayerStateComponent.STATE_STANDING_UP || state.get() == PlayerStateComponent.STATE_RUNNING_UP))
            state.set(PlayerStateComponent.STATE_STANDING_UP);
        if (speedY == 0 && (state.get() == PlayerStateComponent.STATE_STANDING_DOWN || state.get() == PlayerStateComponent.STATE_RUNNING_DOWN))
            state.set(PlayerStateComponent.STATE_STANDING_DOWN);
        if (speedX == 0 && (state.get() == PlayerStateComponent.STATE_STANDING_RIGHT || state.get() == PlayerStateComponent.STATE_RUNNING_RIGHT))
            state.set(PlayerStateComponent.STATE_STANDING_RIGHT);
        if (speedX == 0 && (state.get() == PlayerStateComponent.STATE_STANDING_LEFT || state.get() == PlayerStateComponent.STATE_RUNNING_LEFT))
            state.set(PlayerStateComponent.STATE_STANDING_LEFT);
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

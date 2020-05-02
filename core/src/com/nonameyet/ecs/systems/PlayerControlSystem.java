package com.nonameyet.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.nonameyet.ecs.components.B2dBodyComponent;
import com.nonameyet.ecs.components.PlayerComponent;
import com.nonameyet.ecs.components.StateComponent;
import com.nonameyet.ecs.components.TriggerB2dBodyComponent;
import com.nonameyet.input.GameKeyInputListener;
import com.nonameyet.input.GameKeys;
import com.nonameyet.input.InputManager;

import static com.nonameyet.ecs.ComponentMappers.*;

public class PlayerControlSystem extends IteratingSystem implements GameKeyInputListener {

    // movement
    float speed;
    float speedX;
    float speedY;

    public PlayerControlSystem() {
        super(Family.all(PlayerComponent.class, B2dBodyComponent.class, StateComponent.class).get());

        InputManager.getInstance().addInputListener(this);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final PlayerComponent player = playerCmpMapper.get(entity);
        final B2dBodyComponent b2dbody = b2dbodyCmpMapper.get(entity);
//        final TriggerB2dBodyComponent triggerB2dBody = triggerb2dbodyCmpMapper.get(entity);
        final StateComponent state = stateCmpMapper.get(entity);

        speed = player.speed;

        //control our _player using immediate impulses
        b2dbody.body.applyLinearImpulse(
                (speedX - b2dbody.body.getLinearVelocity().x * b2dbody.body.getMass()),
                (speedY - b2dbody.body.getLinearVelocity().y * b2dbody.body.getMass()),
                b2dbody.body.getWorldCenter().x,
                b2dbody.body.getWorldCenter().y,
                true);

        if (speedY > 0) {
            if (state.get() != StateComponent.STATE_RUNNING_UP)
                state.set(StateComponent.STATE_RUNNING_UP);
        }
        if (speedY < 0) {
            if (state.get() != StateComponent.STATE_RUNNING_DOWN)
                state.set(StateComponent.STATE_RUNNING_DOWN);
        }
        if (speedX > 0) {
            if (state.get() != StateComponent.STATE_RUNNING_RIGHT)
                state.set(StateComponent.STATE_RUNNING_RIGHT);
        }
        if (speedX < 0) {
            if (state.get() != StateComponent.STATE_RUNNING_LEFT)
                state.set(StateComponent.STATE_RUNNING_LEFT);
        }

        if (speedY == 0 && state.get() == StateComponent.STATE_RUNNING_UP)
            state.set(StateComponent.STATE_STANDING_UP);
        if (speedY == 0 && state.get() == StateComponent.STATE_RUNNING_DOWN)
            state.set(StateComponent.STATE_STANDING_DOWN);
        if (speedX == 0 && state.get() == StateComponent.STATE_RUNNING_RIGHT)
            state.set(StateComponent.STATE_STANDING_RIGHT);
        if (speedX == 0 && state.get() == StateComponent.STATE_RUNNING_LEFT)
            state.set(StateComponent.STATE_STANDING_LEFT);
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

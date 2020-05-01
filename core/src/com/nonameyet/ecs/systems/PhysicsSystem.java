package com.nonameyet.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.nonameyet.ecs.components.B2dBodyComponent;
import com.nonameyet.ecs.components.TransformComponent;

import static com.nonameyet.ecs.ComponentMappers.b2dbodyCmpMapper;
import static com.nonameyet.ecs.ComponentMappers.transformCmpMapper;

public class PhysicsSystem extends IteratingSystem {

    private static final float MAX_STEP_TIME = 1 / 60f;
    private static float accumulator = 0f;

    private World world;
    private Array<Entity> bodiesQueue;

    public PhysicsSystem(World world) {
        super(Family.all(B2dBodyComponent.class, TransformComponent.class).get());

        this.world = world;
        this.bodiesQueue = new Array<>();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        if (accumulator >= MAX_STEP_TIME) {
            world.step(MAX_STEP_TIME, 6, 2);
            accumulator -= MAX_STEP_TIME;

            //Entity Queue
            for (Entity entity : bodiesQueue) {
                TransformComponent tfm = transformCmpMapper.get(entity);
                B2dBodyComponent b2dbody = b2dbodyCmpMapper.get(entity);
                Vector2 position = b2dbody.body.getPosition();
                tfm.position.x = position.x;
                tfm.position.y = position.y;
                tfm.rotation = b2dbody.body.getAngle() * MathUtils.radiansToDegrees;
            }
        }

        bodiesQueue.clear();

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        bodiesQueue.add(entity);
    }
}

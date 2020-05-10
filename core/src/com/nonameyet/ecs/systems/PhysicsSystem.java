package com.nonameyet.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.nonameyet.ecs.components.B2dBodyComponent;
import com.nonameyet.ecs.components.TextureComponent;
import com.nonameyet.ecs.components.TransformComponent;
import com.nonameyet.ecs.components.TypeComponent;
import com.nonameyet.ecs.entities.humans.PlayerEntity;
import com.nonameyet.ecs.entities.items.ItemEntity;

import static com.nonameyet.ecs.ComponentMappers.*;
import static com.nonameyet.utils.Constants.FIXED_TIME_STEP;
import static com.nonameyet.utils.Constants.PPM;

public class PhysicsSystem extends IteratingSystem {

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
        if (accumulator >= FIXED_TIME_STEP) {
            world.step(FIXED_TIME_STEP, 6, 2);
            accumulator -= FIXED_TIME_STEP;

            Vector2 playerPosition = null;

            // get player position to update ArmEntity position
            for (Entity entity : bodiesQueue) {
                TypeComponent type = typeCmpMapper.get(entity);
                B2dBodyComponent b2dbody = b2dbodyCmpMapper.get(entity);

                if (type != null && type.type == TypeComponent.PLAYER) {
                    playerPosition = b2dbody.body.getPosition();
                }
            }

            //Entity Queue
            for (Entity entity : bodiesQueue) {
                TransformComponent tfm = transformCmpMapper.get(entity);

                B2dBodyComponent b2dbody = b2dbodyCmpMapper.get(entity);
                TypeComponent type = typeCmpMapper.get(entity);
                TextureComponent texture = textureCmpMapper.get(entity);
                Vector2 position = b2dbody.body.getPosition();

                if (type != null && type.type == TypeComponent.ITEM) {
                    ((ItemEntity) entity).checkb2dResetFlag();
                }


                if (type != null && (type.type == TypeComponent.PLAYER || type.type == TypeComponent.SCENERY_ITEMS)) {
                    tfm.position.x = position.x;
                    tfm.position.y = position.y + (texture.region.getRegionHeight() / PPM / 2) - ((texture.region.getRegionHeight() / PPM / 12));
                    tfm.rotation = b2dbody.body.getAngle() * MathUtils.radiansToDegrees;

                    // update ArmEntity position
                    if (type.type == TypeComponent.PLAYER) {
                        PlayerEntity playerEntity = (PlayerEntity) entity;
                        playerEntity.armEntity.transformCmp.position.x = playerPosition.x;
                        playerEntity.armEntity.transformCmp.position.y = playerPosition.y + (texture.region.getRegionHeight() / PPM / 2) - ((texture.region.getRegionHeight() / PPM / 12));
                        playerEntity.armEntity.transformCmp.rotation = b2dbody.body.getAngle() * MathUtils.radiansToDegrees;
                    }

                } else if (type != null && type.type == TypeComponent.SOCKET_ITEM) {
                    b2dbody.body.setTransform(tfm.position.x, tfm.position.y, (float) Math.toRadians(tfm.rotation));
                } else {
                    tfm.position.x = position.x;
                    tfm.position.y = position.y;
                }
            }
        }

        bodiesQueue.clear();

    }


    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        bodiesQueue.add(entity);
    }
}

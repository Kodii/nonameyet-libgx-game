package com.nonameyet.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;
import com.nonameyet.ecs.components.*;
import com.nonameyet.screens.GameScreen;

import java.util.Comparator;

import static com.nonameyet.ecs.ComponentMappers.*;
import static com.nonameyet.utils.Constants.PPM;

public class RenderingSystem extends SortedIteratingSystem {

    static {
        comparator = new ZComparator();
    }

    private Batch batch;
    private Array<Entity> renderQueue; // an array used to allow sorting of images allowing us to draw images on top of each other
    private static Comparator<Entity> comparator; // a comparator to sort images based on the z position of the transfromComponent
    private OrthographicCamera cam;

    public RenderingSystem(GameScreen screen) {
        super(Family.all(TransformComponent.class, TextureComponent.class).get(), comparator);

        // create the array for sorting entities
        renderQueue = new Array<>();

        this.batch = screen.getMapRenderer().getBatch();

        cam = screen.getCamera();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // sort the renderQueue based on z index

        renderQueue.sort(comparator);
        changePositionsDependPlayerPosition(renderQueue);
        renderQueue.sort(comparator);

        // update camera and sprite batch
        batch.setProjectionMatrix(cam.combined);
        batch.enableBlending();
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.begin();

        Gdx.graphics.setTitle("NoNameYet | fps: " + Gdx.graphics.getFramesPerSecond());

        // loop through each entity in our render queue
        for (Entity entity : renderQueue) {
            TextureComponent texture = textureCmpMapper.get(entity);
            TransformComponent transform = transformCmpMapper.get(entity);
//            SubTextureComponent subTexture = subTextureCmpMapper.get(entity);
//            SubTransformComponent subTransform = subTransformCmpMapper.get(entity);

            if (texture.region == null || transform.isHidden) {
                continue;
            }

            float width = texture.region.getRegionWidth();
            float height = texture.region.getRegionHeight();
            float originX = width / 2;
            float originY = height / 2;

            batch.draw(texture.region,
                    transform.position.x - originX, transform.position.y - originY,
                    originX, originY,
                    width, height,
                    1 / PPM, 1 / PPM, 0);

//            // sub texture
//            if (subTexture != null) {
//                float subWidth = subTexture.region.getRegionWidth();
//                float subHeight = subTexture.region.getRegionHeight();
//                float subOriginX = subWidth / 2;
//                float subOriginY = subHeight / 2;
//                batch.draw(subTexture.region,
//                        subTransform.position.x - subOriginX, subTransform.position.y - subOriginY,
//                        subOriginX, subOriginY,
//                        subWidth, subHeight,
//                        1 / PPM, 1 / PPM, 0);
//            }
        }

        // render particle effects
        for (Entity entity : renderQueue) {
            ParticleEffectComponent particleEffectCmp = particleEffectCmpMapper.get(entity);
            if (particleEffectCmp != null && particleEffectCmp.effect != null) {
                particleEffectCmp.effect.draw(batch);
            }
        }
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        batch.end();
        renderQueue.clear();
    }

    private void changePositionsDependPlayerPosition(Array<Entity> renderQueue) {
        float playerY = 0;

        // find player
        for (Entity entity : renderQueue) {
            TypeComponent type = typeCmpMapper.get(entity);
            if (type != null && type.type == TypeComponent.PLAYER) {
                B2dBodyComponent b2dBody = b2dbodyCmpMapper.get(entity);
                playerY = b2dBody.body.getPosition().y;
                break;
            }
        }

        for (Entity entity : renderQueue) {
            TypeComponent type = typeCmpMapper.get(entity);
            if (type != null && type.type != TypeComponent.PLAYER) {
                TransformComponent transform = transformCmpMapper.get(entity);
                B2dBodyComponent b2dBody = b2dbodyCmpMapper.get(entity);

                if (b2dBody != null) {
                    if (b2dBody.body.getPosition().y > playerY)
                        transform.position.z = 3;

                    if (b2dBody.body.getPosition().y < playerY)
                        transform.position.z = -3;
                }
            }
        }
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        renderQueue.add(entity);

    }
}

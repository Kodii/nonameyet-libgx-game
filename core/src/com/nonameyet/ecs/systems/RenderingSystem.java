package com.nonameyet.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;
import com.nonameyet.ecs.components.TextureComponent;
import com.nonameyet.ecs.components.TransformComponent;
import com.nonameyet.screens.GameScreen;

import java.util.Comparator;

import static com.nonameyet.ecs.ComponentMappers.textureCmpMapper;
import static com.nonameyet.ecs.ComponentMappers.transformCmpMapper;
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

        // update camera and sprite batch
        cam.update();
        batch.setProjectionMatrix(cam.combined);
        batch.enableBlending();
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.begin();

        Gdx.graphics.setTitle("NoNameYet | fps: " + Gdx.graphics.getFramesPerSecond());

        // loop through each entity in our render queue
        for (Entity entity : renderQueue) {
            TextureComponent tex = textureCmpMapper.get(entity);
            TransformComponent t = transformCmpMapper.get(entity);

            if (tex.region == null || t.isHidden) {
                continue;
            }

            float width = tex.region.getRegionWidth();
            float height = tex.region.getRegionHeight();

            float originX = width / 2;
            float originY = height / 2;

            batch.draw(tex.region,
                    t.position.x - originX, t.position.y - originY,
                    originX, originY,
                    width, height,
                    1 / PPM, 1 / PPM, 0);
        }

        batch.end();
        renderQueue.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        renderQueue.add(entity);

    }
}

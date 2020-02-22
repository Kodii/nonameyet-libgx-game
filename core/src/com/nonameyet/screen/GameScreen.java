package com.nonameyet.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.*;
import com.nonameyet.NoNameYet;

import static com.nonameyet.NoNameYet.*;

public class GameScreen extends AbstractScreen {

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    public GameScreen(NoNameYet context) {
        super(context);

        createExampleShapes();

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        cam.position.set(viewPort.getScreenWidth() / 2, viewPort.getScreenHeight() / 2, 0);
    }

    // todo: remove this soon (example)
    private void createExampleShapes() {
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;

        // create a circle
        bodyDef.position.set(3f, 5);
        bodyDef.gravityScale = 1;
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        fixtureDef.isSensor = false;
        fixtureDef.restitution = 0.5f;
        fixtureDef.friction = 0.2f;
        fixtureDef.filter.categoryBits = BIT_CIRCLE;
        fixtureDef.filter.maskBits = BIT_GROUND | BIT_BOX;
        CircleShape cShape = new CircleShape();
        cShape.setRadius(0.3f);
        fixtureDef.shape = cShape;
        body.createFixture(fixtureDef);
        cShape.dispose();

        // create a box
        bodyDef.position.set(6f, 9);
        bodyDef.gravityScale = 1;
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        fixtureDef.isSensor = false;
        fixtureDef.restitution = 0.5f;
        fixtureDef.friction = 0.2f;
        fixtureDef.filter.categoryBits = BIT_BOX;
        fixtureDef.filter.maskBits = BIT_GROUND | BIT_CIRCLE;
        PolygonShape pShape = new PolygonShape();
        pShape.setAsBox(0.3f, 0.3f);
        fixtureDef.shape = pShape;
        body.createFixture(fixtureDef);
        pShape.dispose();

        // create a platform
        bodyDef.position.set(8f, 1);
        bodyDef.gravityScale = 1;
        bodyDef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bodyDef);

        fixtureDef.isSensor = false;
        fixtureDef.restitution = 0;
        fixtureDef.friction = 0.2f;
        fixtureDef.filter.categoryBits = BIT_GROUND;
        fixtureDef.filter.maskBits = -1;
        pShape = new PolygonShape();
        pShape.setAsBox(6, 0.2f);
        fixtureDef.shape = pShape;
        body.createFixture(fixtureDef);
        pShape.dispose();
    }

    @Override
    public void handleInput(float dt) {
        super.handleInput(dt);

        if (Gdx.input.isKeyPressed(Input.Keys.W))
            cam.position.y += 1000 * dt;
        if (Gdx.input.isKeyPressed(Input.Keys.S))
            cam.position.y -= 1000 * dt;
        if (Gdx.input.isKeyPressed(Input.Keys.A))
            cam.position.x -= 1000 * dt;
        if (Gdx.input.isKeyPressed(Input.Keys.D))
            cam.position.x += 1000 * dt;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        renderer.setView(cam);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        Gdx.gl.glClearColor(0.4f, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
//            context.setScreen(ScreenType.LOADING);
//        }
//        viewPort.apply(true);
//        box2DDebugRenderer.render(world, viewPort.getCamera().combined);

        renderer.render();
    }
}

package com.nonameyet.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.nonameyet.NoNameYet;
import com.nonameyet.WorldContactListener;
import com.nonameyet.environment.AssetName;
import com.nonameyet.sprites.Player;
import com.nonameyet.tools.B2WorldCreator;

import static com.nonameyet.utils.Constants.*;

public class GameScreen implements Screen {
    private static final String TAG = LoadingScreen.class.getSimpleName();
    private final NoNameYet context;

    private OrthographicCamera gameCamera;
    private Viewport gamePort;

    // Tiled map
    private OrthogonalTiledMapRenderer renderer;

    // Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;

    private Player player;

    private WorldContactListener worldContactListener;


    public GameScreen(NoNameYet context) {
        this.context = context;

        // create cam used to follow through cam world
        gameCamera = new OrthographicCamera();
        this.gameCamera.zoom = 1f;

        // create a FitViewport to maintain virtual aspect ratio
        this.gamePort = new FitViewport(V_WIDTH, V_HEIGHT, gameCamera);

        // setup map renderer
        renderer = new OrthogonalTiledMapRenderer(null, UNIT_SCALE, context.batch);

        world = new World(new Vector2(0, 0), true);
        b2dr = new Box2DDebugRenderer();

        new B2WorldCreator(world);

        // create player
        player = new Player(world);

        // set contact listener
        worldContactListener = new WorldContactListener();
        world.setContactListener(worldContactListener);
    }

    @Override
    public void show() {

        renderer.setMap((TiledMap) context.getAssets().manager.get(AssetName.MAP_TMX.getAssetName()));
    }

    public void handleInput(float dt) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            context.setScreen(ScreenType.LOADING);
        }

        // movement
        final float speedX;
        final float speedY;

        if (Gdx.input.isKeyPressed(Input.Keys.A)) speedX = -3;
        else if (Gdx.input.isKeyPressed(Input.Keys.D)) speedX = +3;
        else speedX = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.S)) speedY = -3;
        else if (Gdx.input.isKeyPressed(Input.Keys.W)) speedY = +3;
        else speedY = 0;

        player.b2body.applyLinearImpulse(
                (speedX - player.b2body.getLinearVelocity().x * player.b2body.getMass()),
                (speedY - player.b2body.getLinearVelocity().y * player.b2body.getMass()),
                player.b2body.getWorldCenter().x,
                player.b2body.getWorldCenter().y,
                true
        );
    }

    public void update(float delta) {
        // handle user input first
        handleInput(delta);

        world.step(FIXED_TIME_STEP, 6, 2);

        gameCamera.position.x = player.b2body.getPosition().x;
        gameCamera.position.y = player.b2body.getPosition().y;

        // update our gameCamera with correct coordinates after changes
        gameCamera.update();

        // tell our renderer to draw only what our camera can see in our game
        renderer.setView(gameCamera);
    }

    @Override
    public void render(float delta) {
        update(delta);

        // Clear the game screen
        Gdx.gl.glClearColor(0.4f, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();

        b2dr.render(world, gameCamera.combined);

    }

    @Override
    public void resize(int width, int height) {
        // updated our game viewport
        gamePort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
    }
}

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
import com.nonameyet.environment.Assets;
import com.nonameyet.sprites.Player;
import com.nonameyet.tools.B2WorldCreator;

import static com.nonameyet.utils.Constants.*;

public class GameScreen implements Screen {
    private static final String TAG = LoadingScreen.class.getSimpleName();
    //Reference to our Game, used to set Screens
    private final NoNameYet context;

    //basic playscreen variables
    private OrthographicCamera gameCamera;
    private Viewport gamePort;

    //Tiled map variables
    private OrthogonalTiledMapRenderer renderer;

    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;

    //sprites
    private Player player;

    private WorldContactListener worldContactListener;


    public GameScreen(NoNameYet context) {
        this.context = context;

        // create cam used to follow through cam world
        gameCamera = new OrthographicCamera();
        gameCamera.zoom = 0.5f;

        //create a FitViewport to maintain virtual aspect ratio despite screen size
        this.gamePort = new FitViewport(V_WIDTH / PPM, V_HEIGHT / PPM, gameCamera);

        // setup map renderer
        renderer = new OrthogonalTiledMapRenderer(null, 1 / PPM, context.batch);

        //initially set our gamcam to be centered correctly at the start of of map
        gameCamera.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        //create our Box2D world, setting no gravity in X, 0 gravity in Y, and allow bodies to sleep
        world = new World(new Vector2(0, 0), true);
        //allows for debug lines of our box2d world.
        b2dr = new Box2DDebugRenderer();

        new B2WorldCreator(world);

        // create player in our game world
        player = new Player(world, this.getAssets());

        // set contact listener
        worldContactListener = new WorldContactListener();
        world.setContactListener(worldContactListener);
    }

    public Assets getAssets() {
        return context.getAssets();
    }

    @Override
    public void show() {

        renderer.setMap((TiledMap) context.getAssets().manager.get(AssetName.MAP_TMX.getAssetName()));
    }

    public void handleInput(float delta) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            context.setScreen(ScreenType.LOADING);
        }

        // movement
        final float speedX;
        final float speedY;

        if (Gdx.input.isKeyPressed(Input.Keys.A)) speedX = -1;
        else if (Gdx.input.isKeyPressed(Input.Keys.D)) speedX = +1;
        else speedX = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.S)) speedY = -1;
        else if (Gdx.input.isKeyPressed(Input.Keys.W)) speedY = +1;
        else speedY = 0;

        //control our player using immediate impulses
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

        //takes 1 step in the physics simulation(60 times per second)
        world.step(FIXED_TIME_STEP, 6, 2);

        player.update(delta);

        //attach our gamecam to our players.x coordinate
        gameCamera.position.x = player.b2body.getPosition().x;
        gameCamera.position.y = player.b2body.getPosition().y;

        // update our gameCamera with correct coordinates after changes
        gameCamera.update();

        //tell our renderer to draw only what our camera can see in our game world.
        renderer.setView(gameCamera);
    }

    @Override
    public void render(float delta) {
        //separate our update logic from render
        update(delta);

        // Clear the game screen
        Gdx.gl.glClearColor(0.4f, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        //render our game map
        renderer.render();

        //renderer our Box2DDebugLines
        b2dr.render(world, gameCamera.combined);

        context.batch.setProjectionMatrix(gameCamera.combined);
        context.batch.begin();
        player.draw(context.batch);
        context.batch.end();

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
        //dispose of all our opened resources
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
    }
}

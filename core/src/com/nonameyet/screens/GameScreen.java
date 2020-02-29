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
import com.nonameyet.environment.AssetName;
import com.nonameyet.environment.Assets;
import com.nonameyet.maps.MapFactory;
import com.nonameyet.sprites.Player;
import com.nonameyet.tools.WorldContactListener;

import static com.nonameyet.utils.Constants.*;

public class GameScreen implements Screen {
    private static final String TAG = GameScreen.class.getSimpleName();
    //Reference to our Game, used to set Screens
    private final NoNameYet _game;

    //basic playscreen variables
    private Viewport gamePort;

    //Tiled maps variables
    private OrthogonalTiledMapRenderer _mapRenderer;
    private OrthographicCamera _camera;

    //Box2d variables
    private World world;
    private TiledMap map;
    private Box2DDebugRenderer b2dr;

    //sprites
    private Player player;

    private WorldContactListener worldContactListener;

    public boolean isMapChanged = false;

    public GameScreen(NoNameYet game) {
        this._game = game;

        // create cam used to follow through cam world
        _camera = new OrthographicCamera();

        //create a FitViewport to maintain virtual aspect ratio despite screen size
        this.gamePort = new FitViewport(V_WIDTH / PPM, V_HEIGHT / PPM, _camera);

        //Load our map and setup our map renderer
        map = _game.getAssets().manager.get(AssetName.MAP_TOWN_TMX.getAssetName());

        //initially set our gamcam to be centered correctly at the start of of maps
        _camera.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        //create our Box2D world, setting no gravity in X, 0 gravity in Y, and allow bodies to sleep
        world = new World(new Vector2(0, 0), true);
        //allows for debug lines of our box2d world.
        b2dr = new Box2DDebugRenderer();

        MapFactory.getMap(this, MapFactory.MapType.TOWN);

        // create player in our game world
        player = new Player(world, this.getAssets());
    }

    public Assets getAssets() {
        return _game.getAssets();
    }

    @Override
    public void show() {

//         set contact listener
        world.setContactListener(new WorldContactListener(this));

        if (_mapRenderer == null) {
            _mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / PPM);
        }

    }

    public void handleInput(float delta) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            _game.setScreen(ScreenType.LOADING);
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


        if (isMapChanged) {
            changeToWorldMap();
        }

        player.update(delta);

        //attach our gamecam to our players.x coordinate
        _camera.position.x = player.b2body.getPosition().x;
        _camera.position.y = player.b2body.getPosition().y;

        // update our _camera with correct coordinates after changes
        _camera.update();

        //tell our _mapRenderer to draw only what our _camera can see in our game world.
        _mapRenderer.setView(_camera);

    }

    @Override
    public void render(float delta) {
        //separate our update logic from render
        update(delta);

        // Clear the game screen
        Gdx.gl.glClearColor(0.4f, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        //render our game maps
        _mapRenderer.render();

        //_mapRenderer our Box2DDebugLines
        b2dr.render(world, _camera.combined);

        _game.batch.setProjectionMatrix(_camera.combined);
        _game.batch.begin();
        Gdx.graphics.setTitle("NoNameYet | fps: " + Gdx.graphics.getFramesPerSecond());
        player.draw(_game.batch);
        _game.batch.end();

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

        if (_mapRenderer != null) {
            _mapRenderer.dispose();
        }
        world.dispose();
        b2dr.dispose();
    }

    public void changeToWorldMap() {
        isMapChanged = false;

        world.dispose();
        world = new World(new Vector2(0, 0), true);

        map = _game.getAssets().manager.get(AssetName.MAP_TOP_WORLD_TMX.getAssetName());
        _mapRenderer.setMap(map);
        MapFactory.getMap(this, MapFactory.MapType.TOP_WORLD);

        // create player in our game world
        player = new Player(world, this.getAssets());

        //         set contact listener
        world.setContactListener(new WorldContactListener(this));

    }

    public World getWorld() {
        return world;
    }

    public TiledMap getMap() {
        return map;
    }
}

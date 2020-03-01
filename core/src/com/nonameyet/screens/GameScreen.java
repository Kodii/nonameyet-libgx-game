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
import com.nonameyet.NoNameYet;
import com.nonameyet.maps.MapFactory;
import com.nonameyet.maps.MapManager;
import com.nonameyet.sprites.Player;

import static com.nonameyet.utils.Constants.FIXED_TIME_STEP;
import static com.nonameyet.utils.Constants.PPM;

public class GameScreen implements Screen {
    private static final String TAG = GameScreen.class.getSimpleName();
    public final NoNameYet game;

    public static class VIEWPORT {
        public static float viewportWidth;
        public static float viewportHeight;
        public static float virtualWidth;
        public static float virtualHeight;
        public static float physicalWidth;
        public static float physicalHeight;
        public static float aspectRatio;
    }

    protected OrthogonalTiledMapRenderer _mapRenderer = null;
    protected MapManager _mapMgr;
    protected OrthographicCamera _camera = null;

    //Box2d variables
    public World world = null;
    private TiledMap _map;
    private Box2DDebugRenderer _b2dr;

    //sprites
    private Player _player;

    public boolean isMapChanged = false;

    public GameScreen(NoNameYet game) {
        this.game = game;
        _mapMgr = new MapManager(this);

        //_camera setup
        setupViewport(16, 9);

        //get the current size
        _camera = new OrthographicCamera();
        _camera.setToOrtho(false, VIEWPORT.viewportWidth, VIEWPORT.viewportHeight);

        _mapRenderer.setMap(_mapMgr.getCurrentTiledMap());
        _mapMgr.loadMap(MapFactory.MapType.TOWN);

        //allows for debug lines of our box2d _world.
        _b2dr = new Box2DDebugRenderer();

        // create _player in our game _world
        _player = new Player(world, game.getAssets());
    }

    @Override
    public void show() {


        if (_mapRenderer == null) {
            _mapRenderer = new OrthogonalTiledMapRenderer(_map, 1 / PPM);
        }

    }

    public void handleInput(float delta) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            game.setScreen(ScreenType.LOADING);
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

        //control our _player using immediate impulses
        _player.b2body.applyLinearImpulse(
                (speedX - _player.b2body.getLinearVelocity().x * _player.b2body.getMass()),
                (speedY - _player.b2body.getLinearVelocity().y * _player.b2body.getMass()),
                _player.b2body.getWorldCenter().x,
                _player.b2body.getWorldCenter().y,
                true
        );
    }

    public void update(float delta) {
        // handle user input first
        handleInput(delta);

        //takes 1 step in the physics simulation(60 times per second)
        world.step(FIXED_TIME_STEP, 6, 2);


        if (_mapMgr.isMapChanged()) {
            if (world != null)
                world.dispose();
            world = new World(new Vector2(0, 0), true);


            _mapRenderer.setMap(_mapMgr.getCurrentTiledMap());
            _mapMgr.loadMap(MapFactory.MapType.TOWN);


        }

        _player.update(delta);

        //attach our gamecam to our players.x coordinate
        _camera.position.x = _player.b2body.getPosition().x;
        _camera.position.y = _player.b2body.getPosition().y;

        // update our _camera with correct coordinates after changes
        _camera.update();

        //tell our _mapRenderer to draw only what our _camera can see in our game _world.
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
        _b2dr.render(world, _camera.combined);

        _mapRenderer.getBatch().setProjectionMatrix(_camera.combined);
        _mapRenderer.getBatch().begin();
        Gdx.graphics.setTitle("NoNameYet | fps: " + Gdx.graphics.getFramesPerSecond());
        _player.draw(_mapRenderer.getBatch());
        _mapRenderer.getBatch().end();

    }

    @Override
    public void resize(int width, int height) {
        setupViewport(16, 9);
        _camera.setToOrtho(false, VIEWPORT.viewportWidth, VIEWPORT.viewportHeight);
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
        _b2dr.dispose();
    }

    private void setupViewport(int width, int height) {
        //Make the viewport a percentage of the total display area
        VIEWPORT.virtualWidth = width;
        VIEWPORT.virtualHeight = height;

        //Current viewport dimensions
        VIEWPORT.viewportWidth = VIEWPORT.virtualWidth;
        VIEWPORT.viewportHeight = VIEWPORT.virtualHeight;

        //pixel dimensions of display
        VIEWPORT.physicalWidth = Gdx.graphics.getWidth();
        VIEWPORT.physicalHeight = Gdx.graphics.getHeight();

        //aspect ratio for current viewport
        VIEWPORT.aspectRatio = (VIEWPORT.virtualWidth / VIEWPORT.virtualHeight);

        //update viewport if there could be skewing
        if (VIEWPORT.physicalWidth / VIEWPORT.physicalHeight >= VIEWPORT.aspectRatio) {
            //Letterbox left and right
            VIEWPORT.viewportWidth = VIEWPORT.viewportHeight * (VIEWPORT.physicalWidth / VIEWPORT.physicalHeight);
            VIEWPORT.viewportHeight = VIEWPORT.virtualHeight;
        } else {
            //letterbox above and below
            VIEWPORT.viewportWidth = VIEWPORT.virtualWidth;
            VIEWPORT.viewportHeight = VIEWPORT.viewportWidth * (VIEWPORT.physicalHeight / VIEWPORT.physicalWidth);
        }

        Gdx.app.debug(TAG, "WorldRenderer: virtual: (" + VIEWPORT.virtualWidth + "," + VIEWPORT.virtualHeight + ")");
        Gdx.app.debug(TAG, "WorldRenderer: viewport: (" + VIEWPORT.viewportWidth + "," + VIEWPORT.viewportHeight + ")");
        Gdx.app.debug(TAG, "WorldRenderer: physical: (" + VIEWPORT.physicalWidth + "," + VIEWPORT.physicalHeight + ")");
    }

    public World getWorld() {
        return world;
    }
}

package com.nonameyet.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.nonameyet.NoNameYet;
import com.nonameyet.maps.MapManager;
import com.nonameyet.sprites.Player;

import static com.nonameyet.utils.Constants.FIXED_TIME_STEP;
import static com.nonameyet.utils.Constants.PPM;

public class GameScreen implements Screen {
    private static final String TAG = GameScreen.class.getSimpleName();
    public final NoNameYet game;

    public static class VIEWPORT {
        static float viewportWidth;
        static float viewportHeight;
        static float virtualWidth;
        static float virtualHeight;
        static float physicalWidth;
        static float physicalHeight;
        static float aspectRatio;
    }

    private OrthogonalTiledMapRenderer _mapRenderer;
    private MapManager _mapMgr;
    private OrthographicCamera _camera;

    //Box2d variables
    private World _world;
    private Box2DDebugRenderer _b2dr;

    //sprites
    private Player _player;

    public GameScreen(NoNameYet game) {
        this.game = game;
        _mapMgr = new MapManager(this);
    }

    @Override
    public void show() {
        //_camera setup
        setupViewport(16, 9);

        //get the current size
        _camera = new OrthographicCamera();
        _camera.setToOrtho(false, VIEWPORT.viewportWidth, VIEWPORT.viewportHeight);

        if (_mapRenderer == null) {
            _mapRenderer = new OrthogonalTiledMapRenderer(_mapMgr.getCurrentTiledMap(), 1 / PPM);
        }

        //allows for debug lines of our box2d _world.
        _b2dr = new Box2DDebugRenderer();

        // create _player in our game _world
        _player = new Player(this);
    }

    public void update(float delta) {
        // handle user input first
        _player.input();

        //takes 1 step in the physics simulation(60 times per second)
        _world.step(FIXED_TIME_STEP, 6, 2);

        if (_mapMgr.isMapChanged()) {
            _mapMgr.loadMap(_mapMgr.getCurrentMapType());
            _mapRenderer.setMap(_mapMgr.getCurrentTiledMap());

            _player = new Player(this);
        }

        _player.update(delta);

        fixBounds();

        // update our _camera with correct coordinates after changes
        _camera.update();

        //tell our _mapRenderer to draw only what our _camera can see in our game _world.
        _mapRenderer.setView(_camera);

    }

    /**
     * keep camera within bounds of TiledMap
     */
    private void fixBounds() {
        MapProperties prop = _mapRenderer.getMap().getProperties();

        int mapWidth = prop.get("width", Integer.class);
        int mapHeight = prop.get("height", Integer.class);
        int tilePixelWidth = prop.get("tilewidth", Integer.class);
        int tilePixelHeight = prop.get("tileheight", Integer.class);

        int mapPixelWidth = mapWidth * tilePixelWidth;
        int mapPixelHeight = mapHeight * tilePixelHeight;


        // These values likely need to be scaled according to your world coordinates.
        // The left boundary of the map (x)
        int mapLeft = 0;
        // The right boundary of the map (x + width)
        int mapRight = (int) (0 + (mapWidth / PPM));
        // The bottom boundary of the map (y)
        int mapBottom = 0;
        // The top boundary of the map (y + height)
        int mapTop = (int) (0 + (mapHeight / PPM));
        // The camera dimensions, halved
        float cameraHalfWidth = _camera.viewportWidth * .5f;
        float cameraHalfHeight = _camera.viewportHeight * .5f;

        // Move camera after player as normal
        //attach our gamecam to our players.x coordinate
        _camera.position.x = _player.b2body.getPosition().x;
        _camera.position.y = _player.b2body.getPosition().y;

        float cameraLeft = _camera.position.x - cameraHalfWidth;
        float cameraRight = _camera.position.x + cameraHalfWidth;
        float cameraBottom = _camera.position.y - cameraHalfHeight;
        float cameraTop = _camera.position.y + cameraHalfHeight;

        // Horizontal axis
        if (_mapRenderer.getViewBounds().getWidth() < _camera.viewportWidth) {
            _camera.position.x = mapRight / 2;
        } else if (cameraLeft <= mapLeft) {
            _camera.position.x = mapRight / 2;
            _camera.position.x = mapLeft + cameraHalfWidth;
        } else if (cameraRight >= mapRight) {
            _camera.position.x = mapRight / 2;
            _camera.position.x = mapRight - cameraHalfWidth;
        }

        // Vertical axis
        if (_mapRenderer.getViewBounds().getHeight() < _camera.viewportHeight) {
            _camera.position.y = mapTop / 2;
        } else if (cameraBottom <= mapBottom) {
            _camera.position.y = mapBottom + cameraHalfHeight;
        } else if (cameraTop >= mapTop) {
            _camera.position.y = mapTop - cameraHalfHeight;
        }
    }

    @Override
    public void render(float delta) {
        //separate our update logic from render
        update(delta);

        // Clear the game screen
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        _mapRenderer.getBatch().enableBlending();
        _mapRenderer.getBatch().setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        //render our game maps
        _mapRenderer.render();

        //_mapRenderer our Box2DDebugLines
        _b2dr.render(_world, _camera.combined);

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
        _world.dispose();
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


    public MapManager getMapMgr() {
        return _mapMgr;
    }

    public World getWorld() {
        return _world;
    }

    public void setWorld(World world) {
        this._world = world;
    }
}

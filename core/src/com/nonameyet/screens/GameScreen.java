package com.nonameyet.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.nonameyet.NoNameYet;
import com.nonameyet.camera.CameraBounds;
import com.nonameyet.maps.MapManager;
import com.nonameyet.ui.PlayerHUD;

import static com.nonameyet.utils.Constants.FIXED_TIME_STEP;
import static com.nonameyet.utils.Constants.PPM;

public class GameScreen extends AbstractScreen {
    private final String TAG = this.getClass().getSimpleName();

    private final MapManager mapMgr;

    private OrthogonalTiledMapRenderer mapRenderer = null;

    private OrthographicCamera camera = null;
    protected OrthographicCamera hudCamera = null;
    private CameraBounds cameraBounds;

    //Box2d
    private World world;
    private Box2DDebugRenderer _b2dr;

    private PlayerHUD playerHUD;

    public GameScreen(NoNameYet game) {
        super(game);
        mapMgr = new MapManager(this);
    }

    @Override
    public void show() {
        createCameras();

        if (mapRenderer == null) {
            mapRenderer = new OrthogonalTiledMapRenderer(mapMgr.getCurrentTiledMap(), 1 / PPM);
        }

        playerHUD = new PlayerHUD(hudCamera, this);

        createInputMultiplexer();

        //allows for debug lines of our box2d _world.
        _b2dr = new Box2DDebugRenderer();
    }

    private void createCameras() {
        //_camera setup
        setupViewport(16, 9, TAG);

        //get the current size
        camera = new OrthographicCamera();
        camera.setToOrtho(false, VIEWPORT.viewportWidth, VIEWPORT.viewportHeight);

        hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false, VIEWPORT.viewportWidth, VIEWPORT.viewportHeight);

        cameraBounds = new CameraBounds(this);
    }

    private void createInputMultiplexer() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(playerHUD.getStage());
        Gdx.input.setInputProcessor(multiplexer);
    }


    @Override
    public void render(float delta) {
        //separate our update logic from render
        update(delta);

        clearGameScreen();

        //render our game maps
        mapRenderer.render();

        //_mapRenderer our Box2DDebugLines
        _b2dr.render(world, camera.combined);

        batchRender(delta);

    }

    private void update(float delta) {
        // handle user input first
        mapMgr.getPlayer().input();

        //takes 1 step in the physics simulation(60 times per second)
        world.step(FIXED_TIME_STEP, 6, 2);

        if (mapMgr.isMapChanged()) {
            mapMgr.loadMap(mapMgr.getCurrentMapType());
            mapRenderer.setMap(mapMgr.getCurrentTiledMap());
        }

        mapMgr.getPlayer().update(delta);
        mapMgr.updateEntities(delta);

        // update our _camera with correct coordinates after changes
        camera.update();
        cameraBounds.cameraCornerBounds();

        //tell our _mapRenderer to draw only what our _camera can see in our game _world.
        mapRenderer.setView(camera);
    }

    private void clearGameScreen() {
        // Clear the game screen
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mapRenderer.getBatch().enableBlending();
        mapRenderer.getBatch().setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    private void batchRender(float delta) {
        mapRenderer.getBatch().setProjectionMatrix(camera.combined);
        mapRenderer.getBatch().begin();
        Gdx.graphics.setTitle("NoNameYet | fps: " + Gdx.graphics.getFramesPerSecond());
        mapMgr.drawEntities(mapRenderer.getBatch());
        mapMgr.getPlayer().draw(mapRenderer.getBatch());
        mapRenderer.getBatch().end();

        playerHUD.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        setupViewport(16, 9, TAG);
        camera.setToOrtho(false, VIEWPORT.viewportWidth, VIEWPORT.viewportHeight);
        playerHUD.resize((int) VIEWPORT.physicalWidth, (int) VIEWPORT.physicalHeight);
    }

    @Override
    public void dispose() {
        //dispose of all our opened resources

        if (mapRenderer != null) {
            mapRenderer.dispose();
        }
        world.dispose();
        _b2dr.dispose();
    }


    public MapManager getMapMgr() {
        return mapMgr;
    }

    public OrthogonalTiledMapRenderer getMapRenderer() {
        return mapRenderer;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}

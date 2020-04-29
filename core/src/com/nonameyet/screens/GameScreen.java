package com.nonameyet.screens;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.nonameyet.NoNameYet;
import com.nonameyet.ecs.ECSEngine;
import com.nonameyet.ecs.systems.PhysicsDebugSystem;
import com.nonameyet.ecs.systems.PhysicsSystem;
import com.nonameyet.ecs.systems.RenderingSystem;
import com.nonameyet.input.InputManager;
import com.nonameyet.maps.MapManager;
import com.nonameyet.ui.PlayerHUD;

import static com.nonameyet.utils.Constants.PPM;

public class GameScreen extends AbstractScreen {
    private final String TAG = this.getClass().getSimpleName();

    private MapManager mapMgr;

    private OrthogonalTiledMapRenderer mapRenderer = null;

    private OrthographicCamera camera = null;
    protected OrthographicCamera hudCamera = null;

    //Box2d
    private World world;

    // Lights2d
    RayHandler rayHandler;

    // ecs
    ECSEngine ecsEngine;

    private PlayerHUD playerHUD;

    public GameScreen(NoNameYet game) {
        super(game);
    }

    @Override
    public void show() {
        // 1. cams
        createCameras();

        // 2. HUD
        playerHUD = new PlayerHUD(hudCamera, this);

        // 3. concat inputs
        createInputMultiplexer();

        // 4. //create a ECS engine
        ecsEngine = new ECSEngine(this);

        // 5. create map with ecs components (Player, Npc, Torch, Chest)
        mapMgr = new MapManager(this);
        if (mapRenderer == null) {
            mapRenderer = new OrthogonalTiledMapRenderer(mapMgr.getCurrentTiledMap(), 1 / PPM);
        }

        ecsEngine.addSystem(new RenderingSystem(this));
        ecsEngine.addSystem(new PhysicsSystem(world));
        ecsEngine.addSystem(new PhysicsDebugSystem(world, camera));

        mapMgr.createEntites();
    }

    private void createCameras() {
        //_camera setup
        setupViewport(16, 9, TAG);

        //get the current size
        camera = new OrthographicCamera();
        camera.setToOrtho(false, VIEWPORT.viewportWidth, VIEWPORT.viewportHeight);

        hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false, VIEWPORT.viewportWidth, VIEWPORT.viewportHeight);
    }

    private void createInputMultiplexer() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(playerHUD.getStage());
        multiplexer.addProcessor(InputManager.getInstance());
        Gdx.input.setInputProcessor(multiplexer);
    }


    @Override
    public void render(float delta) {
        clearGameScreen();
        //separate our update logic from render
        update(delta);
        //render our game maps
        mapRenderer.render();
        ecsEngine.update(delta);
        rayHandler.render();
        playerHUD.render(delta);
    }

    private void clearGameScreen() {
        // Clear the game screen
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void update(float delta) {
        rayHandler.update();

        if (mapMgr.isMapChanged()) {
            mapMgr.loadMap(mapMgr.getCurrentMapType());
            mapRenderer.setMap(mapMgr.getCurrentTiledMap());
        }
        mapRenderer.setView(camera);
        rayHandler.setCombinedMatrix(camera);
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
//        _b2dr.dispose();
        rayHandler.dispose();
    }

    public MapManager getMapMgr() {
        return mapMgr;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public ECSEngine getEcsEngine() {
        return ecsEngine;
    }

    public OrthogonalTiledMapRenderer getMapRenderer() {
        return mapRenderer;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public RayHandler getRayHandler() {
        return rayHandler;
    }

    public void setRayHandler(RayHandler rayHandler) {
        this.rayHandler = rayHandler;
    }

    public PlayerHUD getPlayerHUD() {
        return playerHUD;
    }
}

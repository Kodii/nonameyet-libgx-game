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
import com.nonameyet.ecs.systems.PlayerCameraSystem;
import com.nonameyet.ecs.systems.RenderingSystem;
import com.nonameyet.input.InputManager;
import com.nonameyet.maps.MapManager;
import com.nonameyet.ui.PlayerHUD;
import com.nonameyet.utils.ItemsDropper;

import static com.nonameyet.utils.Constants.PPM;

public class GameScreen extends AbstractScreen {
    private final String TAG = this.getClass().getSimpleName();

    private MapManager mapMgr;

    private OrthogonalTiledMapRenderer mapRenderer;

    private OrthographicCamera camera;
    protected OrthographicCamera hudCamera;

    //Box2d
    private World world;

    // Lights2d
    RayHandler rayHandler;

    // ecs
    ECSEngine ecsEngine;
    private RenderingSystem renderingSystem;
    public PhysicsSystem physicsSystem;
    public PhysicsDebugSystem physicsDebugSystem;
    private PlayerCameraSystem playerCameraSystem;

    private PlayerHUD playerHUD;

    private ItemsDropper itemsDropper;

    public GameScreen(NoNameYet game) {
        super(game);

        itemsDropper = new ItemsDropper(this);
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

        // 5. create map
        mapMgr = new MapManager(this);
        if (mapRenderer == null) {
            mapRenderer = new OrthogonalTiledMapRenderer(mapMgr.getCurrentTiledMap(), 1 / PPM);
        }

        // 6. create system & ecs components (Player, Npc, Torch, Chest)
        this.createSystems();
        mapMgr.createEntites();
    }

    private void createCameras() {
        //_camera setup
        setupViewport(16, 9, TAG);

        //get the current size
        camera = new OrthographicCamera();

        hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false, 16, 9);
    }

    private void createInputMultiplexer() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(playerHUD.getStage());
        multiplexer.addProcessor(InputManager.getInstance());
        Gdx.input.setInputProcessor(multiplexer);
    }

    private void createSystems() {
        renderingSystem = new RenderingSystem(this);
        physicsSystem = new PhysicsSystem(world);
        physicsDebugSystem = new PhysicsDebugSystem(world, camera);
        playerCameraSystem = new PlayerCameraSystem(this);
        ecsEngine.addSystem(physicsSystem);
        ecsEngine.addSystem(physicsDebugSystem);
        ecsEngine.addSystem(renderingSystem);
        ecsEngine.addSystem(playerCameraSystem);
    }

    public void removeSystems() {
        ecsEngine.removeSystem(renderingSystem);
        ecsEngine.removeSystem(physicsSystem);
        ecsEngine.removeSystem(physicsDebugSystem);
        ecsEngine.removeSystem(playerCameraSystem);
    }


    @Override
    public void render(float delta) {
        // Clear the game screen
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (mapMgr.isMapChanged()) {

            mapMgr.loadMap(mapMgr.getCurrentMapType());
            mapRenderer.setMap(mapMgr.getCurrentTiledMap());

            this.createSystems();

            mapMgr.createEntites();
        }
        //render our game maps
        mapRenderer.setView(camera);
        mapRenderer.render();

        ecsEngine.update(delta);

        rayHandler.setCombinedMatrix(camera);
        rayHandler.updateAndRender();

        playerHUD.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        setupViewport(16, 9, TAG);
        camera.setToOrtho(false, VIEWPORT.virtualWidth, VIEWPORT.virtualHeight);
    }

    @Override
    public void dispose() {
        //dispose of all our opened resources

        if (mapRenderer != null) {
            mapRenderer.dispose();
        }
        world.dispose();
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

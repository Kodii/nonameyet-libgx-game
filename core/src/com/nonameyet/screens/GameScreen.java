package com.nonameyet.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.nonameyet.NoNameYet;
import com.nonameyet.maps.MapManager;
import com.nonameyet.sprites.Player;
import com.nonameyet.ui.PlayerHUD;

import static com.nonameyet.utils.Constants.FIXED_TIME_STEP;
import static com.nonameyet.utils.Constants.PPM;

public class GameScreen extends AbstractScreen {
    private final String TAG = this.getClass().getSimpleName();
    private InputMultiplexer multiplexer;

    private OrthogonalTiledMapRenderer mapRenderer = null;
    private MapManager mapMgr;
    private OrthographicCamera camera = null;
    protected OrthographicCamera hudCamera = null;

    //Box2d
    private World world;
    private Box2DDebugRenderer _b2dr;

    private Player player;
    private PlayerHUD playerHUD;

    public GameScreen(NoNameYet game) {
        super(game);
        mapMgr = new MapManager(this);
    }

    @Override
    public void show() {

        //_camera setup
        setupViewport(16, 9, TAG);

        //get the current size
        camera = new OrthographicCamera();
        camera.setToOrtho(false, VIEWPORT.viewportWidth, VIEWPORT.viewportHeight);

        hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false, VIEWPORT.viewportWidth, VIEWPORT.viewportHeight);

        if (mapRenderer == null) {
            mapRenderer = new OrthogonalTiledMapRenderer(mapMgr.getCurrentTiledMap(), 1 / PPM);
        }

        //allows for debug lines of our box2d _world.
        _b2dr = new Box2DDebugRenderer();

        // create _player in our game _world
        player = new Player(this);
        playerHUD = new PlayerHUD(hudCamera, this);

        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(playerHUD.getStage());
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        //separate our update logic from render
        update(delta);

        // Clear the game screen
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mapRenderer.getBatch().enableBlending();
        mapRenderer.getBatch().setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        //render our game maps
        mapRenderer.render();

        //_mapRenderer our Box2DDebugLines
//        _b2dr.render(world, camera.combined);

        mapRenderer.getBatch().setProjectionMatrix(camera.combined);
        mapRenderer.getBatch().begin();
        Gdx.graphics.setTitle("NoNameYet | fps: " + Gdx.graphics.getFramesPerSecond());
        player.draw(mapRenderer.getBatch());
        getMapMgr().drawEntities(mapRenderer.getBatch());

        mapRenderer.getBatch().end();

        playerHUD.render(delta);

    }

    private void update(float delta) {
        // handle user input first
        player.input();

        //takes 1 step in the physics simulation(60 times per second)
        world.step(FIXED_TIME_STEP, 6, 2);

        if (mapMgr.isMapChanged()) {
            mapMgr.loadMap(mapMgr.getCurrentMapType());
            mapRenderer.setMap(mapMgr.getCurrentTiledMap());

            player = new Player(this);
            getMapMgr().createEntities();
        }

        player.update(delta);
        getMapMgr().updateEntities(delta);

        fixBounds();

        // update our _camera with correct coordinates after changes
        camera.update();

        //tell our _mapRenderer to draw only what our _camera can see in our game _world.
        mapRenderer.setView(camera);
    }

    /**
     * keep camera within bounds of TiledMap
     */
    private void fixBounds() {
        MapProperties prop = mapRenderer.getMap().getProperties();

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
        float cameraHalfWidth = camera.viewportWidth * .5f;
        float cameraHalfHeight = camera.viewportHeight * .5f;

        // Move camera after player as normal
        //attach our gamecam to our players.x coordinate
        camera.position.x = player.b2body.getPosition().x;
        camera.position.y = player.b2body.getPosition().y;

        float cameraLeft = camera.position.x - cameraHalfWidth;
        float cameraRight = camera.position.x + cameraHalfWidth;
        float cameraBottom = camera.position.y - cameraHalfHeight;
        float cameraTop = camera.position.y + cameraHalfHeight;

        // Horizontal axis
        if (mapRenderer.getViewBounds().getWidth() < camera.viewportWidth) {
            camera.position.x = mapRight / 2;
        } else if (cameraLeft <= mapLeft) {
            camera.position.x = mapRight / 2;
            camera.position.x = mapLeft + cameraHalfWidth;
        } else if (cameraRight >= mapRight) {
            camera.position.x = mapRight / 2;
            camera.position.x = mapRight - cameraHalfWidth;
        }

        // Vertical axis
        if (mapRenderer.getViewBounds().getHeight() < camera.viewportHeight) {
            camera.position.y = mapTop / 2;
        } else if (cameraBottom <= mapBottom) {
            camera.position.y = mapBottom + cameraHalfHeight;
        } else if (cameraTop >= mapTop) {
            camera.position.y = mapTop - cameraHalfHeight;
        }
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

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public PlayerHUD getPlayerHUD() {
        return playerHUD;
    }
}

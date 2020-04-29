package com.nonameyet.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.nonameyet.ecs.components.BodyComponent;
import com.nonameyet.ecs.components.PlayerComponent;
import com.nonameyet.screens.GameScreen;

import static com.nonameyet.ecs.ComponentMappers.bodyCmpMapper;
import static com.nonameyet.utils.Constants.PPM;

public class PlayerCameraSystem extends IteratingSystem {

    private final GameScreen screen;
    private final OrthographicCamera gameCamera;

    public PlayerCameraSystem(GameScreen screen) {
        super(Family.all(PlayerComponent.class, BodyComponent.class).get());

        this.screen = screen;
        gameCamera = screen.getCamera();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Vector2 position = bodyCmpMapper.get(entity).body.getPosition();
        gameCamera.update();
        cameraCornerBounds(position);

    }

    public void cameraCornerBounds(Vector2 position) {
        MapProperties prop = screen.getMapRenderer().getMap().getProperties();

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
        float cameraHalfWidth = screen.getCamera().viewportWidth * .5f;
        float cameraHalfHeight = screen.getCamera().viewportHeight * .5f;

        // Move camera after player as normal
        //attach our gamecam to our players.x coordinate
        screen.getCamera().position.x = position.x;
        screen.getCamera().position.y = position.y;

        float cameraLeft = screen.getCamera().position.x - cameraHalfWidth;
        float cameraRight = screen.getCamera().position.x + cameraHalfWidth;
        float cameraBottom = screen.getCamera().position.y - cameraHalfHeight;
        float cameraTop = screen.getCamera().position.y + cameraHalfHeight;

        // Horizontal axis
        if (screen.getMapRenderer().getViewBounds().getWidth() < screen.getCamera().viewportWidth) {
            screen.getCamera().position.x = mapRight / 2;
        } else if (cameraLeft <= mapLeft) {
            screen.getCamera().position.x = mapRight / 2;
            screen.getCamera().position.x = mapLeft + cameraHalfWidth;
        } else if (cameraRight >= mapRight) {
            screen.getCamera().position.x = mapRight / 2;
            screen.getCamera().position.x = mapRight - cameraHalfWidth;
        }

        // Vertical axis
        if (screen.getMapRenderer().getViewBounds().getHeight() < screen.getCamera().viewportHeight) {
            screen.getCamera().position.y = mapTop / 2;
        } else if (cameraBottom <= mapBottom) {
            screen.getCamera().position.y = mapBottom + cameraHalfHeight;
        } else if (cameraTop >= mapTop) {
            screen.getCamera().position.y = mapTop - cameraHalfHeight;
        }
    }
}

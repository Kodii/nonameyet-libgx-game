package com.nonameyet.camera;

import com.badlogic.gdx.maps.MapProperties;
import com.nonameyet.screens.GameScreen;

import static com.nonameyet.utils.Constants.PPM;

/**
 * keep camera within bounds of TiledMap
 */
public class CameraBounds {
    private final String TAG = this.getClass().getSimpleName();

    private final GameScreen screen;

    public CameraBounds(GameScreen screen) {
        this.screen = screen;
    }

    public void cameraCornerBounds() {
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
        screen.getCamera().position.x = screen.getMapMgr().getPlayer().b2body.getPosition().x;
        screen.getCamera().position.y = screen.getMapMgr().getPlayer().b2body.getPosition().y;

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

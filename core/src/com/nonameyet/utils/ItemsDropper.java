package com.nonameyet.utils;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.nonameyet.ecs.entities.items.Apple;
import com.nonameyet.ecs.entities.items.TrainingSword;
import com.nonameyet.input.GameKeyInputListener;
import com.nonameyet.input.GameKeys;
import com.nonameyet.input.InputManager;
import com.nonameyet.screens.GameScreen;

import static com.nonameyet.utils.Constants.PPM;

public class ItemsDropper implements GameKeyInputListener {
    private final String TAG = this.getClass().getSimpleName();
    private final GameScreen screen;

    public ItemsDropper(GameScreen screen) {
        this.screen = screen;

        InputManager.getInstance().addInputListener(this);
    }

    public void dropSword() {
        System.out.println("drop sword");
        Rectangle playerPositionPoint = screen.getMapMgr().getPlayerLayer().getObjects().getByType(RectangleMapObject.class).get(0).getRectangle();
        new TrainingSword(screen.getEcsEngine(), new Vector2(playerPositionPoint.getX() / PPM, playerPositionPoint.getY() / PPM));
    }

    public void dropApple() {
        System.out.println("drop apple");
        Rectangle playerPositionPoint = screen.getMapMgr().getPlayerLayer().getObjects().getByType(RectangleMapObject.class).get(0).getRectangle();
        new Apple(screen.getEcsEngine(), new Vector2(playerPositionPoint.getX() / PPM, playerPositionPoint.getY() / PPM));
    }

    @Override
    public void keyPressed(InputManager inputManager, GameKeys key) {
        switch (key) {
            case DROP_SWORD:
                dropSword();
                break;
            case DROP_APPLE:
                dropApple();
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(InputManager inputManager, GameKeys key) {

    }
}

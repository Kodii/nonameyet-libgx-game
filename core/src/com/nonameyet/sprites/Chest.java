package com.nonameyet.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.nonameyet.assets.AssetName;
import com.nonameyet.assets.Assets;
import com.nonameyet.b2d.BodyBuilder;
import com.nonameyet.screens.GameScreen;
import com.nonameyet.ui.chest.ChestWindowEvent;
import com.nonameyet.utils.Collision;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static com.nonameyet.utils.Constants.PPM;

public class Chest extends Sprite implements Disposable, PropertyChangeListener {
    private final String TAG = this.getClass().getSimpleName();
    private GameScreen screen;

    public enum State {
        CHEST_CLOSE_IDLE,
        CHEST_OPEN_IDLE,
        CHEST_OPEN,
        CHEST_CLOSE,
    }

    private State currentState = State.CHEST_CLOSE_IDLE;
    private State previousState = State.CHEST_CLOSE_IDLE;

    private World world;
    public Body b2body;

    private TextureRegion openIdle;
    private TextureRegion closeIdle;
    private Animation<TextureRegion> openAnimation;
    private Animation<TextureRegion> closeAnimation;
    private float stateTime = 0;

    private static TextureAtlas textureAtlas = Assets.manager.get(AssetName.CHEST_ATLAS.getAssetName());

    public Chest(GameScreen screen) {
        super(textureAtlas.findRegion("chest_grass"));
        this.screen = screen;
        this.world = screen.getWorld();

        closeIdle = new TextureRegion(getTexture(), 0, 0, 19, 16);
        openIdle = new TextureRegion(getTexture(), 38, 0, 19, 16);
        setBounds(0, 0, 19 / PPM, 16 / PPM);
        setRegion(openIdle);
        createAnimation();

        b2body = BodyBuilder.staticPointBody(
                world,
                chestPosition(),
                new Vector2(closeIdle.getRegionWidth(), closeIdle.getRegionHeight()),
                "CHEST",
                Collision.OBJECT);

        // listeners
        screen.getMapMgr().getWorldContactListener().addPropertyChangeListener(this);

        Gdx.app.debug(TAG, "Chest was created");
    }

    private void createAnimation() {
        openAnimation = new Animation<TextureRegion>(0.15f, textureAtlas.findRegions("chest_grass"));
        closeAnimation = new Animation<TextureRegion>(0.15f, textureAtlas.findRegions("chest_grass"), Animation.PlayMode.REVERSED);
    }

    public void update(float dt) {
        input();

        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));

    }

    public void input() {
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
            currentState = State.CHEST_OPEN;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
            currentState = State.CHEST_CLOSE;
        }

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Gdx.app.debug(TAG, "Chest --> propertyChange(): " + evt.getPropertyName() + ", getNewValue(): " + evt.getNewValue());

        if (evt.getPropertyName().equals(ChestWindowEvent.class.getSimpleName())) {
            chestWindowEvent((ChestWindowEvent) evt.getNewValue());
        }
    }

    public void chestWindowEvent(ChestWindowEvent event) {

        switch (event) {
            case CHEST_OPENED:
                currentState = State.CHEST_OPEN;
                break;
            case CHEST_CLOSED:
                currentState = State.CHEST_CLOSE;
                break;
        }
    }

    private TextureRegion getFrame(float dt) {
        TextureRegion region;

        stateTime = currentState == previousState ? stateTime + dt : 0;
        previousState = currentState;

        switch (currentState) {
            case CHEST_OPEN:
                region = openAnimation.getKeyFrame(stateTime);
                break;
            case CHEST_CLOSE:
                region = closeAnimation.getKeyFrame(stateTime);
                break;
            case CHEST_OPEN_IDLE:
            case CHEST_CLOSE_IDLE:
            default:
                region = closeIdle;
                break;
        }

        return region;
    }

    private Vector2 chestPosition() {
        Rectangle playerPositionPoint = screen.getMapMgr().getChestSpawnLayer().getObjects().getByType(RectangleMapObject.class).get(0).getRectangle();
        return new Vector2(playerPositionPoint.getX() / PPM, playerPositionPoint.getY() / PPM);
    }

    @Override
    public void dispose() {
        screen.getMapMgr().getWorldContactListener().removePropertyChangeListener(this);
    }

}

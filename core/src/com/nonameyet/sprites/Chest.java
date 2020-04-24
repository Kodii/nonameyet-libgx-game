package com.nonameyet.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.nonameyet.assets.AssetName;
import com.nonameyet.assets.Assets;
import com.nonameyet.screens.GameScreen;
import com.nonameyet.ui.chest.ChestWindowEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static com.nonameyet.utils.Constants.PPM;

public class Chest extends Sprite implements PropertyChangeListener {
    private final String TAG = this.getClass().getSimpleName();
    private GameScreen screen;

    private static final int FRAME_COLS = 3, FRAME_ROWS = 1;

    public enum State {
        CHEST_IDLE,
        CHEST_OPEN,
        CHEST_CLOSE,
    }

    private State currentState = State.CHEST_IDLE;
    private State previousState = State.CHEST_IDLE;

    private World world;
    public Body b2body;

    private TextureRegion idle;
    private Animation<TextureRegion> open;
    private Animation<TextureRegion> close;
    private float stateTimer = 0;

    public Chest(GameScreen screen) {
        super((Texture) Assets.manager.get(AssetName.CHEST_PNG.getAssetName()));
        this.screen = screen;
        this.world = screen.getWorld();

        createIdle();
        createAnimation();

        defineChest();
        setBounds(0, 0, (getTexture().getWidth() / FRAME_COLS) / PPM, (getTexture().getHeight() / FRAME_ROWS) / PPM);
        setRegion(idle);

        // listeners
        screen.getMapMgr().getWorldContactListener().addPropertyChangeListener(this);

        Gdx.app.debug(TAG, "Chest was created");
    }

    private void createIdle() {
        int frameWidth = getTexture().getWidth() / FRAME_COLS;
        int frameHeight = getTexture().getHeight() / FRAME_ROWS;

        Gdx.app.debug(TAG, "frameWidth: " + getTexture().getWidth());
        Gdx.app.debug(TAG, "frameHeight: " + getTexture().getHeight());
        TextureRegion[][] temp = TextureRegion.split(getTexture(), frameWidth, frameHeight);
        idle = new TextureRegion(getTexture(), temp[0][0].getRegionX(), temp[0][0].getRegionY(), frameWidth, frameHeight);
    }

    private void createAnimation() {
        int frameWidth = getTexture().getWidth() / FRAME_COLS;
        int frameHeight = getTexture().getHeight() / FRAME_ROWS;
        float frameDuration = 0.25f;

        TextureRegion[][] temp = TextureRegion.split(getTexture(), frameWidth, frameHeight);

        Array<TextureRegion> frames = new Array<>();
        for (int c = 1; c < FRAME_COLS; c++)
            frames.add(temp[0][c]);
        open = new Animation<>(frameDuration, frames, Animation.PlayMode.NORMAL);
        frames.clear();

        for (int c = 0; c < FRAME_COLS; c++)
            frames.add(temp[0][c]);
        close = new Animation<>(frameDuration, frames, Animation.PlayMode.REVERSED);
        frames.clear();
    }

    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));

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
        switch (currentState) {
            case CHEST_OPEN:
                region = open.getKeyFrame(stateTimer);
                break;
            case CHEST_CLOSE:
                region = close.getKeyFrame(stateTimer);
                break;
            default:
                region = idle;
                break;
        }
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;

        return region;
    }

    private void defineChest() {
        float x = idle.getRegionWidth() / PPM;
        float y = idle.getRegionHeight() / PPM;

        BodyDef bdef = new BodyDef();
        bdef.position.set(chestPosition().x, chestPosition().y);

        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);
        b2body.setUserData("CHEST");

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(x / 2, y / 2);

        fdef.shape = shape;
        b2body.createFixture(fdef);

        shape.dispose();
    }

    private Vector2 chestPosition() {
        Rectangle playerPositionPoint = screen.getMapMgr().getChestSpawnLayer().getObjects().getByType(RectangleMapObject.class).get(0).getRectangle();
        Vector2 vector2 = new Vector2(playerPositionPoint.getX() / PPM, playerPositionPoint.getY() / PPM);
        return vector2;
    }

}

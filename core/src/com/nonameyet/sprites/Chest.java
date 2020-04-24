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

import static com.nonameyet.utils.Constants.PPM;

public class Chest extends Sprite {
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
        super((Texture) Assets.manager.get(AssetName.PLAYER_PNG.getAssetName()));
        this.screen = screen;
        this.world = screen.getWorld();

        createIdle();
        createAnimation();

        defineChest();
        setBounds(0, 0, 16 / PPM, 16 / PPM);
        setRegion(idle);

        Gdx.app.debug(TAG, "Chest was created");
    }

    private void createIdle() {
        int frameWidth = getTexture().getWidth() / FRAME_COLS;
        int frameHeight = getTexture().getHeight() / FRAME_ROWS;

        Gdx.app.debug(TAG, "frameWidth: " + frameWidth);
        Gdx.app.debug(TAG, "frameHeight: " + frameHeight);
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

        for (int c = 1; c < FRAME_COLS; c++)
            frames.add(temp[1][c]);
        close = new Animation<>(frameDuration, frames, Animation.PlayMode.REVERSED);
        frames.clear();
    }

    public void update(float dt) {
        float w = b2body.getPosition().x;
        float h = b2body.getPosition().y;
//        Gdx.app.debug(TAG, "w: " + w + "h: " + h);
        setPosition(b2body.getPosition().x, b2body.getPosition().y);
//        setRegion(getFrame(dt));

    }

    private TextureRegion getFrame(float dt) {
        currentState = State.CHEST_IDLE;
        TextureRegion region;
        switch (currentState) {
            // run
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

        BodyDef bdef = new BodyDef();
        bdef.position.set(chestPosition());

        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);
        b2body.setUserData("CHEST");

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(10 / PPM, 7 / PPM);

        fdef.shape = shape;
        b2body.createFixture(fdef);

        shape.dispose();
    }

    private Vector2 chestPosition() {
        Rectangle playerPositionPoint = screen.getMapMgr().getChestSpawnLayer().getObjects().getByType(RectangleMapObject.class).get(0).getRectangle();
        Vector2 vector2 = new Vector2(playerPositionPoint.getX() / PPM, playerPositionPoint.getY() / PPM);
        Gdx.app.debug(TAG, "chestPosition: " + vector2);
        return vector2;
    }

}

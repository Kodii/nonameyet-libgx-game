package com.nonameyet.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.nonameyet.assets.AssetName;
import com.nonameyet.assets.Assets;
import com.nonameyet.b2d.BodyBuilder;
import com.nonameyet.input.GameKeyInputListener;
import com.nonameyet.input.GameKeys;
import com.nonameyet.input.InputManager;
import com.nonameyet.screens.GameScreen;
import com.nonameyet.utils.Collision;

import static com.nonameyet.utils.Constants.PPM;

public class Player extends Sprite implements GameKeyInputListener {
    private final String TAG = this.getClass().getSimpleName();
    private final GameScreen screen;

    private static final int FRAME_COLS = 4, FRAME_ROWS = 4;

    public enum State {
        STANDING_LEFT,
        STANDING_RIGHT,
        STANDING_UP,
        STANDING_DOWN,
        RUNNING_LEFT,
        RUNNING_RIGHT,
        RUNNING_UP,
        RUNNING_DOWN
    }

    private State currentState = State.STANDING_DOWN;
    private State previousState = State.STANDING_DOWN;

    public Body b2body;

    private TextureRegion standUp;
    private TextureRegion standDown;
    private TextureRegion standLeft;
    private TextureRegion standRight;

    private Animation<TextureRegion> runUp;
    private Animation<TextureRegion> runDown;
    private Animation<TextureRegion> runLeft;
    private Animation<TextureRegion> runRight;

    private float stateTimer = 0;

    // movement
    final float speed = 3;
    float speedX;
    float speedY;

    public Player(GameScreen screen) {
        super((Texture) Assets.manager.get(AssetName.PLAYER_PNG.getAssetName()));
        this.screen = screen;

        createStand();
        createRunAnimation();

        b2body = BodyBuilder.dynamicRectangleBody(
                screen.getWorld(),
                playerPosition(),
                new Vector2(standDown.getRegionWidth(), standDown.getRegionHeight()),
                "PLAYER",
                Collision.PLAYER);


        setBounds(0, 0, 16 / PPM, 16 / PPM);
        setRegion(standDown);

//        InputManager.getInstance().addInputListener(this);

        Gdx.app.debug(TAG, "Player was created");
    }

    private void createStand() {
        int frameWidth = getTexture().getWidth() / FRAME_COLS;
        int frameHeight = getTexture().getHeight() / FRAME_ROWS;
        TextureRegion[][] temp = TextureRegion.split(getTexture(), frameWidth, frameHeight);

        standDown = new TextureRegion(getTexture(), temp[0][0].getRegionX(), temp[0][0].getRegionY(), frameWidth, frameHeight);
        standLeft = new TextureRegion(getTexture(), temp[1][0].getRegionX(), temp[1][0].getRegionY(), frameWidth, frameHeight);
        standRight = new TextureRegion(getTexture(), temp[2][0].getRegionX(), temp[2][0].getRegionY(), frameWidth, frameHeight);
        standUp = new TextureRegion(getTexture(), temp[3][0].getRegionX(), temp[3][0].getRegionY(), frameWidth, frameHeight);
    }

    private void createRunAnimation() {
        int frameWidth = getTexture().getWidth() / FRAME_COLS;
        int frameHeight = getTexture().getHeight() / FRAME_ROWS;
        float frameDuration = 0.25f;

        TextureRegion[][] temp = TextureRegion.split(getTexture(), frameWidth, frameHeight);

        Array<TextureRegion> frames = new Array<>();
        for (int c = 1; c < FRAME_COLS; c++)
            frames.add(temp[0][c]);
        runDown = new Animation<>(frameDuration, frames, Animation.PlayMode.LOOP_PINGPONG);
        frames.clear();

        for (int c = 1; c < FRAME_COLS; c++)
            frames.add(temp[1][c]);
        runLeft = new Animation<>(frameDuration, frames, Animation.PlayMode.LOOP_PINGPONG);
        frames.clear();

        for (int c = 1; c < FRAME_COLS; c++)
            frames.add(temp[2][c]);
        runRight = new Animation<>(frameDuration, frames, Animation.PlayMode.LOOP_PINGPONG);
        frames.clear();

        for (int c = 1; c < FRAME_COLS; c++)
            frames.add(temp[3][c]);
        runUp = new Animation<>(frameDuration, frames, Animation.PlayMode.LOOP_PINGPONG);
        frames.clear();

    }

    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));

    }

    public void move() {
        //control our _player using immediate impulses
        b2body.applyLinearImpulse(
                (speedX - b2body.getLinearVelocity().x * b2body.getMass()),
                (speedY - b2body.getLinearVelocity().y * b2body.getMass()),
                b2body.getWorldCenter().x,
                b2body.getWorldCenter().y,
                true
        );
    }

    @Override
    public void keyPressed(InputManager inputManager, GameKeys key) {

        switch (key) {
            case UP:
                speedY = +speed;
                speedX = 0;
                break;
            case DOWN:
                speedY = -speed;
                speedX = 0;
                break;
            case LEFT:
                speedX = -speed;
                speedY = 0;
                break;
            case RIGHT:
                speedX = +speed;
                speedY = 0;
                break;
            default:
                break;

        }
    }

    @Override
    public void keyReleased(InputManager inputManager, GameKeys key) {
        switch (key) {
            case UP:
                speedY = inputManager.isKeyPressed(GameKeys.DOWN) ? -speed : 0;
                break;
            case DOWN:
                speedY = inputManager.isKeyPressed(GameKeys.UP) ? +speed : 0;
                break;
            case LEFT:
                speedX = inputManager.isKeyPressed(GameKeys.RIGHT) ? +speed : 0;
                break;
            case RIGHT:
                speedX = inputManager.isKeyPressed(GameKeys.LEFT) ? -speed : 0;
                break;
            default:
                break;

        }

    }

    private TextureRegion getFrame(float dt) {
        currentState = getState();
        TextureRegion region;
        switch (currentState) {
            // run
            case RUNNING_UP:
                region = runUp.getKeyFrame(stateTimer);
                break;
            case RUNNING_DOWN:
                region = runDown.getKeyFrame(stateTimer);
                break;
            case RUNNING_LEFT:
                region = runLeft.getKeyFrame(stateTimer);
                break;
            case RUNNING_RIGHT:
                region = runRight.getKeyFrame(stateTimer);
                break;
            // stand
            case STANDING_UP:
                region = standUp;
                break;
            case STANDING_LEFT:
                region = standLeft;
                break;
            case STANDING_RIGHT:
                region = standRight;
                break;
            case STANDING_DOWN:
            default:
                region = standDown;
                break;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;

        return region;
    }

    private State getState() {
        // run
        if (b2body.getLinearVelocity().y > 0)
            return State.RUNNING_UP;

        if (b2body.getLinearVelocity().y < 0)
            return State.RUNNING_DOWN;

        if (b2body.getLinearVelocity().x < 0)
            return State.RUNNING_LEFT;

        if (b2body.getLinearVelocity().x > 0)
            return State.RUNNING_RIGHT;

        boolean isStand = b2body.getLinearVelocity().y == 0 && b2body.getLinearVelocity().x == 0;
        // stand
        if (isStand && previousState == State.RUNNING_UP)
            return State.STANDING_UP;
        if (isStand && previousState == State.RUNNING_DOWN)
            return State.STANDING_DOWN;
        if (isStand && previousState == State.RUNNING_LEFT)
            return State.STANDING_LEFT;
        if (isStand && previousState == State.RUNNING_RIGHT)
            return State.STANDING_RIGHT;
        if (previousState == currentState)
            return previousState;
        else
            return State.STANDING_DOWN;
    }

    private Vector2 playerPosition() {
        Rectangle playerPositionPoint = screen.getMapMgr().getPlayerSpawnLayer().getObjects().getByType(RectangleMapObject.class).get(0).getRectangle();
        return new Vector2(playerPositionPoint.getX() / PPM, playerPositionPoint.getY() / PPM);
    }
}

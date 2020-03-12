package com.nonameyet.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.nonameyet.environment.AssetName;
import com.nonameyet.environment.Assets;
import com.nonameyet.screens.GameScreen;

import static com.nonameyet.utils.Constants.PPM;

public class Player extends Sprite {
    private static final String TAG = Player.class.getSimpleName();

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

    private World world;
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

    public Player(GameScreen screen) {
        super((Texture) Assets.manager.get(AssetName.PLAYER_PNG.getAssetName()));
        this.world = screen.getWorld();

        createStand();
        createRunAnimation();

        definePlayer();
        setBounds(0, 0, 16 / PPM, 16 / PPM);
        setRegion(standDown);

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

    public void input() {
        final float speed = 3;

        // movement
        final float speedX;
        final float speedY;

        if (Gdx.input.isKeyPressed(Input.Keys.A)) speedX = -speed;
        else if (Gdx.input.isKeyPressed(Input.Keys.D)) speedX = +speed;
        else speedX = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.S)) speedY = -speed;
        else if (Gdx.input.isKeyPressed(Input.Keys.W)) speedY = +speed;
        else speedY = 0;

        //control our _player using immediate impulses
        b2body.applyLinearImpulse(
                (speedX - b2body.getLinearVelocity().x * b2body.getMass()),
                (speedY - b2body.getLinearVelocity().y * b2body.getMass()),
                b2body.getWorldCenter().x,
                b2body.getWorldCenter().y,
                true
        );
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

    private void definePlayer() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(12.52f, 5.78f);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
        b2body.setUserData("PLAYER");

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(6 / PPM, 8 / PPM);

        fdef.shape = shape;
        b2body.createFixture(fdef);

        shape.dispose();
    }
}

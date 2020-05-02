package com.nonameyet.ui.clock;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Disposable;
import com.nonameyet.assets.AssetName;
import com.nonameyet.audio.AudioManager;
import com.nonameyet.events.DayTimeEvent;
import com.nonameyet.preferences.PlayerPref;
import com.nonameyet.screens.GameScreen;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ClockUI extends Label implements Disposable {
    private final String TAG = this.getClass().getSimpleName();

    static Label.LabelStyle labelStyle;

    static {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(AssetName.PIXEL_FONT.getAssetName()));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 23;
        BitmapFont bitmapFont = generator.generateFont(parameter);

        generator.dispose(); // don't forget to dispose to avoid memory leaks!

        labelStyle = new Label.LabelStyle();
        labelStyle.font = bitmapFont;
        labelStyle.fontColor = Color.WHITE;
    }

    private final GameScreen screen;

    private static final String FORMAT = "%02d:%02d";
    private float totalTime = 0;
    private float rateOfTime = 1;

    DayTimeEvent previousStateOfDay = null;
    DayTimeEvent currentStateOfDay = null;

    // events
    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);

    public ClockUI(GameScreen screen, CharSequence text) {
        super(text, labelStyle);
        this.screen = screen;
        init();

        addPropertyChangeListener(AudioManager.getInstance());
//        addPropertyChangeListener(screen.getMapMgr());
    }

    private void init() {
        String time = String.format(FORMAT, 0, 0, currentStateOfDay);
        this.setText(time);
        this.pack();
    }

    public void getCurrentStateOfDay(int hours) {
        if (hours >= 5 && hours < 8) {
            previousStateOfDay = currentStateOfDay;
            currentStateOfDay = DayTimeEvent.DAWN;
            if (currentStateOfDay != previousStateOfDay) {
                changes.firePropertyChange(DayTimeEvent.NAME, null, currentStateOfDay);
            }
        } else if (hours >= 8 && hours < 18) {
            previousStateOfDay = currentStateOfDay;
            currentStateOfDay = DayTimeEvent.AFTERNOON;
            if (currentStateOfDay != previousStateOfDay) {
                changes.firePropertyChange(DayTimeEvent.NAME, null, currentStateOfDay);
            }
        } else if (hours >= 18 && hours < 23) {
            previousStateOfDay = currentStateOfDay;
            currentStateOfDay = DayTimeEvent.DUSK;
            if (currentStateOfDay != previousStateOfDay) {
                changes.firePropertyChange(DayTimeEvent.NAME, null, currentStateOfDay);
            }
        } else {
            previousStateOfDay = currentStateOfDay;
            currentStateOfDay = DayTimeEvent.NIGHT;
            if (currentStateOfDay != previousStateOfDay) {
                Gdx.app.debug(TAG, "NIGHT EVENT !!!!!");
                changes.firePropertyChange(DayTimeEvent.NAME, null, currentStateOfDay);
            }
        }
    }

    @Override
    public void act(float delta) {
        input();

        totalTime += (delta * rateOfTime);

        int minutes = getCurrentTimeMinutes();
        int hours = getCurrentTimeHours();

        getCurrentStateOfDay(hours);

        PlayerPref.setCurrentTime(totalTime);

        String time = String.format(FORMAT, hours, minutes);
        this.setText(time);
    }

    private void input() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)) {
            PlayerPref.setCurrentTime(60 * 60 * 5);
            setTotalTime(PlayerPref.getCurrentTime());
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_7)) {
            PlayerPref.setCurrentTime(60 * 60 * 10);
            setTotalTime(PlayerPref.getCurrentTime());
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_8)) {
            PlayerPref.setCurrentTime(60 * 60 * 19);
            setTotalTime(PlayerPref.getCurrentTime());
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_9)) {
            PlayerPref.setCurrentTime(60 * 60 * 1);
            setTotalTime(PlayerPref.getCurrentTime());
        }
    }

    public int getCurrentTimeMinutes() {
        return MathUtils.floor((totalTime / 60) % 60);
    }

    public int getCurrentTimeHours() {
        return MathUtils.floor((totalTime / 3600) % 24);
    }

    public void setRateOfTime(float rateOfTime) {
        this.rateOfTime = rateOfTime;
    }

    public float getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(float totalTime) {
        this.totalTime = totalTime;
    }

    public void addPropertyChangeListener(
            PropertyChangeListener p) {
        changes.addPropertyChangeListener(p);
    }

    public void removePropertyChangeListener(
            PropertyChangeListener p) {
        changes.removePropertyChangeListener(p);
    }

    @Override
    public void dispose() {
        removePropertyChangeListener(AudioManager.getInstance());
//        removePropertyChangeListener(screen.getMapMgr());
    }
}

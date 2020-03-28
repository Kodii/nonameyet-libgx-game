package com.nonameyet.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nonameyet.NoNameYet;

public class ReleaseNotesScreen extends AbstractMenuScreen {
    private static final String TAG = ReleaseNotesScreen.class.getSimpleName();

    public ReleaseNotesScreen(NoNameYet game) {
        super(game);

        title("Release Notes");
        TextButton backButton = backButton();
        releaseNotes();

        //Listeners
        backButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                ReleaseNotesScreen.this.game.setScreen(ScreenType.MAIN_MENU);
            }
        });
    }

    private void releaseNotes() {
        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(false); // turn on all debug lines (table, cell, and widget)

        Label version = new Label("0.0.1 ", labelStyle);
        version.setFontScale(1.5f, 1.5f);
        Label date = new Label("March 28, 2020 ", labelStyle);
        Label test = new Label("Everything is fine. (We hope.) ", labelStyle);


        table.add(version).spaceBottom(30).row();
        table.add(date).spaceBottom(20).row();
        table.add(test);

        stage.addActor(table);
    }

}

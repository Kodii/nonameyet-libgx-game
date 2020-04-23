package com.nonameyet.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.nonameyet.NoNameYet;
import com.nonameyet.screens.ScreenType;

public class ReleaseNotesScreen extends AbstractMenuScreen {
    private static final String TAG = ReleaseNotesScreen.class.getSimpleName();
    private static String RELEASE_NOTES_PATH = "licenses/release-notes.txt";

    private ScrollPane scrollPane;

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
        //Get text
        FileHandle file = Gdx.files.internal(RELEASE_NOTES_PATH);
        String textString = file.readString();

        Label text = new Label(textString, labelStyle);
        text.setAlignment(Align.top | Align.center);
        text.setWrap(true);

        scrollPane = new ScrollPane(text);
        scrollPane.setSmoothScrolling(true);

        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(false); // turn on all debug lines (table, cell, and widget)

        table.defaults().height(Gdx.graphics.getHeight() / 2f);
        table.defaults().width(Gdx.graphics.getWidth());
        table.add(scrollPane);

        stage.addActor(table);
    }

}

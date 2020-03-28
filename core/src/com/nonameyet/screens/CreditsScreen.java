package com.nonameyet.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nonameyet.NoNameYet;

public class CreditsScreen extends AbstractMenuScreen {
    private static final String TAG = CreditsScreen.class.getSimpleName();

    public CreditsScreen(NoNameYet game) {
        super(game);

        title("Credits");
        TextButton backButton = backButton();
        credits();

        //Listeners
        backButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                CreditsScreen.this.game.setScreen(ScreenType.MAIN_MENU);
            }
        });
    }

    private void credits() {
        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(false); // turn on all debug lines (table, cell, and widget)

        Label creators = new Label("Creators:", labelStyle);
        creators.setFontScale(1.5f, 1.5f);
        Label daniel = new Label("Daniel Dedek", labelStyle);
        Label kordian = new Label("Kordian Stryczek", labelStyle);


        table.add(creators).colspan(2).spaceBottom(30).row();
        table.add(daniel).padRight(20);
        table.add(kordian).padLeft(20);

        stage.addActor(table);
    }
}

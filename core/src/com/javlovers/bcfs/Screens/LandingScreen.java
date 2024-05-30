package com.javlovers.bcfs.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.javlovers.bcfs.BCFS;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.javlovers.bcfs.Others.GlobalEntities;

import java.util.ArrayList;

public class LandingScreen implements Screen {
    final BCFS game;
    OrthographicCamera camera;
    Stage stage;
    ArrayList<TextButton> buttons;
    Skin skin;
    Skin customSkin;
    Texture backgroundTexture;  // Add this line

    public LandingScreen(final BCFS gam) {
        game = gam;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage); // Set the stage as the input processor

        // Load the skin
        skin = new Skin(Gdx.files.internal("tracerui/tracer-ui.json"));
        customSkin = new Skin(Gdx.files.internal("custom_ui/custom_ui.json"));


        // Load the background texture
        backgroundTexture = new Texture(Gdx.files.internal("farm.png"));
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(new Color(0.15f, 0f, 0f, 1f));

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.end();

        // Update and draw the stage
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void show() {
        buttons = new ArrayList<>();

        // Create the TextButtons
        TextButton makeButton = new TextButton("COCK UP", customSkin);
        TextButton challengeButton = new TextButton("FIGHT", customSkin);
        TextButton settingsButton = new TextButton("SETTINGS", customSkin);
        TextButton historyButton = new TextButton("HISTORY", customSkin);
        TextButton exitButton = new TextButton("EXIT", customSkin);

        buttons.add(makeButton);
        buttons.add(challengeButton);
        buttons.add(historyButton);
        buttons.add(settingsButton);
        buttons.add(exitButton);

        if(GlobalEntities.CurrentCock == null){
            challengeButton.setDisabled(true);
        }

        // Create a table and add the buttons
        Table table = new Table();
        table.setFillParent(true);

        table.center();

        Texture appLogo = new Texture(Gdx.files.internal("logo.png"));
        Image logo = new Image(appLogo);
        logo.setSize(400, 125);
        logo.setPosition(450 , 575);
        table.addActor(logo);


        // Add buttons to the table with spacing
        float buttonSpacing = 20f;
        table.add(challengeButton).width(500).height(75).padTop(100).padBottom(buttonSpacing).row();
        table.add(makeButton).width(500).height(75).padBottom(buttonSpacing).row();
        table.add(historyButton).width(500).height(75).padBottom(buttonSpacing).row();
        table.add(settingsButton).width(500).height(75).padBottom(buttonSpacing).row();
        table.add(exitButton).width(500).height(75).padBottom(buttonSpacing).row();

        // Add the table to the stage
        stage.addActor(table);

        challengeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Handle Button Click
                System.out.println("CHALLENGE");
                game.setScreen(new ChallengeScreen(game));
                dispose();
            }
        });

        makeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Handle Button Click
                System.out.println("MAKE");
                game.setScreen(new MakeCockScreen(game));
                dispose();
            }
        });

        historyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Handle Button Click
                System.out.println("HISTORY");
                game.setScreen(new HistoryScreen(game));
                dispose();
            }
        });

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Handle Button Click
                System.out.println("SETTINGS");
                game.setScreen(new SettingsScreen(game));
                dispose();
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Handle Button Click
                System.out.println("EXIT");
                Gdx.app.exit();
                dispose();
            }
        });
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose(); // Dispose of the skin
        backgroundTexture.dispose(); // Dispose of the background texture
    }
}
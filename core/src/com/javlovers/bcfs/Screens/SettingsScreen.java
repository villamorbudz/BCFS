package com.javlovers.bcfs.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.javlovers.bcfs.BCFS;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import jdk.internal.net.http.common.Log;

import java.util.ArrayList;

public class SettingsScreen implements Screen {
    final BCFS game;
    OrthographicCamera camera;
    Stage stage;
    Skin skin;
    Skin customSkin;
    Table table;
    Label accountLabel;
    Label displayNameLabel;
    Label usernameLabel;
    Label passwordLabel;
    TextField displayNameField;
    TextField usernameField;
    TextField passwordField;
    TextButton backButton;
    TextButton saveButton;
    TextButton logOutButton;
    Texture backgroundTexture;
    public SettingsScreen(final BCFS gam) {
        game = gam;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage); // Set the stage as the input processor

        backgroundTexture = new Texture(Gdx.files.internal("farm.png"));

        // Load the skin
        skin = new Skin(Gdx.files.internal("tracerui/tracer-ui.json"));
        customSkin = new Skin(Gdx.files.internal("custom_ui/custom_ui.json"));
        table = new Table();

        accountLabel = new Label("ACCOUNT SETTINGS", skin, "title");
        displayNameLabel = new Label("EDIT DISPLAY NAME", skin);
        usernameLabel = new Label("EDIT USERNAME", skin);
        passwordLabel = new Label("CHANGE PASSWORD", skin);

        displayNameField = new TextField("", customSkin);
        usernameField = new TextField("", customSkin);
        passwordField = new TextField("", customSkin);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');

        displayNameField.setAlignment(Align.center);
        usernameField.setAlignment(Align.center);
        passwordField.setAlignment(Align.center);

        backButton = new TextButton("BACK", customSkin);
        saveButton = new TextButton("SAVE", customSkin);
        logOutButton = new TextButton("LOG OUT", customSkin);
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
        table.setFillParent(true);
        table.pad(50).padTop(25).top().left();
        table.top().left();


        table.add(backButton).width(200).height(35).padBottom(35).left();
        table.row();

        table.add(accountLabel).padBottom(40).left().row();
        table.add(displayNameLabel).padBottom(10).left().row();
        table.add(displayNameField).width(350).height(45).padBottom(25).left().row();
        table.add(usernameLabel).padBottom(10).left().row();
        table.add(usernameField).width(350).height(45).padBottom(25).left().row();
        table.add(passwordLabel).padBottom(10).left().row();
        table.add(passwordField).width(350).height(45).padBottom(45).left().row();
        table.add(saveButton).padBottom(60).width(350).height(65).left().row();
        table.add(logOutButton).width(350).height(60).left().row();
        stage.addActor(table);

        // CLICK HANDLING
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Handle Button Click
                System.out.println("BACK");
                game.setScreen(new LandingScreen(game));
                dispose();
            }
        });

        logOutButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Handle Button Click
                System.out.println("LOGGING OUT...");
                game.setScreen(new LoginScreen(game));
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
    }
}
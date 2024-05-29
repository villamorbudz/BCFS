package com.javlovers.bcfs.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.javlovers.bcfs.BCFS;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import java.util.ArrayList;

public class SettingsScreen implements Screen {
    final BCFS game;
    OrthographicCamera camera;
    Stage stage;
    Skin skin;
    Table table;
    Label accountLabel;
    Label usernameLabel;
    Label passwordLabel;
    TextField usernameField;
    TextField passwordField;
    TextButton backButton;
    TextButton saveButton;
    TextButton logOutButton;
    public SettingsScreen(final BCFS gam) {
        game = gam;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage); // Set the stage as the input processor

        // Load the skin
        skin = new Skin(Gdx.files.internal("tracerui/tracer-ui.json"));

        table = new Table();

        accountLabel = new Label("ACCOUNT SETTINGS", skin, "title");
        usernameLabel = new Label("EDIT USERNAME", skin);
        passwordLabel = new Label("CHANGE PASSWORD", skin);

        usernameField = new TextField("", skin);
        passwordField = new TextField("", skin);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');

        backButton = new TextButton("BACK", skin);
        saveButton = new TextButton("SAVE", skin);
        logOutButton = new TextButton("LOG OUT", skin);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(new Color(0.15f, 0f, 0f, 1f));

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
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


        table.add(backButton).width(200).height(40).padBottom(50).left();
        table.row();

        table.add(accountLabel).padBottom(35).left().row();
        table.add(usernameLabel).padBottom(10).left().row();
        table.add(usernameField).width(350).height(45).padBottom(40).left().row();
        table.add(passwordLabel).padBottom(10).left().row();
        table.add(passwordField).width(350).height(45).padBottom(40).left().row();
        table.add(saveButton).padBottom(150).width(350).height(65).left().row();
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
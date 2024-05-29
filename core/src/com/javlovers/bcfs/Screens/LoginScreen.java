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
import com.javlovers.bcfs.Screens.BackEnd.Globals.DBHelpers;
import com.javlovers.bcfs.Screens.BackEnd.Main.User;
import sun.rmi.server.UnicastServerRef;

import java.util.ArrayList;

public class LoginScreen implements Screen {
    final BCFS game;
    OrthographicCamera camera;
    Stage stage;
    Skin skin;
    Label loginLabel;
    Label serverMessage;
    Table table;
    TextButton loginButton;
    TextButton signUpRedirectButton;
    TextField usernameField;
    TextField passwordField;
    Texture background;

    public LoginScreen(final BCFS gam) {
        game = gam;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage); // Set the stage as the input processor

        // Load the skin
        skin = new Skin(Gdx.files.internal("tracerui/tracer-ui.json"));
        background = new Texture(Gdx.files.internal("userAuthBG.jpg"));
        table = new Table();

        loginLabel = new Label("LOG IN", skin, "title");
        serverMessage = new Label("Server Message", skin);

        loginButton = new TextButton("LOG IN", skin);
        signUpRedirectButton = new TextButton("Don't have an account? Create Account", skin, "label");
        usernameField = new TextField("", skin);
        usernameField.setMessageText("Username");

        passwordField = new TextField("", skin);
        passwordField.setMessageText("Password");
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(new Color(0.15f, 0f, 0f, 1f));

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.end();

        // Update and draw the stage
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void show() {
        table.setFillParent(true);
        table.pad(50).padTop(25).top().center();

        float buttonSpacing = 25f;
        table.add(loginLabel).padBottom(50).row();
        table.add(usernameField).width(600).height(75).padBottom(buttonSpacing).row();
        table.add(passwordField).width(600).height(75).padBottom(buttonSpacing).row();
        table.add(loginButton).width(400).padTop(25).height(50).row();
        table.add(signUpRedirectButton).padTop(50).row();
        table.add(serverMessage).padTop(50);

        stage.addActor(table);

        signUpRedirectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                // Handle Button Click
                System.out.println("SIGN UP");
                game.setScreen(new SignUpScreen(game));
                dispose();
            }
        });

        loginButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                DBHelpers dbh = new DBHelpers(DBHelpers.getGlobalConnection());
                String Username = usernameField.getText();;
                String Password = passwordField.getText();
                User res = dbh.LoginUser(Username,Password);
                if (res != null) {
                    GlobalEntities.setCurrentUser(res);
                    System.out.println("LOGGING IN");
                    game.setScreen(new LandingScreen(game));
                    dispose();
                }else{
                    serverMessage.setText("Incorrect Credentials, Please Try Again");
                }
                // Handle Button Click
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

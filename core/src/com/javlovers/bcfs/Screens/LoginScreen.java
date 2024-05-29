package com.javlovers.bcfs.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
    Skin buttonSkin;
    Label loginLabel;
    Label serverMessage;
    Table table;
    Table loginContainer;
    TextButton loginButton;
    TextButton signUpRedirectButton;
    TextField usernameField;
    TextField passwordField;
    Texture background;
    ShapeRenderer renderer;

    public LoginScreen(final BCFS gam) {
        game = gam;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage); // Set the stage as the input processor

        // Load the skin
        skin = new Skin(Gdx.files.internal("tracerui/tracer-ui.json"));
        buttonSkin = new Skin(Gdx.files.internal("custom_ui/buttons.json"));

        background = new Texture(Gdx.files.internal("userAuthBG.jpg"));
        renderer = new ShapeRenderer();

        table = new Table();
        loginContainer = new Table(skin);

        loginLabel = new Label("LOG IN", skin, "title");
        serverMessage = new Label("", skin);

        loginButton = new TextButton("LOG IN", buttonSkin);
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
        table.pad(50).padTop(150).top();

        loginContainer.add(loginLabel).padBottom(35).row();
        loginContainer.add(usernameField).width(400).height(50).padBottom(20).row();
        loginContainer.add(passwordField).width(400).height(50).padBottom(40).row();
        loginContainer.add(loginButton).width(250).padBottom(30).height(50).row();
        loginContainer.add(signUpRedirectButton).padBottom(35).row();
        loginContainer.add(serverMessage);

        table.add(loginContainer).width(800);
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

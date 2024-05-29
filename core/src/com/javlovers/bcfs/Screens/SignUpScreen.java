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
import com.javlovers.bcfs.Screens.BackEnd.Globals.DBHelpers;

import java.util.Objects;

public class SignUpScreen implements Screen {
    final BCFS game;
    OrthographicCamera camera;
    Stage stage;
    Skin skin;
    Skin customSkin;
    Label signUpLabel;
    Label serverMessage;
    Table table;
    TextButton signUpButton;
    TextButton loginRedirectButton;
    TextField usernameField;
    TextField displayNameField;
    TextField passwordField;
    Texture background;


    public SignUpScreen(final BCFS gam) {
        game = gam;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage); // Set the stage as the input processor

        // Load the skin
        skin = new Skin(Gdx.files.internal("tracerui/tracer-ui.json"));
        customSkin = new Skin(Gdx.files.internal("custom_ui/custom_ui.json"));
        background = new Texture(Gdx.files.internal("farm.png"));
        table = new Table();

        signUpLabel = new Label("CREATE ACCOUNT", customSkin);
        serverMessage = new Label("", skin);

        signUpButton = new TextButton("CREATE ACCOUNT", customSkin);
        loginRedirectButton = new TextButton("Already have an account? Log in", skin, "label");
        usernameField = new TextField("", customSkin);
        usernameField.setMessageText("Username");
        displayNameField = new TextField("", customSkin);
        displayNameField.setMessageText("Display Name");

        passwordField = new TextField("", customSkin);
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
        Texture appLogo = new Texture(Gdx.files.internal("logo.png"));
        Image logo = new Image(appLogo);
        logo.setSize(600, 200);
        logo.setPosition(341 , 500);
        table.addActor(logo);
        table.add(signUpLabel).padTop(300).padBottom(35).row();
        table.add(displayNameField).width(450).height(60).padBottom(buttonSpacing).row();
        table.add(usernameField).width(450).height(60).padBottom(buttonSpacing).row();
        table.add(passwordField).width(450).height(40).padBottom(buttonSpacing).row();
        table.add(signUpButton).width(400).padTop(25).height(50).row();
        table.add(loginRedirectButton).padTop(50).row();
        table.add(serverMessage).padTop(50);

        stage.addActor(table);

        signUpButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String DisplayName = displayNameField.getText();
                String UserName = usernameField.getText();
                String Password = passwordField.getText();
                if(Objects.equals(DisplayName, "") || Objects.equals(UserName, "") || Objects.equals(Password, "")){
                    serverMessage.setColor(Color.RED);
                    serverMessage.setText("Please Fill Out ALl Forms");
                    return;
                }
                DBHelpers dbh = new DBHelpers(DBHelpers.getGlobalConnection());
                int isSuccess = dbh.createAccount(DisplayName,UserName,Password);
                if(isSuccess == -1){
                    return;
                }
                System.out.println("SIGN UP");
                game.setScreen(new LoginScreen(game));
                dispose();
            }
        });

        loginRedirectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                // Handle Button Click
                System.out.println("LOGIN");
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

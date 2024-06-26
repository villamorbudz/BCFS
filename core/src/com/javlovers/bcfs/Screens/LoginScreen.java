package com.javlovers.bcfs.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.javlovers.bcfs.BCFS;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.javlovers.bcfs.Others.GlobalEntities;
import com.javlovers.bcfs.Screens.BackEnd.Globals.DBHelpers;
import com.javlovers.bcfs.Screens.BackEnd.Main.Cock;
import com.javlovers.bcfs.Screens.BackEnd.Main.User;

import java.net.Inet4Address;
import java.util.HashMap;

public class LoginScreen implements Screen {
    final BCFS game;
    OrthographicCamera camera;
    Stage stage;
    Skin skin;
    Skin customSkin;
    Label loginLabel;
    Label serverMessage;
    Table table;
    Table loginContainer;
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
        customSkin = new Skin(Gdx.files.internal("custom_ui/custom_ui.json"));
        background = new Texture(Gdx.files.internal("farm.png"));

        table = new Table();
        loginContainer = new Table(skin);

        loginLabel = new Label("LOG IN", customSkin);
        serverMessage = new Label("", skin);

        usernameField = new TextField("", customSkin);
        usernameField.setMessageText("Username");
        passwordField = new TextField("", customSkin);
        passwordField.setMessageText("Password");
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');

        loginButton = new TextButton("LOG IN", customSkin);
        signUpRedirectButton = new TextButton("Don't have an account? Create Account", skin, "label");
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(new Color(0.15f, 0f, 0f, 1f));

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.setColor(1, 1, 1, 0.5f);
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

        Texture appLogo = new Texture(Gdx.files.internal("logo.png"));
        Image logo = new Image(appLogo);
        logo.setSize(450, 150);
        logo.setPosition(loginContainer.getWidth() / 2, 425);
        loginContainer.addActor(logo);
        loginContainer.add(loginLabel).padTop(175).padBottom(35).row();

        loginContainer.add(usernameField).width(450).height(60).padBottom(20).row();
        loginContainer.add(passwordField).width(450).height(60).padBottom(40).row();
        loginContainer.add(loginButton).width(350).padBottom(25).height(50).row();
        loginContainer.add(signUpRedirectButton).padBottom(30).row();
        loginContainer.add(serverMessage);

        usernameField.setAlignment(Align.center);
        passwordField.setAlignment(Align.center);

        table.add(loginContainer);
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
                String Username = usernameField.getText();
                String Password = passwordField.getText();
                User res = dbh.LoginUser(Username,Password);
                if (res != null) {
                    GlobalEntities.setCurrentUser(res);
                    HashMap<Integer, Cock> Tmp = dbh.getAllCurrCockData();
                    GlobalEntities.CurrentCock = Tmp.get(res.getUserID());
                    Cock temp = new Cock("",res.getUserID());
                    temp.setCockID(0);
                    if(GlobalEntities.CurrentCock==null)GlobalEntities.CurrentCock=temp;

                    serverMessage.setColor(Color.GREEN);
                    serverMessage.setText("Logging in...");
                    System.out.println("LOGGING IN");
                    game.setScreen(new LandingScreen(game));
                    dispose();
                }else{
                    serverMessage.setColor(new Color(1f, 0.2f, 0.2f, 1f));
                    serverMessage.setText("Invalid credentials, please try again");
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

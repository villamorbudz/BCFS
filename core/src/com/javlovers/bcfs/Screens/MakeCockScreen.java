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

public class MakeCockScreen implements Screen {
    final BCFS game;
    OrthographicCamera camera;
    Stage stage;
    ArrayList<TextButton> attackButtons;
    Skin skin;

    public MakeCockScreen(final BCFS gam) {
        game = gam;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage); // Set the stage as the input processor

        // Load the skin
        skin = new Skin(Gdx.files.internal("tracerui/tracer-ui.json"));
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
        Table table = new Table();
        table.setFillParent(true);
        table.pad(50).padTop(25).left();

        attackButtons = new ArrayList<>();
        Label attacksText = new Label("ATTACKS", skin);
        Label nameText = new Label("NAME: ", skin);
        TextField cockNameTextField = new TextField("", skin);

        // Creating
        TextButton backButton = new TextButton("BACK", skin);
        TextButton attack1Button = new TextButton("ATTACK 1", skin);
        TextButton attack2Button = new TextButton("ATTACK 2", skin);
        TextButton attack3Button = new TextButton("ATTACK 3", skin);
        TextButton attack4Button = new TextButton("ATTACK 4", skin);
        TextButton saveButton = new TextButton("SAVE", skin);
        TextButton testButton = new TextButton("TEST", skin);

        attackButtons.add(attack1Button);
        attackButtons.add(attack2Button);
        attackButtons.add(attack3Button);
        attackButtons.add(attack4Button);

        // Adding
        Table sidebarTable = new Table();
        sidebarTable.top().left();

        table.add(sidebarTable);

        float buttonSpacing = 25f;

        sidebarTable.add(backButton).width(200).height(45).padBottom(buttonSpacing).left();
        sidebarTable.row();

        Table cockNameSubtable = new Table();
        cockNameSubtable.add(nameText).padRight(25).left();
        cockNameSubtable.add(cockNameTextField).width(300).left();
        sidebarTable.add(cockNameSubtable).padTop(10).left();
        sidebarTable.row();

        sidebarTable.add(attacksText).left().padTop(50).padBottom(25).colspan(2);
        sidebarTable.row();

        for (TextButton button : attackButtons) {
            sidebarTable.add(button).width(400).height(60).padBottom(buttonSpacing).left();
            sidebarTable.row(); // Move to the next row after each button
        }

        // Subtable
        Table subTable = new Table();
        subTable.add(saveButton).width(190).height(60).padRight(20).left();
        subTable.add(testButton).width(190).height(60);

        sidebarTable.add(subTable).padTop(50).colspan(2).left();

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

        attack1Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Handle Attack 1 Click
                System.out.println("ATTACK 1");
            }
        });

        attack2Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Handle Attack 2 Click
                System.out.println("ATTACK 2");
            }
        });

        attack3Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Handle Attack 3 Click
                System.out.println("ATTACK 3");
            }
        });

        attack4Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Handle Attack 1 Click
                System.out.println("ATTACK 4");
            }
        });

        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Handle Save Button Click
                System.out.println("SAVE");
            }
        });

        testButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Handle Test Button Click
                System.out.println("TEST");
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
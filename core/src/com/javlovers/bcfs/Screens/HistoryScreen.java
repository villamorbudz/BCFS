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

public class HistoryScreen implements Screen {
    final BCFS game;
    OrthographicCamera camera;
    Stage stage;
    Skin skin;

    public HistoryScreen(final BCFS gam) {
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
        table.pad(50).padTop(25).top().left();

        ScrollPane historyScrollPane;
        Table sidebarTable;
        Table gameHistoryTable;

        // Creating
        Label historyLabel = new Label("HISTORY", skin);
        TextButton backButton = new TextButton("BACK", skin);
        TextArea gameInfo = new TextArea("GAME INFO", skin);

        // Adding
        sidebarTable = new Table();
        sidebarTable.top().left();
        table.add(sidebarTable);

        gameHistoryTable = new Table();
        historyScrollPane = new ScrollPane(gameHistoryTable, skin);

        float buttonSpacing = 25f;

        sidebarTable.add(backButton).width(200).height(45).padLeft(15).padBottom(50f).left();
        sidebarTable.row();

        sidebarTable.add(historyLabel).padTop(25).padBottom(25).center();
        sidebarTable.row();

        for (int i = 1; i <= 50; i++) {
            TextButton gameButton = new TextButton("PLAYER" + i + " VS PLAYER" + (i + 1), skin);
            gameHistoryTable.add(gameButton).width(350).height(60).padBottom(buttonSpacing).center();
            gameHistoryTable.row();
        }
        sidebarTable.add(historyScrollPane).width(400).growY().padBottom(buttonSpacing).top().left();

        table.add(gameInfo).pad(25).grow();
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
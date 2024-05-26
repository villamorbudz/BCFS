package com.javlovers.bcfs.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.javlovers.bcfs.BCFS;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

import static com.badlogic.gdx.utils.Align.center;
import static com.badlogic.gdx.utils.Align.top;

public class HistoryScreen implements Screen {
    final BCFS game;
    OrthographicCamera camera;
    Stage stage;
    Skin skin;
    Table table;
    Table sidebarTable;
    Table gameHistoryTable;
    Table gameInfoTable;
    TextButton gameInfo;
    ScrollPane historyScrollPane;
    Label historyLabel;
    TextButton backButton;
    ButtonGroup<TextButton> gamesButtonGroup;

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
        table = new Table();
        sidebarTable = new Table();
        gameHistoryTable = new Table();
        gameInfoTable = new Table();

        table.setFillParent(true);
        table.pad(50).padTop(25).top().left();

        historyLabel = new Label("HISTORY", skin);
        backButton = new TextButton("BACK", skin);
        historyScrollPane = new ScrollPane(gameHistoryTable, skin);

        gamesButtonGroup = new ButtonGroup<>();
        gamesButtonGroup.setMinCheckCount(0);
        gamesButtonGroup.setMaxCheckCount(1);

        sidebarTable.top().left();

        float buttonSpacing = 25f;

        table.add(sidebarTable).expandY().top().left();
        sidebarTable.add(backButton).width(200).height(45).padLeft(15).padBottom(50f).left();
        sidebarTable.row();

        sidebarTable.add(historyLabel).padBottom(25).center();
        sidebarTable.row();

        for (int i = 1; i <= 50; i++) {
            TextButton gameButton = new TextButton("", skin, "toggle");
            Label gameButtonLabel = new Label("PLAYER" + i + " VS PLAYER" + (i + 1), skin);
            gameButton.setLabel(gameButtonLabel);
            gameButtonLabel.setAlignment(center);

            gamesButtonGroup.add(gameButton);
            gameHistoryTable.add(gameButton).width(350).height(60).padBottom(buttonSpacing).center();
            gameHistoryTable.row();

            gameButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (gameButton.isChecked()) {
                        displayGameInfo();
                    } else {
                        clearGameInfo();
                    }
                }
            });
        }

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Handle Button Click
                System.out.println("BACK");
                game.setScreen(new LandingScreen(game));
                dispose();
            }
        });

        gameInfo = new TextButton("GAME INFO", skin, "display");
        gameInfoTable.add(gameInfo).grow().center();
        clearGameInfo();

        table.add(gameInfoTable).grow().padLeft(15);
        sidebarTable.add(historyScrollPane).width(400).growY().padBottom(buttonSpacing).top().left();
        stage.addActor(table);
    }

    private void displayGameInfo() {
        // add functionality where it inputs appropriate game info
        Label gameInfoText = new Label("GAME INFO\n\n" +
                "STAT\n" +
                "STAT\n" +
                "STATs\n", skin);
        gameInfo.setLabel(gameInfoText);
        gameInfoText.setAlignment(center);

        gameInfo.setDisabled(true);
        gameInfo.setVisible(true);
    }

    private void clearGameInfo() {
        gameInfo.setText("");
        gameInfo.setVisible(false);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}

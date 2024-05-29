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

import static com.badlogic.gdx.utils.Align.center;

public class HistoryScreen implements Screen {
    final BCFS game;
    OrthographicCamera camera;
    Stage stage;
    Skin skin;
    Table table;
    Table sidebarTable;
    Table gameHistoryTable;
    Table gameInfoContainer;
    Table gameInfoTable;
    Table navigationContainer;
    Table userAttackCards;
    Table enemyAttackCards;
    Table fightStatsTable;
    Table attacksSequenceTable;
    ScrollPane historyScrollPane;
    Label historyLabel;
    Label userLabel;
    Label enemyLabel;
    TextButton prev;
    TextButton next;
    TextButton backButton;
    TextArea attackLogs;
    ButtonGroup<TextButton> gamesButtonGroup;
    int page;

    public HistoryScreen(final BCFS gam) {
        game = gam;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage); // Set the stage as the input processor

        // Load the skin
        skin = new Skin(Gdx.files.internal("tracerui/tracer-ui.json"));

        table = new Table();
        sidebarTable = new Table();
        gameHistoryTable = new Table();
        gameInfoContainer = new Table();
        gameInfoTable = new Table();
        navigationContainer = new Table();
        fightStatsTable = new Table();
        attacksSequenceTable = new Table();

        historyLabel = new Label("HISTORY", skin, "title");
        userLabel = new Label("", skin);
        enemyLabel = new Label("", skin);

        attackLogs = new TextArea("", skin);
        backButton = new TextButton("BACK", skin);
        prev = new TextButton("<", skin);
        next = new TextButton(">",skin);
        historyScrollPane = new ScrollPane(gameHistoryTable, skin);

        gamesButtonGroup = new ButtonGroup<>();

        page = 1;
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

        gamesButtonGroup.setMinCheckCount(0);
        gamesButtonGroup.setMaxCheckCount(1);

        sidebarTable.top().left();

        float buttonSpacing = 25f;

        table.add(sidebarTable).expandY().top().left();
        sidebarTable.add(backButton).width(200).height(45).padLeft(15).padBottom(25).left();
        sidebarTable.row();

        sidebarTable.add(historyLabel).padBottom(25).center();
        sidebarTable.row();

        // TODO: implement getter for games from the player
        String enemyName = "player2";

        for (int i = 1; i <= 50; i++) {
            TextButton gameButton = new TextButton("", skin, "toggle");
            Label gameButtonLabel = new Label(enemyName, skin);
            gameButton.setLabel(gameButtonLabel);
            gameButtonLabel.setAlignment(center);

            gamesButtonGroup.add(gameButton);
            gameHistoryTable.add(gameButton).width(350).height(60).padBottom(buttonSpacing).center();
            gameHistoryTable.row();

            gameButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (gameButton.isChecked()) {
                        // getGameInfo
                    } else {
                        // clearInfoTable
                    }
                }
            });
        }

        getAttackCards();

        gameInfoContainer.add(gameInfoTable).row();
        table.add(gameInfoContainer).grow().top().row();
        sidebarTable.add(historyScrollPane).width(400).growY().top().left();
        navigationContainer.add(prev).padRight(25);
        navigationContainer.add(next).padLeft(25);
        gameInfoContainer.add(navigationContainer).colspan(3).padTop(50).growX().center();
        stage.addActor(table);
        table.setDebug(true);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Handle Button Click
                System.out.println("BACK");
                game.setScreen(new LandingScreen(game));
                dispose();
            }
        });

        prev.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Handle Button Click
                navigate(1);
            }
        });

        next.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Handle Button Click
                navigate(2);
            }
        });
    }

    private void navigate(int i) {
        switch(i) {
            case 1:
                gameInfoTable.clear();
                getAttackCards();
                break;
            case 2:
                gameInfoTable.clear();
                getFightReplay();
                break;
        }
    }

    private void getAttackCards() {
        // fetch from DB
        userLabel.setText("cockName (You)");
        enemyLabel.setText("cockName (enemy)");

        gameInfoTable.add(userLabel).growX().padLeft(15).colspan(3).left().row();
        userAttackCards = new Table();
        for(int i = 0; i < 4; i++) {
            TextButton attackCard = new TextButton("attackName" + "\n\n\n" +
                    "IDamage:   " + "atkID" + "\n\n" +
                    "Multiplier:   " + "param2" + "\n\n" +
                    "Speed:   " + "param3" + "\n", skin, "display");
            userAttackCards.add(attackCard).pad(15);
        }
        gameInfoTable.add(userAttackCards).colspan(3).row();
        userAttackCards.padBottom(50);

        gameInfoTable.add(enemyLabel).colspan(3).growX().padLeft(15).right().row();
        enemyAttackCards = new Table();
        for(int i = 0; i < 4; i++) {
            TextButton attackCard = new TextButton("attackName" + "\n\n\n" +
                    "IDamage:   " + "atkID" + "\n\n" +
                    "Multiplier:   " + "param2" + "\n\n" +
                    "Speed:   " + "param3" + "\n", skin, "display");
            enemyAttackCards.add(attackCard).pad(15);
        }
        gameInfoTable.add(enemyAttackCards).colspan(3).padBottom(25).row();

        Label param1 = new Label("Param1: " + "9999", skin);
        Label param2 = new Label("Param2: " + "9999", skin);
        Label param3 = new Label("Param3: " + "9999", skin);

        gameInfoTable.add(param1);
        gameInfoTable.add(param2);
        gameInfoTable.add(param3);

    }
    private void getFightReplay() {
        attacksSequenceTable.add(attackLogs).width(800).height(300).bottom();
        attackLogs.setDisabled(true);
        gameInfoTable.add(attacksSequenceTable).center();
        attacksSequenceTable.setDebug(true);
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

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

import static com.badlogic.gdx.math.MathUtils.random;
import static com.badlogic.gdx.utils.Align.center;

public class ChallengeScreen implements Screen {
    final BCFS game;
    OrthographicCamera camera;
    Stage stage;
    Skin skin;
    Table mainTable;
        Table sidebarTable;
            TextButton backButton;
            TextButton cockStats;
            TextButton editCockButton;
            ButtonGroup<TextButton> challengesButtonGroup;

        Table challengeContainer;
            ScrollPane challengeListScrollPane;
                Table challengeListContainer;
                    TextButton sampleRequestChallenge;
    Table challengeButtonBar;
    Texture background;

    Label challengesLabel;
    public ChallengeScreen(final BCFS gam) {
        game = gam;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage); // Set the stage as the input processor

        // Load the skin
        skin = new Skin(Gdx.files.internal("tracerui/tracer-ui.json"));

        background = new Texture(Gdx.files.internal("landingBG.gif"));

        mainTable = new Table();
            sidebarTable = new Table();
            challengeContainer = new Table();
                challengeListContainer = new Table();
                challengeListScrollPane = new ScrollPane(challengeListContainer.top());
                        challengesButtonGroup = new ButtonGroup();
                challengeButtonBar = new Table();


        editCockButton = new TextButton("EDIT COCK", skin);
        cockStats = new TextButton("", skin, "display");
        backButton = new TextButton("BACK", skin);

        challengesLabel = new Label("SELECT USER TO CHALLENGE", skin, "title");

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(new Color(0.15f, 0f, 0f, 1f));

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(background,0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.end();

        // Update and draw the stage
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void show() {
        float buttonSpacing = 25f;

        mainTable.setFillParent(true);
        mainTable.pad(50).padTop(25).top().left();

        sidebarTable.top().left();
        sidebarTable.add(backButton).width(200).height(45).padBottom(buttonSpacing).left().row();
        sidebarTable.add(cockStats).width(350).height(350).padBottom(buttonSpacing).left().row();
        sidebarTable.add(editCockButton).width(350).height(60).padBottom(20).left().row();
        cockStats.setLabel(getCockStats());
        cockStats.align(Align.center);
        mainTable.add(sidebarTable);

        challengeContainer.add(challengesLabel).padBottom(15).center();
        challengeContainer.row();


        challengeContainer.add(challengeListScrollPane).grow();
        challengeContainer.row();

        challengesButtonGroup.setMaxCheckCount(1);
        challengesButtonGroup.setMinCheckCount(0);
        challengesButtonGroup.setUncheckLast(true);

        mainTable.add(challengeContainer).grow().pad(20);

        getPlayerList();
//        challengeContainer.setDebug(true);

        // CLICK HANDLING
        editCockButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Handle Button Click
                System.out.println("EDIT COCK");
                game.setScreen(new MakeCockScreen(game));
                dispose();
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Handle Button Click
                System.out.println("BACK");
                game.setScreen(new LandingScreen(game));
                dispose();
            }
        });

        stage.addActor(mainTable);
    }

    // parse and send cockstats to the display
    private Label getCockStats() {
        String attackName = "ATTACKNAME";
        int param1 = random.nextInt(9999 - 1 + 1) + 1;
        int param2 = random.nextInt(9999 - 1 + 1) + 1;
        int param3 = random.nextInt(9999 - 1 + 1) + 1;
        String sampleCockStats = attackName + "\n\n\n" +
                "PARAM:   " + param1 + "\n\n" +
                "PARAM:   " + param2 + "\n\n" +
                "PARAM:   " + param3 + "\n";

        Label cockStats = new Label(sampleCockStats, skin);
        cockStats.setAlignment(center);
        return cockStats;
    }

    private void getPlayerList() {
        for(int i = 0; i < 5; i++) {
            sampleRequestChallenge = new TextButton("PLAYER " + i, skin);
            challengeListContainer.add(sampleRequestChallenge).padLeft(75).padRight(75).padBottom(5).padTop(5).growX().height(50).top();
            challengeListContainer.row();
        }
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
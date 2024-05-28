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
import com.javlovers.bcfs.Others.GlobalEntities;
import com.javlovers.bcfs.Screens.BackEnd.Globals.GlobalObjects;
import com.javlovers.bcfs.Screens.BackEnd.Main.Attack;
import com.javlovers.bcfs.Screens.BackEnd.Main.Cock;
import org.w3c.dom.Text;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.badlogic.gdx.utils.Align.center;

public class ChallengeScreen implements Screen {
    final BCFS game;
    OrthographicCamera camera;
    Stage stage;
    Skin skin;
    Table table;
    Table sidebarTable;
    Table challengesContainer;
    TextButton editCockButton;
    TextButton challengeUser;
    TextButton cockChallenges;
    TextButton cockStats;
    TextButton backButton;
    TextButton cockChallenge;
    public ChallengeScreen(final BCFS gam) {
        game = gam;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage); // Set the stage as the input processor

        // Load the skin
        skin = new Skin(Gdx.files.internal("tracerui/tracer-ui.json"));

        table = new Table();
        sidebarTable = new Table();
        challengesContainer = new Table();

        editCockButton = new TextButton("EDIT COCK", skin);
        challengeUser = new TextButton("CHALLENGE USER", skin);
        cockChallenges = new TextButton("COCK CHALLENGES", skin);
        cockStats = new TextButton("", skin, "display");
        backButton = new TextButton("BACK", skin);
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

        sidebarTable.top().left();

        table.add(sidebarTable);
        table.add(challengesContainer).grow().pad(20);

//        challengesContainer.setDebug(true);
//        cockChallenge = new TextButton("CHALLENGE 1", skin);
//        cockChallenge.add(challengesContainer).center();

        float buttonSpacing = 25f;

        sidebarTable.add(backButton).width(200).height(45).padBottom(buttonSpacing).left().row();
        sidebarTable.add(cockStats).width(350).height(350).padBottom(buttonSpacing).left().row();
        sidebarTable.add(editCockButton).width(350).height(60).padBottom(20).left().row();
        sidebarTable.add(challengeUser).width(350).height(60).padBottom(20).left().row();
        sidebarTable.add(cockChallenges).width(350).height(60).padBottom(20).left().row();
        cockStats.setLabel(getCockStats());
        cockStats.align(Align.center);
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

        challengeUser.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Handle Button Click
                System.out.println("CHALLENGE USER");
            }
        });

        cockChallenges.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Handle Button Click
                System.out.println("COCK CHALLENGES");
                getCockChallenges();
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

        stage.addActor(table);
    }

    // parse and send cockstats to the display
    private Label getCockStats() {
        Cock curr = GlobalEntities.CurrentCock;
        StringBuilder sb = new StringBuilder();
        if(curr !=null){
            String attackName = curr.getName();
            sb.append(attackName + "\n\n\n");
            for(Attack atk:curr.getAttackList()){
                sb.append("ATTACK : " + atk.getName() + "\n\n");
            }
        }
        Label cockStats = new Label(sb.toString(), skin);
        cockStats.setAlignment(center);
        return cockStats;
    }

    private void getCockChallenges() {
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
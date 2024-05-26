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

import static com.badlogic.gdx.math.MathUtils.random;

public class MakeCockScreen implements Screen {
    final BCFS game;
    OrthographicCamera camera;
    Stage stage;
    Skin skin;
    ArrayList<TextButton> cockAttacks;
    Table table;
    Table sidebarTable;
    Table subTable;
    Table attackListTable;
    Table attackTraversalButtonBar;
    TextField cockNameTextField;
    Label attacksText;
    Label nameText;
    TextButton backButton;
    TextButton saveButton;
    TextButton testButton;
    TextButton prevAttacksButton;
    TextButton selectAttackButton;
    TextButton nextAttacksButton;

    public MakeCockScreen(final BCFS gam) {
        game = gam;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage); // Set the stage as the input processor

        // Load the skin
        skin = new Skin(Gdx.files.internal("tracerui/tracer-ui.json"));

        // Initialize
        table = new Table();
        sidebarTable = new Table();
        subTable = new Table();
        attackListTable = new Table();
        attackTraversalButtonBar = new Table();
        cockNameTextField = new TextField("", skin);
        attacksText = new Label("ATTACKS", skin);
        nameText = new Label("NAME: ", skin);
        backButton = new TextButton("BACK", skin);
        saveButton = new TextButton("SAVE", skin);
        testButton = new TextButton("TEST", skin);
        prevAttacksButton = new TextButton("<", skin);
        selectAttackButton = new TextButton("SELECT", skin);
        nextAttacksButton = new TextButton(">", skin);
        cockAttacks = new ArrayList<>();

        table.setFillParent(true);
        table.pad(25).left();

        cockAttacks.add(new TextButton("ATTACK 1", skin));
        cockAttacks.add(new TextButton("ATTACK 2", skin));
        cockAttacks.add(new TextButton("ATTACK 3", skin));
        cockAttacks.add(new TextButton("ATTACK 4", skin));
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
        // Main Table Elements
        table.add(sidebarTable).top().left();
        table.add(attackListTable).padTop(25).padLeft(20).top().left();

        float buttonSpacing = 25f;
        sidebarTable.add(backButton).width(175).height(45).padBottom(buttonSpacing).left();
        sidebarTable.row();

        // Cock Name TextField
        Table cockNameSubtable = new Table();
        cockNameSubtable.add(nameText).padRight(25).left();
        cockNameSubtable.add(cockNameTextField).width(250).left();
        sidebarTable.add(cockNameSubtable).center();
        sidebarTable.row();

        // Sidebar
        sidebarTable.add(attacksText).left().padTop(50).padBottom(25).colspan(2);
        sidebarTable.row();

        for (TextButton button : cockAttacks) {
            sidebarTable.add(button).width(350).height(60).padBottom(buttonSpacing).left();
            sidebarTable.row();

            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // Handle Button Click
                    System.out.println(button.getText());
                    createAttackList();
                }
            });
        }

        subTable.add(saveButton).width(170).height(60).padRight(15).left();
        subTable.add(testButton).width(170).height(60);
        sidebarTable.add(subTable).padTop(50).colspan(2).left();

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

        stage.addActor(table);
//        table.setDebug(true);
    }

    private void createAttackList() {
        // Clear existing attack list
        attackListTable.clear();

        // fields for attackCard, insert values from attacksDB
        // Add field for attacktype, which defines the button skin to be used in the constructor
        String attackName = "ATTACKNAME";
        int param1 = random.nextInt(9999 - 1 + 1) + 1;
        int param2 = random.nextInt(9999 - 1 + 1) + 1;
        int param3 = random.nextInt(9999 - 1 + 1) + 1;

        // ATTACK CARD CONSTRUCTOR
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                final int row = i;
                final int col = j;
                TextButton attackCard = new TextButton(attackName + "\n\n\n" +
                        "PARAM:   " + param1 + "\n\n" +
                        "PARAM:   " + param2 + "\n\n" +
                        "PARAM:   " + param3 + "\n\n", skin);
                attackListTable.add(attackCard).width(175).height(180).pad(5);
                attackCard.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        // Handle Attack Card Click
                        System.out.println("ATTACK CARD [" + row + "][" + col + "]");
                    }
                });
            }
            attackListTable.row();
        }

        attackTraversalButtonBar.add(prevAttacksButton).width(125).padRight(25);
        attackTraversalButtonBar.add(selectAttackButton).width(225).padRight(25);
        attackTraversalButtonBar.add(nextAttacksButton).width(125);
        attackListTable.add(attackTraversalButtonBar).colspan(3).pad(20);

        prevAttacksButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // add page traversal from attacks in DB
                // add attacks list traversal tracker and check index bounds
                System.out.println("PREV");
                createAttackList();
            }
        });
        nextAttacksButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // add page traversal from attacks in DB
                // add attacks list traversal tracker and check index bounds
                System.out.println("NEXT");
                createAttackList();
            }
        });

        selectAttackButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // set selected Attack to target cockAttacks slot
                System.out.println("SELECT");
            }
        });
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose(); // Dispose of the skin
    }
}
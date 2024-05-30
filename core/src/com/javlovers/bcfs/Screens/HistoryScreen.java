package com.javlovers.bcfs.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.javlovers.bcfs.BCFS;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.javlovers.bcfs.CockfightGame;
import com.javlovers.bcfs.Others.GlobalEntities;
import com.javlovers.bcfs.Screens.BackEnd.Globals.DBHelpers;
import com.javlovers.bcfs.Screens.BackEnd.Main.Attack;
import com.javlovers.bcfs.Screens.BackEnd.Main.Cock;
import com.javlovers.bcfs.Screens.BackEnd.Main.MatchResult;
import com.javlovers.bcfs.Screens.BackEnd.Main.User;

import java.security.AllPermission;
import java.util.ArrayList;
import java.util.HashMap;

import static com.badlogic.gdx.utils.Align.center;

public class HistoryScreen implements Screen {
    final BCFS game;
    OrthographicCamera camera;
    Stage stage;
    Skin skin;
    Skin customSkin;
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
    Texture backgroundTexture;
    int page;

    CockfightGame cockfightGame; // Instance of CockfightGame

    public HistoryScreen(final BCFS gam) {
        game = gam;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage); // Set the stage as the input processor

        // Load the skin
        skin = new Skin(Gdx.files.internal("tracerui/tracer-ui.json"));
        customSkin = new Skin(Gdx.files.internal("custom_ui/custom_ui.json"));
        backgroundTexture = new Texture(Gdx.files.internal("farm.png"));

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
        backButton = new TextButton("BACK", customSkin);
        prev = new TextButton("<", skin);


        next = new TextButton("REPLAY", customSkin);
        historyScrollPane = new ScrollPane(gameHistoryTable, skin);

        gamesButtonGroup = new ButtonGroup<>();

        page = 1;

        // Initialize the CockfightGame
        cockfightGame = new CockfightGame();
        cockfightGame.create();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(new Color(0.15f, 0f, 0f, 1f));

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.end();

        // Update and draw the stage
        stage.act(delta);
        stage.draw();

        // Render the CockfightGame if it's active
        if (cockfightGame.getScreen() != null) {
            cockfightGame.render();
        }
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

        DBHelpers dbh = new DBHelpers(DBHelpers.getGlobalConnection());
        HashMap<Integer, String> allU = dbh.getAllDIsplayNames();
        HashMap<Integer, ArrayList<Integer>> History = dbh.getMatchesByUser(GlobalEntities.currentUser.getUserID());
        HashMap<Integer, Cock> AllC = dbh.getAllCockData();
        // TODO: implement getter for games from the player
        for(Integer MatchID: History.keySet()){
            ArrayList<Integer> MR = History.get(MatchID);
            String p1 = allU.get(AllC.get(MR.get(0)).getOwnerID());
            String p2 = allU.get(AllC.get(MR.get(1)).getOwnerID());

            String enemyName = "";
            if(p1.equals(GlobalEntities.currentUser.getDisplayName())){
                enemyName = "Match: " + p2;
            }else{
                enemyName = "Match: " + p1;
            }
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
                        gameInfoTable.clear();
                        getAttackCards(MR);
                    } else {
                        // clearInfoTable
                        gameInfoTable.clear();
                    }

                }
            });
        }




        gameInfoContainer.add(gameInfoTable).row();
        table.add(gameInfoContainer).grow().top().row();
        sidebarTable.add(historyScrollPane).width(400).growY().top().left();
//        navigationContainer.add(prev).padRight(25);
        navigationContainer.add(next).width(200).height(45).padLeft(25);
        gameInfoContainer.add(navigationContainer).colspan(3).padTop(50).growX().center();
        stage.addActor(table);
        stage.addActor(table);

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
//                navigate(2);
                showFightReplay();
            }

        });
    }

    private void navigate(int i) {
        switch(i) {
            case 1:
                gameInfoTable.clear();
                //getAttackCards();
                break;
            case 2:
                gameInfoTable.clear();
                getFightReplay();
                break;
        }
    }

    private void getAttackCards(ArrayList<Integer> MatchResult) {
        DBHelpers dbh = new DBHelpers(DBHelpers.getGlobalConnection());
        HashMap<Integer, Cock> AllC = dbh.getAllCockData();
        HashMap<Integer,String>DisplayNames = dbh.getAllDIsplayNames();
        Cock C1 = AllC.get(MatchResult.get(0));
        Cock C2 = AllC.get(MatchResult.get(1));
        String p1 = DisplayNames.get(C1.getOwnerID());
        String p2 = DisplayNames.get(C2.getOwnerID());
        String P1 = String.format("%s (%s)",C1.getName(),p1);
        String P2 = String.format("%s (%s)",C2.getName(),p2);
        Cock Player;
        Cock Enemy;
        if(p1.equals(GlobalEntities.currentUser.getDisplayName())){
            userLabel.setText(P1);
            enemyLabel.setText(P2);
            Player = C1;
            Enemy = C2;
        }else{
            userLabel.setText(P2);
            enemyLabel.setText(P1);
            Player = C2;
            Enemy = C1;
        }
        gameInfoTable.add(userLabel).growX().padLeft(15).colspan(3).left().row();
        userAttackCards = new Table();
        ArrayList<Attack> atkList = Player.getAttackList();
        for(int i = 0; i < 4 ; i++) {
            String AtkDesc = "";
            if(i < atkList.size()){
                AtkDesc = atkList.get(i).toString();
            }
            Attack atk = atkList.get(i);
            TextButton attackCard = new TextButton(AtkDesc, skin, "display");
            userAttackCards.add(attackCard).pad(15);
        }
        gameInfoTable.add(userAttackCards).colspan(3).row();
        userAttackCards.padBottom(50);

        gameInfoTable.add(enemyLabel).colspan(3).growX().padLeft(15).right().row();
        enemyAttackCards = new Table();
        atkList = Enemy.getAttackList();
        for(int i = 0; i < 4 ; i++) {
            String AtkDesc = "";
            if(i < atkList.size()){
                AtkDesc = atkList.get(i).toString();
            }
            Attack atk = atkList.get(i);
            TextButton attackCard = new TextButton(AtkDesc, skin, "display");
            enemyAttackCards.add(attackCard).pad(15);
        }
        gameInfoTable.add(enemyAttackCards).colspan(3).padBottom(25).row();
        int WinnerID = MatchResult.get(2);
        Label param1 = new Label("Winner: " + ((WinnerID != 0) ? AllC.get(WinnerID).getName() : "None"), skin);
        //Label param2 = new Label("Param2: " + "9999", skin);
        //Label param3 = new Label("Param3: " + "9999", skin);

        gameInfoTable.add(param1);
        //gameInfoTable.add(param2);
        //gameInfoTable.add(param3);
    }

    private void getFightReplay() {
        /*CockfightGame cockfightGame = new CockfightGame();
        cockfightGame.startGame();*/

        attacksSequenceTable.add(attackLogs).width(800).height(300).bottom();
        attackLogs.setDisabled(true);
        gameInfoTable.add(attacksSequenceTable).center();
    }

    private void showFightReplay() {
        // Logic to display the CockfightGame within the history screen
        // This might involve setting the screen to the PlayScreen or rendering the game within a specific area
        cockfightGame.startGame(this,cockfightGame,game);
        game.setScreen(cockfightGame.getScreen());
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
        cockfightGame.dispose(); // Dispose of the CockfightGame resources
    }
}

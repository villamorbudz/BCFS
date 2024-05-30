package com.javlovers.bcfs.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.javlovers.bcfs.BCFS;
import com.javlovers.bcfs.CockfightGame;
import com.javlovers.bcfs.Screens.HistoryScreen;
import com.javlovers.bcfs.Screens.LandingScreen;
import com.javlovers.bcfs.Screens.PlayScreen;
import com.javlovers.bcfs.Screens.SettingsScreen;

public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport;
    private Integer worldTimer;
    private float timeCount;
    private Integer score;
    public static boolean btnCicked = false;

    Label countdownLabel;
    Label scoreLabel;
    Label timeLabel;
    Label levelLabel;
    Label worldLabel;
    Label marioLabel;
    TextButton prevScreenBtn;


    public Hud(SpriteBatch spriteBatch, Screen prevScreen, CockfightGame game, BCFS bcfs) {
        worldTimer = 300;
        timeCount = 0;
        score = 0;

        viewport = new FitViewport(CockfightGame.V_WIDTH, CockfightGame.V_HEIGHT , new OrthographicCamera());
        stage = new Stage(viewport, spriteBatch);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levelLabel = new Label("1-1", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        worldLabel = new Label("WORLD", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        marioLabel = new Label("MARIO", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        Skin customSkin = new Skin(Gdx.files.internal("custom_ui/custom_ui.json"));
        prevScreenBtn = new TextButton("Back", customSkin);


        table.add(marioLabel).expandX().padTop(10);
        table.add(worldLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.row();
        table.add(scoreLabel).expandX();
        table.add(levelLabel).expandX();
//        table.add(prevScreen).expandX();
//        table.row();
        prevScreenBtn.setHeight(30);
        prevScreenBtn.setWidth(100);
        prevScreenBtn.setX(10);
        prevScreenBtn.setY(20);

        stage.addActor(table);
        stage.addActor(prevScreenBtn);
        System.out.println(prevScreenBtn);

        prevScreenBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Handle Button Click
                System.out.println("Back button clickled");
                btnCicked=true;
//                game.setScreen(prevScreen);
                bcfs.setScreen(new HistoryScreen(bcfs));
//                game.dispose();
                dispose();
                stage.dispose();

//                try {
//                    Thread.sleep(1000);
//
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//                game.setScreen(new SettingsScreen(game));
                dispose();
//                PlayScreen.dispose();


            }
        });


        Gdx.input.setInputProcessor(stage);
    }


    @Override
    public void dispose() {
        stage.dispose();
    }
}
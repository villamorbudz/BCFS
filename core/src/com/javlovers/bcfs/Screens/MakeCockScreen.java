package com.javlovers.bcfs.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.javlovers.bcfs.BCFS;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.javlovers.bcfs.Others.GlobalEntities;
import com.javlovers.bcfs.Screens.BackEnd.DB.LocalHostConnection;
import com.javlovers.bcfs.Screens.BackEnd.Globals.DBHelpers;
import com.javlovers.bcfs.Screens.BackEnd.Main.Attack;
import com.javlovers.bcfs.Screens.BackEnd.Main.Cock;
import com.javlovers.bcfs.Screens.BackEnd.Main.MatchFacade;
import com.javlovers.bcfs.Screens.BackEnd.Threading.MotherThreadController;

import java.util.ArrayList;
import java.util.HashMap;

import static com.badlogic.gdx.math.MathUtils.random;

public class MakeCockScreen implements Screen {
    final BCFS game;
    OrthographicCamera camera;
    Stage stage;
    Skin skin;
    Skin customSkin;
    ButtonGroup<TextButton> cockAttacks;
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

    //
    Attack SelectedAttack;
    Cock tempCock;

    public MakeCockScreen(final BCFS gam) {
        game = gam;


        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage); // Set the stage as the input processor

        // Load the skin
        skin = new Skin(Gdx.files.internal("tracerui/tracer-ui.json"));
        customSkin = new Skin(Gdx.files.internal("custom_ui/custom_ui.json"));

        // Initialize
        table = new Table();
        sidebarTable = new Table();
        subTable = new Table();
        attackListTable = new Table();
        attackTraversalButtonBar = new Table();

        cockNameTextField = new TextField("", customSkin);
        cockNameTextField.setAlignment(Align.center);
        attacksText = new Label("ATTACKS", skin, "title");

        nameText = new Label("NAME: ", skin,"title");

        backButton = new TextButton("BACK", customSkin);
        saveButton = new TextButton("SAVE", customSkin);
        testButton = new TextButton("TEST", customSkin);

        cockAttacks = new ButtonGroup<>();
        cockAttacks.setMaxCheckCount(1);
        cockAttacks.setMinCheckCount(0);
        cockAttacks.add(new TextButton("ATTACK 1", skin, "toggle"));
        cockAttacks.add(new TextButton("ATTACK 2", skin, "toggle"));
        cockAttacks.add(new TextButton("ATTACK 3", skin, "toggle"));
        cockAttacks.add(new TextButton("ATTACK 4", skin, "toggle"));
        table.setFillParent(true);
        table.pad(25).left();

        DBHelpers dbh = new DBHelpers(DBHelpers.getGlobalConnection());
        HashMap<Integer,Cock> tempCocks= dbh.getAllCurrCockData();
        GlobalEntities.CurrentCock = tempCocks.get(GlobalEntities.currentUser.getUserID());
        Cock cock = GlobalEntities.CurrentCock;
        if(GlobalEntities.CurrentCock == null){
            tempCock = new Cock("",GlobalEntities.currentUser.getUserID());
            tempCock.setCockID(0);
        }else {
            tempCock = GlobalEntities.CurrentCock;
            cockNameTextField.setText(tempCock.getName());
            ArrayList<Attack> Atks = tempCock.getAttackList();
            for(int x = 0;x<Atks.size();x++){
                cockAttacks.getButtons().get(x).setText(Atks.get(x).getName());
            }
        }
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
        cockNameSubtable.add(cockNameTextField).width(250).height(50).left();
        sidebarTable.add(cockNameSubtable).center();
        sidebarTable.row();

        // Sidebar
        sidebarTable.add(attacksText).left().padTop(50).padBottom(25).colspan(2);
        sidebarTable.row();
        int ind = 0;
        for (TextButton button : cockAttacks.getButtons()) {
            sidebarTable.add(button).width(350).height(60).padBottom(buttonSpacing).left();
            sidebarTable.row();

            int finalInd = ind;
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // Handle Button Click
                    boolean isChecked = button.isChecked();
                    if (isChecked) {
                        createAttackList(0, finalInd);
                    } else {
                        hideAttackList();
                    }
                }
            });
            ind++;
        }

        subTable.add(saveButton).width(350).height(60).padRight(15).left();
//        subTable.add(testButton).width(170).height(60);
        sidebarTable.add(subTable).padTop(50).colspan(2).left();

        // Button Click Handling
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
                tempCock.setName(cockNameTextField.getText());
                GlobalEntities.CurrentCock = tempCock;
                DBHelpers dbh = new DBHelpers(DBHelpers.getGlobalConnection());
                dbh.sendToTempCock(tempCock);
                // Handle Save Button Click
                System.out.println("SAVE");
            }
        });

        testButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(GlobalEntities.CurrentCock == null) return;
                // Handle Test Button Click
                DBHelpers dbh = new DBHelpers(DBHelpers.getGlobalConnection());
                HashMap<Integer,Cock> AllC = dbh.getAllCockData();
                ArrayList<Integer> AC = new ArrayList<>(AllC.keySet());
                ArrayList<MatchFacade> Fights = new ArrayList<>();
                Cock currC = GlobalEntities.CurrentCock;
                AllC.put(0,currC);
                for(Integer CID : AC){
                    MatchFacade mf = new MatchFacade(CID,currC.getCockID(),AllC.get(CID).getCockID());
                    Fights.add(mf);
                } MotherThreadController MTC = new MotherThreadController(Fights,5,AllC);
                Thread MotherThread = new Thread(MTC);
                MotherThread.start();
                while(MotherThread.isAlive()){
                    System.out.println("Mother is Looping");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                HashMap<Integer,Integer> ResultFinal = MTC.getThreadResults();
                int wins = 0;
                for(Integer EnemyID : ResultFinal.keySet()){
                    if(ResultFinal.get(EnemyID) == 0) wins++;
                }
                System.out.println("Wins " + wins);
                System.out.println("Loses " + (ResultFinal.keySet().size() - wins));
                System.out.println("TEST");
            }
        });

        reloadAttackLabel();

        stage.addActor(table);
    }

    private void createAttackList(int pageNumber,int slotIndex) {
        hideAttackList();

        DBHelpers.setGlobalConnection(new LocalHostConnection());
        DBHelpers dbh = new DBHelpers(new LocalHostConnection());
        HashMap<Integer, Attack> allatk = dbh.getAllAttacksEnabled();

        int Maxpage =(int) Math.ceil(allatk.size()/9.0);
        if(pageNumber > Maxpage) pageNumber = 0;
        if(pageNumber < 0) pageNumber = Maxpage-1;
        ArrayList<Integer> Atkkeys = new ArrayList<>(allatk.keySet());

        ButtonGroup<TextButton> attacksButtonGroup = new ButtonGroup<>();

        TextButton prevAttacksButton = new TextButton("<", skin);
        TextButton selectAttackButton = new TextButton("SELECT", skin);
        TextButton nextAttacksButton = new TextButton(">", skin);

        attacksButtonGroup.setMaxCheckCount(1);
        attacksButtonGroup.setMinCheckCount(0);

        // fields for attackCard, insert values from attacksDB
        // Add field for attacktype, which defines the button skin to be used in the constructor

        // Attack Card Constructor
         for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                final int row = i;
                final int col = j;
                int atkIndex = ((row*3) + col) + ((pageNumber) * 9);
                System.out.println(atkIndex);
                TextButton attackCard = null;
                try {
                    int atkID = Atkkeys.get(atkIndex);
                    Attack curratk = allatk.get(atkID);
                    String attackName = curratk.getName();
                    System.out.println(attackName);
                    int param1 = curratk.getDamage();
                    double param2 = curratk.getDamageMultiplier();
                    int param3 = curratk.getSpeed();
                    attackCard = new TextButton(attackName + "\n\n\n" +
                            "IDamage:   " + atkID + "\n\n" +
                            "Multiplier:   " + param2 + "\n\n" +
                            "Speed:   " + param3 + "\n", skin, "toggle");

                    attackCard.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            // Handle Attack Card Click
                            System.out.println("ATTACK CARD [" + row + "][" + col + "]");
                            SelectedAttack = curratk.clone();
                        }
                    });
                    System.out.println("Slot Index "+ slotIndex);
                    if(tempCock.getAttackList().size() > slotIndex){
                        Attack atkSomething  = tempCock.getAttackList().get(slotIndex);
                        if(atkSomething.getAttackID() == atkID ){
                            attackCard.setChecked(true);
                        }
                    }
                }catch (IndexOutOfBoundsException ignored){
                    attackCard = new TextButton("",skin);
                }

                attackListTable.add(attackCard).width(175).height(180).pad(5);
                attacksButtonGroup.add(attackCard);
            }
            attackListTable.row();
        }

        attackTraversalButtonBar.add(prevAttacksButton).width(125).padRight(25);
        attackTraversalButtonBar.add(selectAttackButton).width(225).padRight(25);
        attackTraversalButtonBar.add(nextAttacksButton).width(125);
        attackListTable.add(attackTraversalButtonBar).colspan(3).pad(20);
        System.out.println("Page Number: " + pageNumber);
        int finalPageNumber = pageNumber;
        prevAttacksButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // add page traversal from attacks in DB
                // add attacks list traversal tracker and check index bounds
                System.out.println("PREV");
                createAttackList(finalPageNumber - 1,slotIndex);
            }
        });
        nextAttacksButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // add page traversal from attacks in DB
                // add attacks list traversal tracker and check index bounds
                System.out.println("NEXT");
                createAttackList(finalPageNumber + 1,slotIndex);
            }
        });

        selectAttackButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // set selected Attack to target cockAttacks slot
                System.out.println("SELECT");
                System.out.println("SELECTED ATTACK:\n" + attacksButtonGroup.getChecked());
                boolean success = tempCock.setAttack(SelectedAttack, slotIndex);
                if(!success){
                    cockAttacks.getButtons().get(tempCock.getAttackList().size()-1).setChecked(true);
                }
                reloadAttackLabel();
            }
        });
    }

    private void hideAttackList() {
        SelectedAttack = null;
        attackListTable.clear();
        attackTraversalButtonBar.clear();
    }

    private void reloadAttackLabel(){
        Cock curr = tempCock;
        Array<TextButton> atklabels =cockAttacks.getButtons();
        ArrayList<Attack> atkList = curr.getAttackList();
        for(int x = 0;x<atkList.size();x++){
            Attack currAtk = atkList.get(x);
            atklabels.get(x).setText(currAtk.getName());
        }
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
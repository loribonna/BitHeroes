package com.my.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.my.game.BitHeroes;
import com.my.game.tools.Interfaces.IEntity;
import com.my.game.tools.PlayScreen;

/**
 * Create menu screen with buttons to choose the player
 */

public class MenuScreen implements Screen {
    private Stage stage;
    private TextButton buttonWarrior;
    private TextButton buttonArcher;
    private TextButton buttonFireBender;
    private Skin skin;
    private TextureAtlas buttonAtlas;
    private Viewport port;
    private BitHeroes game;
    private Camera camera;
    private Texture background;

    /**
     * Create the MenuScreen with a background and a button for every playable entity.
     * @param game
     */
    public MenuScreen(final BitHeroes game){
        this.game=game;
        camera=new OrthographicCamera();
        port= new FitViewport(BitHeroes.V_WIDTH, BitHeroes.V_HEIGHT,camera);
        stage = new Stage(port);
        Gdx.input.setInputProcessor(stage);
        background=new Texture("schermata_menu.png");

        skin = new Skin();

        buttonAtlas = new TextureAtlas(Gdx.files.internal("buttons/buttons.pack"));
        skin.addRegions(buttonAtlas);
        TextButton.TextButtonStyle textButtonWarriorStyle;
        textButtonWarriorStyle = new TextButton.TextButtonStyle();
        textButtonWarriorStyle.font = new BitmapFont();
        textButtonWarriorStyle.up = skin.getDrawable("guerriero");
        buttonWarrior = new TextButton("", textButtonWarriorStyle);
        buttonWarrior.setBounds(BitHeroes.V_WIDTH / 2 - BitHeroes.V_WIDTH / 2.5f, BitHeroes.V_HEIGHT / 2 - BitHeroes.V_HEIGHT / 4, BitHeroes.V_WIDTH / 5, BitHeroes.V_HEIGHT / 5);
        buttonWarrior.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (event.getListenerActor() instanceof TextButton) {
                    TextButton t = ((TextButton) event.getListenerActor());
                    if (t.isChecked()) {
                        Gdx.app.log("MenuScreen", "checked");
                        dispose();
                        PlayScreen firstLevel = new com.my.game.screens.PlayScreens.FirstLevel(game, IEntity.PlayerName.WARRIOR,0);
                        game.changeLevel(firstLevel);
                    }
                }
                return false;
            }
        });

        TextButton.TextButtonStyle textButtonArcherStyle;
        textButtonArcherStyle = new TextButton.TextButtonStyle();
        textButtonArcherStyle.font = new BitmapFont();
        textButtonArcherStyle.up = skin.getDrawable("archer");
        buttonArcher = new TextButton("", textButtonArcherStyle);
        buttonArcher.setBounds(BitHeroes.V_WIDTH / 2 - BitHeroes.V_WIDTH / 10, BitHeroes.V_HEIGHT / 2 - BitHeroes.V_HEIGHT / 4, BitHeroes.V_WIDTH / 5, BitHeroes.V_HEIGHT / 5);
        buttonArcher.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (event.getListenerActor() instanceof TextButton) {
                    TextButton t = ((TextButton) event.getListenerActor());
                    if (t.isChecked()) {
                        Gdx.app.log("MenuScreen", "checked");
                        dispose();
                        PlayScreen firstLevel = new com.my.game.screens.PlayScreens.FirstLevel(game, IEntity.PlayerName.ARCHER,0);
                        game.changeLevel(firstLevel);
                    }
                }
                return false;
            }
        });

        TextButton.TextButtonStyle textButtonFireBenderStyle;
        textButtonFireBenderStyle = new TextButton.TextButtonStyle();
        textButtonFireBenderStyle.font = new BitmapFont();
        textButtonFireBenderStyle.up = skin.getDrawable("firebender");
        buttonFireBender = new TextButton("", textButtonFireBenderStyle);
        buttonFireBender.setBounds(BitHeroes.V_WIDTH / 2 + BitHeroes.V_WIDTH / 5, BitHeroes.V_HEIGHT / 2 - BitHeroes.V_HEIGHT / 4, BitHeroes.V_WIDTH / 5, BitHeroes.V_HEIGHT / 5);
        buttonFireBender.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (event.getListenerActor() instanceof TextButton) {
                    TextButton t = ((TextButton) event.getListenerActor());
                    if (t.isChecked()) {
                        Gdx.app.log("MenuScreen", "checked");
                        dispose();
                        PlayScreen firstLevel = new com.my.game.screens.PlayScreens.FirstLevel(game, IEntity.PlayerName.FIREBENDER,0);
                        game.changeLevel(firstLevel);
                    }
                }
                return false;
            }
        });

        stage.addActor(buttonFireBender);
        stage.addActor(buttonArcher);
        stage.addActor(buttonWarrior);
    }

    @Override
    public void show() {

    }

    /**
     * Renders the background and the stage containing the menu buttons.
     * @param delta
     */
    @Override
    public void render(float delta) {
        game.getBatch().setProjectionMatrix(camera.combined);
        game.getBatch().begin();
        game.getBatch().draw(background,0,0,camera.viewportWidth,camera.viewportHeight);
        game.getBatch().end();
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {

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

    /**
     * Dispose the current stage
     */
    @Override
    public void dispose() {
        stage.dispose();
    }
}

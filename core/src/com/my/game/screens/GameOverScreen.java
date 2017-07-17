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

/**
 * Created by lorib on 24/05/2017.
 */

public class GameOverScreen implements Screen {
    private Stage stage;
    private TextButton buttonExit;
    private TextButton buttonRestart;
    private Skin skin;
    private TextureAtlas buttonAtlas;
    private Viewport port;
    private BitHeroes game;
    private Camera camera;
    private Texture background;

    /**
     * Display the GameOver screen with the buttons Restart and Exit
     * @param game
     */
    public GameOverScreen(final BitHeroes game){
        this.game=game;
        camera=new OrthographicCamera();
        port= new FitViewport(BitHeroes.V_WIDTH, BitHeroes.V_HEIGHT,camera);
        stage = new Stage(port);
        Gdx.input.setInputProcessor(stage);

        skin = new Skin();

        background=new Texture("schermata_game_over.png");

        buttonAtlas = new TextureAtlas(Gdx.files.internal("buttons/buttons.pack"));
        skin.addRegions(buttonAtlas);
        TextButton.TextButtonStyle textButtonRestartStyle;
        textButtonRestartStyle = new TextButton.TextButtonStyle();
        textButtonRestartStyle.font = new BitmapFont();
        textButtonRestartStyle.up = skin.getDrawable("uscita");
        buttonExit = new TextButton("", textButtonRestartStyle);
        buttonExit.setBounds(BitHeroes.V_WIDTH/2- BitHeroes.V_WIDTH/10, BitHeroes.V_HEIGHT/2- BitHeroes.V_HEIGHT/3, BitHeroes.V_WIDTH/5, BitHeroes.V_HEIGHT/7);
        buttonExit.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if(event.getListenerActor() instanceof TextButton){
                    TextButton t =((TextButton)event.getListenerActor());
                    if(t.isChecked()){
                        dispose();
                        Gdx.app.exit();
                    }
                }
                return false;
            }
        });

        TextButton.TextButtonStyle textButtonExitStyle;
        textButtonExitStyle = new TextButton.TextButtonStyle();
        textButtonExitStyle.font = new BitmapFont();
        textButtonExitStyle.up = skin.getDrawable("ricomincia");
        buttonRestart = new TextButton("", textButtonExitStyle);
        buttonRestart.setBounds(BitHeroes.V_WIDTH/2- BitHeroes.V_WIDTH/10, BitHeroes.V_HEIGHT/2- BitHeroes.V_HEIGHT/7, BitHeroes.V_WIDTH/5, BitHeroes.V_HEIGHT/7);
        buttonRestart.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if(event.getListenerActor() instanceof TextButton){
                    TextButton t =((TextButton)event.getListenerActor());
                    if(t.isChecked()){
                        dispose();
                        Screen screen;
                        screen = new MenuScreen(game);
                        game.setScreen(screen);
                    }
                }
                return false;
            }
        });

        stage.addActor(buttonRestart);
        stage.addActor(buttonExit);
    }


    @Override
    public void show() {

    }

    /**
     * Renders background and buttons(actors) contained in the stage
     * @param delta
     */
    @Override
    public void render(float delta) {
        game.getBatch().setProjectionMatrix(camera.combined);
        game.getBatch().begin();
        game.getBatch().draw(background,0,0);
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

    @Override
    public void dispose() {
        stage.dispose();
    }
}


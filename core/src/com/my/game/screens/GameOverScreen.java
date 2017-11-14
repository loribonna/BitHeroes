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
 * GameOver screen with return and exit button
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

    private TextButton createButton(TextureAtlas atlas, String drawable, float positionY){
        skin.addRegions(atlas);
        TextButton.TextButtonStyle textButtonStyle;
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = new BitmapFont();
        textButtonStyle.up = skin.getDrawable(drawable);
        TextButton button = new TextButton("", textButtonStyle);
        button.setBounds(BitHeroes.V_WIDTH / 2 - BitHeroes.V_WIDTH / 10, BitHeroes.V_HEIGHT/2+ BitHeroes.V_HEIGHT/positionY, BitHeroes.V_WIDTH/5, BitHeroes.V_HEIGHT/7);
        button.addListener(new EventListener() {
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

        return button;
    }

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
        buttonExit = createButton(buttonAtlas,"uscita",-3);
        buttonRestart = createButton(buttonAtlas,"ricomincia",-7);

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


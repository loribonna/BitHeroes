package com.my.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.my.game.MyGame;

/**
 * Created by lorib on 16/07/2017.
 */

public class FinalScreen implements Screen {
    private Stage stage;
    private Viewport port;
    private MyGame game;
    private Camera camera;
    private Texture background;

    /**
     * Create the FinalScreen and jumps back in MenuScreen after 5 seconds.
     * @param game
     */
    public FinalScreen(final MyGame game){
        this.game=game;

        camera=new OrthographicCamera();
        port= new FitViewport(MyGame.V_WIDTH,MyGame.V_HEIGHT,camera);
        stage = new Stage(port);
        Gdx.input.setInputProcessor(stage);

        background=new Texture("schermata_finale.png");

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                dispose();
                Screen screen;
                screen = new MenuScreen(game);
                game.setScreen(screen);
            }
        },5);

    }

    @Override
    public void show() {

    }

    /**
     * Renders the background and the empty stage
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

    @Override
    public void dispose() {
        stage.dispose();
    }
}

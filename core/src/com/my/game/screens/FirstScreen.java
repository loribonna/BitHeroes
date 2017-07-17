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
 * First screen with play button
 */

public class FirstScreen implements Screen {
    private Stage stage;
    private TextButton button;
    private TextButton.TextButtonStyle textButtonStyle;
    private Skin skin;
    private TextureAtlas buttonAtlas;
    private Texture background;
    private Viewport port;
    private BitHeroes game;
    private Camera camera;

    /**
     * Create the initial screen of the game with the only Play-Button
     * @param game
     */
    public FirstScreen(final BitHeroes game) {
        this.game=game;
        camera=new OrthographicCamera();
        port= new FitViewport(BitHeroes.V_WIDTH, BitHeroes.V_HEIGHT,camera);
        stage = new Stage(port);
        Gdx.input.setInputProcessor(stage);

        background=new Texture("SchermataIniziale.png");

        skin = new Skin();
        buttonAtlas = new TextureAtlas(Gdx.files.internal("buttons/buttons.pack"));
        skin.addRegions(buttonAtlas);
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = new BitmapFont();
        textButtonStyle.up = skin.getDrawable("gioca");
        button = new TextButton("Button1", textButtonStyle);
        button.setBounds(BitHeroes.V_WIDTH/2- BitHeroes.V_WIDTH/10, BitHeroes.V_HEIGHT/2- BitHeroes.V_HEIGHT/4, BitHeroes.V_WIDTH/5, BitHeroes.V_HEIGHT/5);
        button.setText("");
        button.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if(event.getListenerActor() instanceof TextButton){
                    TextButton t =((TextButton)event.getListenerActor());
                    if(t.isChecked()){
                        Gdx.app.log("FirstScreen","checked");
                        dispose();
                        game.setScreen(new MenuScreen(game));
                    }
                }
                return false;
            }
        });
        stage.addActor(button);

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

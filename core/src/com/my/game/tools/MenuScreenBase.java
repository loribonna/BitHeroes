package com.my.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.my.game.BitHeroes;

import javax.xml.soap.Text;

/**
 * Created by lorib on 15/11/2017.
 */

public abstract class MenuScreenBase implements Screen {
    protected Stage stage;
    protected Skin skin;
    protected Viewport port;
    protected BitHeroes game;
    protected Camera camera;
    protected Texture background;
    protected TextureAtlas buttonAtlas;

    public MenuScreenBase(BitHeroes game,String buttonAtlasName,String backgroundTheme){
        this.game=game;
        this.buttonAtlas=new TextureAtlas(Gdx.files.internal(buttonAtlasName));
        this.camera=new OrthographicCamera();
        this.port= new FitViewport(BitHeroes.V_WIDTH, BitHeroes.V_HEIGHT,camera);
        this.stage = new Stage(port);
        Gdx.input.setInputProcessor(stage);
        this.background=new Texture(backgroundTheme);
        skin = new Skin();
        skin.addRegions(buttonAtlas);
        addActors(setupActors());
    }

    protected abstract TextButton[] setupActors();

    protected TextButton createButton(String drawable, AppConstants.Float2 position, AppConstants.Float2 sizeMultipliers, EventListener listener){
        TextButton.TextButtonStyle textButtonStyle;
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = new BitmapFont();
        textButtonStyle.up = skin.getDrawable(drawable);
        TextButton button = new TextButton("", textButtonStyle);
        button.setBounds(BitHeroes.V_WIDTH / 2 + BitHeroes.V_WIDTH / position.x, BitHeroes.V_HEIGHT/2+ BitHeroes.V_HEIGHT/position.y,
                (BitHeroes.V_WIDTH/5)*sizeMultipliers.x, (BitHeroes.V_HEIGHT/7)*sizeMultipliers.y);
        button.addListener(listener);
        return button;
    }

    protected void addActors(TextButton[] buttons){
        for(TextButton button : buttons){
            this.stage.addActor(button);
        }
    }
    @Override
    public void show() {

    }

    /**
     * Renders the background and the buttons in the stage
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

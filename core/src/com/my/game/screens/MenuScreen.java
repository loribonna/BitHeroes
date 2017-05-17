package com.my.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.my.game.MyGame;

/**
 * Created by lorib on 17/05/2017.
 */

public class MenuScreen implements Screen {
    Stage stage;
    TextButton button;
    TextButton.TextButtonStyle textButtonStyle;
    Skin skin;
    TextureAtlas buttonAtlas;
    Viewport port;

    public MenuScreen(final MyGame game){
        port= new FitViewport(MyGame.V_WIDTH,MyGame.V_HEIGHT,new OrthographicCamera());
        stage = new Stage(port);
        Gdx.input.setInputProcessor(stage);

        skin = new Skin();

        buttonAtlas = new TextureAtlas(Gdx.files.internal("buttons/buttons.pack"));
        skin.addRegions(buttonAtlas);
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = new BitmapFont();
        textButtonStyle.up = skin.getDrawable("up-button");
        textButtonStyle.down = skin.getDrawable("down-button");
        textButtonStyle.checked = skin.getDrawable("checked-button");
        button = new TextButton("Button1", textButtonStyle);
        button.setSize(MyGame.V_WIDTH,MyGame.V_HEIGHT);
        button.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if(event.getListenerActor() instanceof TextButton){
                    TextButton t =((TextButton)event.getListenerActor());
                    if(t.isChecked()){
                        Gdx.app.log("MenuScreen","checked");
                        dispose();
                        game.setScreen(new PlayScreen(game));
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

    @Override
    public void render(float delta) {
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

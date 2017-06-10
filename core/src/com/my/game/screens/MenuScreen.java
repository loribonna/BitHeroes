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
    MyGame game;
    Camera camera;
    Texture background;
    public MenuScreen(final MyGame game){
        this.game=game;

        camera=new OrthographicCamera();
        port= new FitViewport(MyGame.V_WIDTH,MyGame.V_HEIGHT,camera);
        stage = new Stage(port);
        Gdx.input.setInputProcessor(stage);

        background=new Texture("schermata finale.png");

        skin = new Skin();

        buttonAtlas = new TextureAtlas(Gdx.files.internal("buttons/buttons.pack"));
        skin.addRegions(buttonAtlas);
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = new BitmapFont();
        textButtonStyle.up = skin.getDrawable("up-button");
        textButtonStyle.down = skin.getDrawable("down-button");
        textButtonStyle.checked = skin.getDrawable("checked-button");
        button = new TextButton("Button1", textButtonStyle);
        button.setBounds(MyGame.V_WIDTH/2-MyGame.V_WIDTH/10,MyGame.V_HEIGHT/2-MyGame.V_HEIGHT/4,MyGame.V_WIDTH/5,MyGame.V_HEIGHT/5);
        button.setText("");
        button.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if(event.getListenerActor() instanceof TextButton){
                    TextButton t =((TextButton)event.getListenerActor());
                    if(t.isChecked()){
                        Gdx.app.log("MenuScreen","checked");
                        dispose();
                        MyGame.currentPlayScreen=0;
                        game.changeLevel(new FirstLevel(game,"warrior"),"warrior");
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
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(background,0,0);
        game.batch.end();
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

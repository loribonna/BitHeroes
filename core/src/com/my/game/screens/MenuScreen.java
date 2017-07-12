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
    TextButton buttonWarrior;
    TextButton buttonArcher;
    TextButton buttonFireBender;
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

        buttonWarrior = new TextButton("Warrior", textButtonStyle);
        buttonWarrior.setBounds(MyGame.V_WIDTH / 2 - MyGame.V_WIDTH / 2.5f, MyGame.V_HEIGHT / 2 - MyGame.V_HEIGHT / 4, MyGame.V_WIDTH / 5, MyGame.V_HEIGHT / 5);
        buttonWarrior.setText("Warrior");
        buttonWarrior.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (event.getListenerActor() instanceof TextButton) {
                    TextButton t = ((TextButton) event.getListenerActor());
                    if (t.isChecked()) {
                        Gdx.app.log("MenuScreen", "checked");
                        dispose();
                        Screen firstLevel = new FirstLevel(game, "warrior");
                        MyGame.currentPlayScreen = '1';
                        game.setScreen(firstLevel);
                    }
                }
                return false;
            }
        });

        buttonArcher = new TextButton("Archer", textButtonStyle);
        buttonArcher.setBounds(MyGame.V_WIDTH / 2 - MyGame.V_WIDTH / 10, MyGame.V_HEIGHT / 2 - MyGame.V_HEIGHT / 4, MyGame.V_WIDTH / 5, MyGame.V_HEIGHT / 5);
        buttonArcher.setText("Archer");
        buttonArcher.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (event.getListenerActor() instanceof TextButton) {
                    TextButton t = ((TextButton) event.getListenerActor());
                    if (t.isChecked()) {
                        Gdx.app.log("MenuScreen", "checked");
                        dispose();
                        Screen firstLevel = new FirstLevel(game, "archer");
                        MyGame.currentPlayScreen = '1';
                        game.setScreen(firstLevel);
                    }
                }
                return false;
            }
        });

        buttonFireBender = new TextButton("FireBender", textButtonStyle);
        buttonFireBender.setBounds(MyGame.V_WIDTH / 2 +MyGame.V_WIDTH / 5, MyGame.V_HEIGHT / 2 - MyGame.V_HEIGHT / 4, MyGame.V_WIDTH / 5, MyGame.V_HEIGHT / 5);
        buttonFireBender.setText("FireBender");
        buttonFireBender.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (event.getListenerActor() instanceof TextButton) {
                    TextButton t = ((TextButton) event.getListenerActor());
                    if (t.isChecked()) {
                        Gdx.app.log("MenuScreen", "checked");
                        dispose();
                        Screen firstLevel = new FirstLevel(game, "firebender");
                        MyGame.currentPlayScreen = '1';
                        game.setScreen(firstLevel);
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

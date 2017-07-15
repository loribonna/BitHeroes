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
import com.my.game.tools.PlayScreen;

/**
 * Created by lorib on 17/05/2017.
 */

public class MenuScreen implements Screen {
    Stage stage;
    TextButton buttonWarrior;
    TextButton buttonArcher;
    TextButton buttonFireBender;

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

        background=new Texture("schermata_menu.png");

        skin = new Skin();

        buttonAtlas = new TextureAtlas(Gdx.files.internal("buttons/buttons.pack"));
        skin.addRegions(buttonAtlas);
        TextButton.TextButtonStyle textButtonWarriorStyle;
        textButtonWarriorStyle = new TextButton.TextButtonStyle();
        textButtonWarriorStyle.font = new BitmapFont();
        textButtonWarriorStyle.up = skin.getDrawable("guerriero");
        buttonWarrior = new TextButton("", textButtonWarriorStyle);
        buttonWarrior.setBounds(MyGame.V_WIDTH / 2 - MyGame.V_WIDTH / 2.5f, MyGame.V_HEIGHT / 2 - MyGame.V_HEIGHT / 4, MyGame.V_WIDTH / 5, MyGame.V_HEIGHT / 5);
        buttonWarrior.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (event.getListenerActor() instanceof TextButton) {
                    TextButton t = ((TextButton) event.getListenerActor());
                    if (t.isChecked()) {
                        Gdx.app.log("MenuScreen", "checked");
                        dispose();
                        PlayScreen firstLevel = new com.my.game.screens.PlayScreens.FirstLevel(game, "warrior",0);
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
        buttonArcher.setBounds(MyGame.V_WIDTH / 2 - MyGame.V_WIDTH / 10, MyGame.V_HEIGHT / 2 - MyGame.V_HEIGHT / 4, MyGame.V_WIDTH / 5, MyGame.V_HEIGHT / 5);
        buttonArcher.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (event.getListenerActor() instanceof TextButton) {
                    TextButton t = ((TextButton) event.getListenerActor());
                    if (t.isChecked()) {
                        Gdx.app.log("MenuScreen", "checked");
                        dispose();
                        PlayScreen firstLevel = new com.my.game.screens.PlayScreens.FirstLevel(game, "archer",0);
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
        buttonFireBender.setBounds(MyGame.V_WIDTH / 2 +MyGame.V_WIDTH / 5, MyGame.V_HEIGHT / 2 - MyGame.V_HEIGHT / 4, MyGame.V_WIDTH / 5, MyGame.V_HEIGHT / 5);
        buttonFireBender.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (event.getListenerActor() instanceof TextButton) {
                    TextButton t = ((TextButton) event.getListenerActor());
                    if (t.isChecked()) {
                        Gdx.app.log("MenuScreen", "checked");
                        dispose();
                        PlayScreen firstLevel = new com.my.game.screens.PlayScreens.FirstLevel(game, "firebender",0);
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

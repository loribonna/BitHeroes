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
import com.my.game.tools.PlayScreen;

/**
 * Create menu screen with buttons to choose the player
 */

public class MenuScreen implements Screen {
    private Stage stage;
    private Skin skin;
    private TextureAtlas buttonAtlas;
    private Viewport port;
    private BitHeroes game;
    private Camera camera;
    private Texture background;

    private TextButton createButton(TextureAtlas atlas, String drawable, final IEntity.PlayerName playerName, float positionX){
        skin.addRegions(atlas);
        TextButton.TextButtonStyle textButtonStyle;
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = new BitmapFont();
        textButtonStyle.up = skin.getDrawable(drawable);
        TextButton button = new TextButton("", textButtonStyle);
        button.setBounds(BitHeroes.V_WIDTH / 2 + (BitHeroes.V_WIDTH / (positionX)), BitHeroes.V_HEIGHT / 2 - BitHeroes.V_HEIGHT / 4, BitHeroes.V_WIDTH / 5, BitHeroes.V_HEIGHT / 5);
        button.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (event.getListenerActor() instanceof TextButton) {
                    TextButton t = ((TextButton) event.getListenerActor());
                    if (t.isChecked()) {
                        dispose();
                        PlayScreen firstLevel = new com.my.game.screens.PlayScreens.FirstLevel(game, playerName,0);
                        game.changeLevel(firstLevel);
                    }
                }
                return false;
            }
        });
        return button;
    }

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

        TextButton buttonWarrior;
        TextButton buttonArcher;
        TextButton buttonFireBender;

        buttonAtlas = new TextureAtlas(Gdx.files.internal("buttons/buttons.pack"));
        buttonWarrior=createButton(buttonAtlas,"guerriero",IEntity.PlayerName.WARRIOR,-2.5f);
        buttonArcher=createButton(buttonAtlas,"archer",IEntity.PlayerName.ARCHER,-10f);
        buttonFireBender=createButton(buttonAtlas,"firebender",IEntity.PlayerName.FIREBENDER,5f);

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

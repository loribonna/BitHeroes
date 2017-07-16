package com.my.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Disableable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.my.game.MyGame;
import com.my.game.screens.PlayScreens.FirstLevel;
import com.my.game.screens.PlayScreens.SecondLevel;

/**
 * Created by lorib on 04/05/2017.
 */

public class Hud implements Disposable{
    /**
     * The stage that will be displayed in the current playing screen
     */
    private Stage stage;
    public Viewport port;
    private int score;
    private Label scoreLabel;
    private Label levelLabel;
    private Label gameLabel;
    private Label lifeLabel;
    private Camera guicam;

    /**
     * Create a hud with a own camera to dislay above the current playing screen.
     * @param game
     * @param level: Current playing level
     * @param initialScore: Value to initialize scores.
     */
    public Hud(MyGame game,int level,int initialScore){
        score = initialScore;
        guicam=new OrthographicCamera();
        port= new FitViewport(MyGame.V_WIDTH,MyGame.V_HEIGHT,guicam);
        stage = new Stage(port,game.getBatch());

        Table t = new Table();
        t.top();
        t.setFillParent(true);

        scoreLabel= new Label(String.format("%06d",score),new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levelLabel= new Label(String.valueOf(level),new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        gameLabel= new Label(game.name,new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        lifeLabel= new Label("",new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        t.add(gameLabel).expandX().padTop(10);
        t.add(levelLabel).expandX().padTop(10);
        t.row();
        t.add(scoreLabel).expandX();
        t.add(lifeLabel).expandX();

        stage.addActor(t);
    }

    /**
     * @return current stage
     */
    public Stage getStage(){
        return stage;
    }

    /**
     * Display player's current life.
     * @param life
     */
    public void setPlayerLife(int life){
        lifeLabel.setText(String.valueOf(life));
    }

    /**
     * Increase current score value of the value of a coin
     */
    public void addCoin(){
        this.score+=100;
        scoreLabel.setText(String.format("%06d",score));
    }

    /**
     * @return Current displaying scores
     */
    public int getScore(){
        return score;
    }

    /**
     * Dispose current stage
     */
    @Override
    public void dispose() {
        stage.dispose();
    }
}

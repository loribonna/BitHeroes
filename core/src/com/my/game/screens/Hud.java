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
    public Stage stage;
    public Viewport port;

    private int score;

    private Label scoreLabel;
    private Label levelLabel;
    private Label gameLabel;

    private Camera guicam;

    public Hud(MyGame game,int level){
        score = 0;
        guicam=new OrthographicCamera();
        port= new FitViewport(MyGame.V_WIDTH,MyGame.V_HEIGHT,guicam);
        stage = new Stage(port,game.getBatch());

        Table t = new Table();
        t.top();
        t.setFillParent(true);

        scoreLabel= new Label(String.format("%06d",score),new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levelLabel= new Label(String.valueOf(level),new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        gameLabel= new Label(game.name,new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        t.add(gameLabel).expandX().padTop(10);
        t.add(levelLabel).expandX().padTop(10);
        t.row();
        t.add(scoreLabel).expandX();


        stage.addActor(t);
    }

    public void addCoin(){
        this.score+=100;
        scoreLabel.setText(String.format("%06d",score));
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}

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

/**
 * Created by lorib on 04/05/2017.
 */

public class Hud implements Disposable{
    public Stage stage;
    public Viewport port;

    private int worldTimer;
    private float timeCount;
    private int score;

    Label countDownLabel;
    Label scoreLabel;
    Label timeLabel;
    Label levelLabel;
    Label worldLabel;
    Label myLabel;

    Camera guicam;

    Rectangle wleftBounds;
    Rectangle wrightBounds;

    public Hud(SpriteBatch sb){
        wleftBounds = new Rectangle(0, 0, 80, 80);
        wrightBounds = new Rectangle(Gdx.graphics.getWidth()-80, 0, 80, 80);

        worldTimer = 300;
        timeCount = 0;
        score = 0;

        guicam=new OrthographicCamera();

        port= new FitViewport(MyGame.V_WIDTH,MyGame.V_HEIGHT,guicam);
        stage = new Stage(port,sb);

        Table t = new Table();
        t.top();
        t.setFillParent(true);

        countDownLabel = new Label(String.format("%03d",worldTimer),new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel= new Label(String.format("%06d",score),new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel= new Label("TIME",new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levelLabel= new Label("1",new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        myLabel= new Label("Nome Gioco",new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        t.add(myLabel).expandX().padTop(10);
        t.add(timeLabel).expandX().padTop(10);
        t.row();
        t.add(scoreLabel).expandX();
        t.add(levelLabel).expandX();
        t.add(countDownLabel).expandX();

        stage.addActor(t);
    }

    public void update(){
        Vector3 touchPoint = new Vector3();
        for (int i=0; i<5; i++){
            if (!Gdx.input.isTouched(i)) continue;
            guicam.unproject(touchPoint.set(Gdx.input.getX(i), Gdx.input.getY(i), 0));
            if (wleftBounds.contains(touchPoint.x, touchPoint.y)){
                Gdx.app.log("left","Touch");
                //Move your player to the left!
            }else if (wrightBounds.contains(touchPoint.x, touchPoint.y)){
                Gdx.app.log("right","Touch");
                //Move your player to the right!
            }
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}

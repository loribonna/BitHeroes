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
import com.my.game.tools.AppConstants;
import com.my.game.tools.MenuScreenBase;

/**
 * GameOver screen with return and exit button
 */

public class GameOverScreen extends MenuScreenBase {

    public GameOverScreen(final BitHeroes game){
        super(game,"buttons/buttons.pack","schermata_game_over.png");
    }

    protected TextButton[] setupActors(){


        TextButton buttonExit = createButton("uscita",new AppConstants.Float2(-10,-3),new AppConstants.Float2(1,1),new EventListener() {
            @Override
            public boolean handle(Event event) {
                if(event.getListenerActor() instanceof TextButton){
                    TextButton t =((TextButton)event.getListenerActor());
                    if(t.isChecked()){
                        dispose();
                        Gdx.app.exit();
                    }
                }
                return false;
            }
        });

        TextButton buttonRestart = createButton("ricomincia",new AppConstants.Float2(-10,-7),new AppConstants.Float2(1,1),new EventListener() {
            @Override
            public boolean handle(Event event) {
                if(event.getListenerActor() instanceof TextButton){
                    TextButton t =((TextButton)event.getListenerActor());
                    if(t.isChecked()){
                        dispose();
                        Screen screen;
                        screen = new MenuScreen(game);
                        game.setScreen(screen);
                    }
                }
                return false;
            }
        });

        return new TextButton[]{buttonExit,buttonRestart};
    }

}


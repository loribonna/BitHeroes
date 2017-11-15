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
import com.my.game.tools.PlayScreen;

/**
 * Create menu screen with buttons to choose the player
 */

public class MenuScreen extends MenuScreenBase {

    public MenuScreen(final BitHeroes game){
        super(game,"buttons/buttons.pack","schermata_menu.png");
    }

    @Override
    protected TextButton[] setupActors() {
        TextButton buttonWarrior;
        TextButton buttonArcher;
        TextButton buttonFireBender;

        buttonWarrior=createButton("guerriero",new AppConstants.Float2(-2.5f,-4),new AppConstants.Float2(1,1.5f),new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (event.getListenerActor() instanceof TextButton) {
                    TextButton t = ((TextButton) event.getListenerActor());
                    if (t.isChecked()) {
                        dispose();
                        PlayScreen firstLevel = new com.my.game.screens.PlayScreens.FirstLevel(game, AppConstants.PlayerName.WARRIOR,0);
                        game.changeLevel(firstLevel);
                    }
                }
                return false;
            }
        });

        buttonArcher=createButton("archer",new AppConstants.Float2(-10f,-4),new AppConstants.Float2(1,1.5f),new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (event.getListenerActor() instanceof TextButton) {
                    TextButton t = ((TextButton) event.getListenerActor());
                    if (t.isChecked()) {
                        dispose();
                        PlayScreen firstLevel = new com.my.game.screens.PlayScreens.FirstLevel(game, AppConstants.PlayerName.ARCHER,0);
                        game.changeLevel(firstLevel);
                    }
                }
                return false;
            }
        });

        buttonFireBender=createButton("firebender",new AppConstants.Float2(5f,-4),new AppConstants.Float2(1,1.5f),new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (event.getListenerActor() instanceof TextButton) {
                    TextButton t = ((TextButton) event.getListenerActor());
                    if (t.isChecked()) {
                        dispose();
                        PlayScreen firstLevel = new com.my.game.screens.PlayScreens.FirstLevel(game, AppConstants.PlayerName.FIREBENDER,0);
                        game.changeLevel(firstLevel);
                    }
                }
                return false;
            }
        });

        return new TextButton[]{buttonArcher,buttonFireBender,buttonWarrior};
    }
}

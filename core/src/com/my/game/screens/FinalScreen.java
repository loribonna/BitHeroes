package com.my.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.my.game.BitHeroes;
import com.my.game.tools.AppConstants;
import com.my.game.tools.MenuScreenBase;

/**
 * Final screen with return and exit button
 */

public class FinalScreen extends MenuScreenBase {
    private TextButton buttonExit;
    private TextButton buttonRestart;
    private TextureAtlas buttonAtlas;

    /**
     * Display the final screen with the buttons Restart and Exit
     * @param game
     */
    public FinalScreen(final BitHeroes game){
        super(game,"buttons/buttons.pack","schermata_finale.png");
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

        TextButton buttonRestart=createButton("ricomincia",new AppConstants.Float2(-10,-7),new AppConstants.Float2(1,1),new EventListener() {
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

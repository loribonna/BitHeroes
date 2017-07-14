package com.my.game.sprites.TileObjects;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.my.game.MyGame;
import com.my.game.screens.PlayScreens.FirstLevel;
import com.my.game.screens.PlayScreens.SecondLevel;
import com.my.game.screens.PlayScreens.ThirdLevel;
import com.my.game.tools.Entity;
import com.my.game.tools.TileObject;

/**
 * Created by lorib on 10/06/2017.
 */

public class Exit extends TileObject {
    public Exit(World world, TiledMap map, Rectangle rect, MyGame game) {
        super(world, map, rect,game);
    }

    @Override
    public void update(float delta) {}

    @Override
    public void onHit(Entity entity) {
        if(game.getCurrentPlayScreen() instanceof FirstLevel){
            game.changeLevel(new SecondLevel(game,game.currentPlayer));
        }
        else if(game.getCurrentPlayScreen() instanceof SecondLevel){
            game.changeLevel(new ThirdLevel(game,game.currentPlayer));
        }else{
            //TODO: Display credits
        }
    }
}
package com.my.game.tools;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.my.game.MyGame;
import com.my.game.screens.SecondLevel;

/**
 * Created by lorib on 10/06/2017.
 */

public class Exit extends TileObject {
    public Exit(World world, TiledMap map, Rectangle rect) {
        super(world, map, rect);
    }

    @Override
    public void onHit(Object entity) {
        MyGame.currentPlayScreen++;
        MyGame.current.changeLevel(new SecondLevel(MyGame.current,MyGame.current.currentPlayer),"warrior");
    }
}

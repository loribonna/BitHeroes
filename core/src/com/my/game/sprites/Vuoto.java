package com.my.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.my.game.MyGame;
import com.my.game.tools.Enemy;
import com.my.game.tools.Entity;
import com.my.game.tools.PlayScreen;
import com.my.game.tools.TileObject;

/**
 * Created by lorib on 17/05/2017.
 */

public class Vuoto extends TileObject {
    public Vuoto(World world, TiledMap map, Rectangle rect) {
        super(world, map, rect);
        setCategoryBits(MyGame.VOID_BIT);
    }

    @Override
    public void update() {}


    @Override
    public void onHit(Entity entity) {
        if(!entity.isPlayer) {
            entity.destroy();
        }else{
            Gdx.app.log("Vuoto", "");
            PlayScreen.current.gameOver();
        }
    }
}

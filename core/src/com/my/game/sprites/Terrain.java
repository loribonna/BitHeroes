package com.my.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.my.game.MyGame;
import com.my.game.tools.TileObject;
import com.sun.org.apache.xpath.internal.operations.String;

/**
 * Created by lorib on 13/05/2017.
 */

public class Terrain extends TileObject {

    public Terrain(World world, TiledMap map, Rectangle rect) {
        super(world, map, rect);
        setCategoryBits(MyGame.DEFAULT_BIT);
    }

    /**
     *
     * @param entity: If String is the contact point with player, else is Enemy.
     */
    @Override
    public void onHit(Object entity) {
        if(entity instanceof String)
            Gdx.app.log("","Terrain");
    }

}

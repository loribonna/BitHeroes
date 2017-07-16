package com.my.game.sprites.TileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.my.game.MyGame;
import com.my.game.tools.Enemy;
import com.my.game.tools.Entity;
import com.my.game.tools.PlayScreen;
import com.my.game.tools.TileObject;
import com.sun.org.apache.xpath.internal.operations.String;

/**
 * Created by lorib on 13/05/2017.
 */

public class Terrain extends TileObject {
    /**
     * Create a Terrain TileObject
     * @param world
     * @param rect
     * @param game
     */
    public Terrain(World world, Rectangle rect, MyGame game) {
        super(world, rect,game);
        setCategoryBits(MyGame.DEFAULT_BIT);
    }

    @Override
    public void update(float delta) {}

    /**
     * If a Enemy hits the ground then can jump again.
     * @param entity: Enemy or Player
     */
    @Override
    public void onHit(Entity entity) {
        if(!entity.isPlayer()) {
            ((Enemy) entity).setFlying(false);
        }
    }

}

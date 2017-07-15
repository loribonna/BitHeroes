package com.my.game.sprites.TileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.my.game.MyGame;
import com.my.game.tools.Entity;
import com.my.game.tools.PlayScreen;
import com.my.game.tools.TileObject;

/**
 * Created by lorib on 13/05/2017.
 */

public class Brick extends TileObject {
    public Brick(World world, TiledMap map, Rectangle rect, MyGame game) {
        super(world, map, rect,game);
        setCategoryBits(MyGame.BRICK_BIT);

    }

    @Override
    public void update(float delta) {}

    @Override
    public void onHit(Entity entity) {

    }
}

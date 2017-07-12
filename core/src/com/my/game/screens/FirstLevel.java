package com.my.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.my.game.MyGame;
import com.my.game.sprites.Archer;
import com.my.game.sprites.Orch;
import com.my.game.sprites.Warrior;
import com.my.game.tools.*;
import com.my.game.tools.PlayScreen;

/**
 * Created by lorib on 03/05/2017.
 */

public class FirstLevel extends PlayScreen {

    /**
     * Initialize game world and any entity
     *
     * @param game Reference to main game instance
     */
    public FirstLevel(final MyGame game,String player) {
        super(game);
        map=mapLoader.load("livello1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / MyGame.PPM);

        new B2WorldCreator(world,map,animatedTileObjects);

        atlOrch = new TextureAtlas("warrior.pack");

        if(player=="warrior") {
            atlPlayer = new TextureAtlas("warrior.pack");
            this.player = new Warrior(world, getAtlasPlayer(), new Vector2(100, 64));
        }
        if(player=="archer") {
            atlPlayer = new TextureAtlas("archer.pack");
            this.player = new Archer(world, getAtlasPlayer(), new Vector2(100, 64));
        }
        if(player=="firebender") {
            atlPlayer = new TextureAtlas("warrior.pack");
            this.player = new Warrior(world, getAtlasPlayer(), new Vector2(100, 64));
        }

        enemyList.add(new Orch(world,getAtlasOrch(),new Vector2(150,64)));

        world.setContactListener(new WorldContactListener());

        current=this;
    }

}

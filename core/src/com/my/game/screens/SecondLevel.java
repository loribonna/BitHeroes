package com.my.game.screens;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.my.game.MyGame;
import com.my.game.sprites.Orch;
import com.my.game.sprites.Warrior;
import com.my.game.tools.B2WorldCreator;
import com.my.game.tools.PlayScreen;
import com.my.game.tools.WorldContactListener;

/**
 * Created by lorib on 30/05/2017.
 */

public class SecondLevel extends PlayScreen{

    /**
     * Initialize game world and any entity
     *
     * @param game Reference to main game instance
     */
    public SecondLevel(final MyGame game,String player) {
        super(game);
        map=mapLoader.load("livello2.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / MyGame.PPM);

        new B2WorldCreator(world,map,animatedTileObjects);

        if(player=="warrior") {
            atl = new TextureAtlas("warrior.pack");
            this.player = new Warrior(world, getAtlas(), new Vector2(100, 64));
        }

        enemyList.add(new Orch(world,getAtlas(),new Vector2(150,64)));

        world.setContactListener(new WorldContactListener());

        current=this;
    }
}

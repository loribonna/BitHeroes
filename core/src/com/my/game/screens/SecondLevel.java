package com.my.game.screens;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.my.game.MyGame;
import com.my.game.sprites.Archer;
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
        MyGame.currentPlayScreen = 2;
        MyGame.currentPlayer=player;
        map=mapLoader.load("livello2.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / MyGame.PPM);

        new B2WorldCreator(world,map,animatedTileObjects);

        if(player=="warrior") {
            atlPlayer = new TextureAtlas("warriorP/warrior.pack");
            this.player = new Warrior(world, getAtlasPlayer(), new Vector2(100, 64));
        }
        if(player=="archer") {
            atlPlayer = new TextureAtlas("archerP/archer.pack");
            this.player = new Archer(world, getAtlasPlayer(), new Vector2(100, 64));
        }
        if(player=="firebender") {
            atlPlayer = new TextureAtlas("aceP/ace.pack");
            this.player = new Warrior(world, getAtlasPlayer(), new Vector2(100, 64));
        }

        world.setContactListener(new WorldContactListener());

        current=this;
    }
}

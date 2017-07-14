package com.my.game.screens.PlayScreens;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.my.game.MyGame;
import com.my.game.screens.Hud;
import com.my.game.sprites.Players.Archer;
import com.my.game.sprites.Players.FireBender;
import com.my.game.sprites.Players.Warrior;
import com.my.game.tools.B2WorldCreator;
import com.my.game.tools.PlayScreen;
import com.my.game.tools.WorldContactListener;

/**
 * Created by lorib on 30/05/2017.
 */

public class ThirdLevel extends PlayScreen {

    /**
     * Initialize game world and any entity
     *
     * @param game Reference to main game instance
     */
    public ThirdLevel(final MyGame game,String player) {
        super(game);
        hud=new Hud(game,3);
        game.setCurrentPlayScreen(this);
        game.currentPlayer=player;
        map=mapLoader.load("livello3.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / MyGame.PPM);

        new B2WorldCreator(world,map,animatedTileObjects,game);

        if(player=="warrior") {
            atlPlayer = new TextureAtlas("warriorP/warrior.pack");
            this.player = new Warrior(world, getAtlasPlayer(), new Vector2(100, 64),game);
        }
        if(player=="archer") {
            atlPlayer = new TextureAtlas("archerP/archer.pack");
            this.player = new Archer(world, getAtlasPlayer(), new Vector2(100, 64),game);
        }
        if(player=="firebender") {
            atlPlayer = new TextureAtlas("aceP/ace.pack");
            this.player = new FireBender(world, getAtlasPlayer(), new Vector2(100, 64),game);
        }

        world.setContactListener(new WorldContactListener(game));
    }
}

package com.my.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.my.game.MyGame;
import com.my.game.sprites.Archer;
import com.my.game.sprites.FireBender;
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
        MyGame.currentPlayScreen = 1;
        MyGame.currentPlayer=player;
        map=mapLoader.load("livello1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / MyGame.PPM);

        new B2WorldCreator(world,map,animatedTileObjects);

        atlOrch = new TextureAtlas("orcoP/orc.pack");
        atlBat = new TextureAtlas("pipistrelloP/bat.pack");
        atlGolem = new TextureAtlas("golemP/GolemPack.pack");
        atlLizard = new TextureAtlas("lucertolaP/lucertola.pack");
        atlMummy = new TextureAtlas("mummiaP/mummia.pack");
        atlSkeleton = new TextureAtlas("skeletonP/scheletro.pack");

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
            this.player = new FireBender(world, getAtlasPlayer(), new Vector2(100, 64));
        }

        enemyList.add(new Orch(world,getAtlasOrch(),new Vector2(150,64)));

        world.setContactListener(new WorldContactListener());

        current=this;
    }

}

package com.my.game.screens.PlayScreens;

import static com.badlogic.gdx.math.MathUtils.random;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.my.game.MyGame;
import com.my.game.screens.Hud;
import com.my.game.sprites.Enemies.Bat;
import com.my.game.sprites.Enemies.Golem;
import com.my.game.sprites.Players.Archer;
import com.my.game.sprites.Players.FireBender;
import com.my.game.sprites.Enemies.Orch;
import com.my.game.sprites.Players.Warrior;
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
        hud=new Hud(game,1);
        game.setCurrentPlayScreen(this);
        game.setCurrentPlayer(player);
        map=mapLoader.load("livello1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / MyGame.PPM);

        int randomUntil=2;
        int Random;

        new B2WorldCreator(world,map,animatedTileObjects,game);

        atlOrch = new TextureAtlas("orcoP/orc.pack");
        atlBat = new TextureAtlas("pipistrelloP/bat.pack");
        atlGolem = new TextureAtlas("golemP/GolemPack.pack");

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

        MapLayer l;
        l = map.getLayers().get(8);
        for (MapObject obj : l.getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) obj).getRectangle();
            Random=random.nextInt(randomUntil);
            if (Random==0){
                enemyList.add(new Orch(world,getAtlasOrch(),new Vector2(rect.getX(),rect.getY()),game));
            }else if(Random==1){
                enemyList.add(new Bat(world,getAtlasBat(),new Vector2(rect.getX(),rect.getY()),game));
            }else if(Random==2){
                enemyList.add(new Golem(world,getAtlasGolem(),new Vector2(rect.getX(),rect.getY()),game));
            }
        }

        world.setContactListener(new WorldContactListener(game));
    }

}

package com.my.game.screens.PlayScreens;

import static com.badlogic.gdx.math.MathUtils.random;

import com.badlogic.gdx.Gdx;
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
import com.my.game.sprites.Enemies.Lizard;
import com.my.game.sprites.Enemies.Mummy;
import com.my.game.sprites.Enemies.Orch;
import com.my.game.sprites.Enemies.Skeleton;
import com.my.game.sprites.Players.Archer;
import com.my.game.sprites.Players.FireBender;
import com.my.game.sprites.Players.Warrior;
import com.my.game.tools.B2WorldCreator;
import com.my.game.tools.PlayScreen;
import com.my.game.tools.WorldContactListener;

import java.util.Random;

/**
 * Created by lorib on 30/05/2017.
 */

public class SecondLevel extends PlayScreen{

    /**
     * Initialize game world and any entity
     *
     * @param game Reference to main game instance
     */
    public SecondLevel(final MyGame game,String player,int score) {
        super(game);
        hud=new Hud(game,2,score);
        game.setCurrentPlayScreen(this);
        game.setCurrentPlayer(player);
        map=mapLoader.load("livello2.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / MyGame.PPM);

        int randomUntil=3;
        int Random;

        new B2WorldCreator(world,map,animatedTileObjects,game);

        atlLizard = new TextureAtlas("lucertolaP/lucertola.pack");
        atlMummy = new TextureAtlas("mummiaP/mummia.pack");
        atlSkeleton = new TextureAtlas("skeletonP/scheletro.pack");

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
        l = map.getLayers().get(7);
        for (MapObject obj : l.getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) obj).getRectangle();
            Random=random.nextInt(randomUntil);
            if (Random==0){
                enemyList.add(new Skeleton(world,getAtlasSkeleton(),new Vector2(rect.getX(),rect.getY()),game));
            }else if(Random==1){
                enemyList.add(new Lizard(world,getAtlasLizard(),new Vector2(rect.getX(),rect.getY()),game));
            }else if(Random==2){
                enemyList.add(new Mummy(world,getAtlasMummy(),new Vector2(rect.getX(),rect.getY()),game));
            }
        }

        world.setContactListener(new WorldContactListener(game));
    }
}

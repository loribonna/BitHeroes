package com.my.game.screens.PlayScreens;

import static com.badlogic.gdx.math.MathUtils.random;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.my.game.BitHeroes;
import com.my.game.screens.Hud;
import com.my.game.sprites.Enemies.Golem;
import com.my.game.sprites.Enemies.Mummy;
import com.my.game.sprites.Players.Archer;
import com.my.game.sprites.Players.FireBender;
import com.my.game.sprites.Enemies.Orch;
import com.my.game.sprites.Players.Warrior;
import com.my.game.tools.*;
import com.my.game.tools.Interfaces.EntityInterface;
import com.my.game.tools.PlayScreen;

/**
 * First level screen
 */

public class FirstLevel extends PlayScreen {

    /**
     * Initialize game world and any every entity. Sets initial score.
     * Enemies are created randomly in positions defined in the level map.
     * @param game
     * @param player
     * @param score
     */
    public FirstLevel(final BitHeroes game, EntityInterface.PlayerName player, int score) {
        super(game);
        hud=new Hud(game,1,score);
        game.setCurrentPlayScreen(this);
        game.setCurrentPlayer(player);
        map=mapLoader.load("livello1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / BitHeroes.PPM);

        int randomUntil=3;
        int Random;

        new B2WorldCreator(world,map,animatedTileObjects,game);

        atlOrch = new TextureAtlas("orcoP/orc.pack");
        atlGolem = new TextureAtlas("golemP/GolemPack.pack");
        atlMummy = new TextureAtlas("mummiaP/mummia.pack");

        if(player==EntityInterface.PlayerName.WARRIOR) {
            atlPlayer = new TextureAtlas("warriorP/warrior.pack");
            this.player = new Warrior(world, getAtlasPlayer(), new Vector2(100, 64),game);
        }
        if(player==EntityInterface.PlayerName.ARCHER) {
            atlPlayer = new TextureAtlas("archerP/archer.pack");
            this.player = new Archer(world, getAtlasPlayer(), new Vector2(100, 64),game);
        }
        if(player==EntityInterface.PlayerName.FIREBENDER) {
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
                enemyList.add(new Mummy(world,getAtlasMummy(),new Vector2(rect.getX(),rect.getY()),game));
            }else if(Random==2){
                enemyList.add(new Golem(world,getAtlasGolem(),new Vector2(rect.getX(),rect.getY()),game));
            }
        }

        world.setContactListener(new WorldContactListener(game));

        music = game.getManager().get("sounds/musica.wav",Music.class);
        music.setLooping(true);
        music.setVolume(0.5f);
        music.play();
    }

}

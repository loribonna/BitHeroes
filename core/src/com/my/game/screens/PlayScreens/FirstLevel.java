package com.my.game.screens.PlayScreens;

import com.badlogic.gdx.Gdx;
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
import com.my.game.sprites.Enemies.Orch;
import com.my.game.tools.AppConstants;
import com.my.game.tools.B2WorldCreator;
import com.my.game.tools.PlayScreen;

import static com.badlogic.gdx.math.MathUtils.random;

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
    public FirstLevel(final BitHeroes game, AppConstants.PlayerName player, int score) {
        super(game,player);
        hud=new Hud(game,1,score);
        game.setCurrentPlayScreen(this);
        game.setCurrentPlayer(player);
        map=mapLoader.load("livello1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / BitHeroes.PPM);

        int randomUntil=3;
        int Random;

        new B2WorldCreator(world,map,animatedTileObjects,game);

        atlOrch = new TextureAtlas("orcoP/orc.pack");
        atlGolem = new TextureAtlas("golemP/golemPack.pack");
        atlMummy = new TextureAtlas("mummiaP/mummia.pack");

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



    }

    protected void playMusic(){
        try {
            music = game.getManager().get("sounds/musica.wav", Music.class);
            music.setLooping(true);
            music.setVolume(0.5f);
            music.play();
        }catch (Exception e){
            Gdx.app.log("Error","audio file not found");
        }
    }

}

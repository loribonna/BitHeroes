package com.my.game.screens.PlayScreens;

import static com.badlogic.gdx.math.MathUtils.random;

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
import com.my.game.sprites.Enemies.Blob;
import com.my.game.sprites.Enemies.Lizard;
import com.my.game.sprites.Enemies.Skeleton;
import com.my.game.sprites.Players.Archer;
import com.my.game.sprites.Players.FireBender;
import com.my.game.sprites.Players.Warrior;
import com.my.game.tools.AppConstants;
import com.my.game.tools.B2WorldCreator;
import com.my.game.tools.PlayScreen;
import com.my.game.tools.WorldContactListener;

/**
 * Second level screen
 */

public class SecondLevel extends PlayScreen{

    /**
     * Initialize game world and any every entity. Sets initial score.
     * Enemies are created randomly in positions defined in the level map.
     * @param game
     * @param player
     * @param score
     */
    public SecondLevel(final BitHeroes game, AppConstants.PlayerName player, int score) {
        super(game,player);
        hud=new Hud(game,2,score);
        game.setCurrentPlayScreen(this);
        game.setCurrentPlayer(player);
        map=mapLoader.load("livello2.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / BitHeroes.PPM);

        int randomUntil=3;
        int Random;

        new B2WorldCreator(world,map,animatedTileObjects,game);

        atlLizard = new TextureAtlas("lucertolaP/lucertola.pack");
        atlBlob = new TextureAtlas("blobP/blob.pack");
        atlSkeleton = new TextureAtlas("skeletonP/scheletro.pack");

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
                enemyList.add(new Blob(world,getAtlasBlob(),new Vector2(rect.getX(),rect.getY()),game));
            }
        }
    }

    @Override
    protected void playMusic() {
        try{
            music = game.getManager().get("sounds/musica.wav",Music.class);
            music.setLooping(true);
            music.setVolume(0.5f);
            music.play();
        }catch (Exception e){
            Gdx.app.log("Error","audio file not found");
        }
    }
}

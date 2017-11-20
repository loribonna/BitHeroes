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
import com.my.game.sprites.Enemies.Dragon;
import com.my.game.tools.AppConstants;
import com.my.game.tools.B2WorldCreator;
import com.my.game.tools.PlayScreen;

/**
 * Third level screen
 */

public class ThirdLevel extends PlayScreen {

    /**
     * Initialize game world and any every entity. Sets initial score.
     * The only enemy is created in the position defined in the level map.
     * @param game
     * @param player
     * @param score
     */
    public ThirdLevel(final BitHeroes game, AppConstants.PlayerName player, int score) {
        super(game,player);
        hud=new Hud(game,3,score);
        game.setCurrentPlayScreen(this);
        game.setCurrentPlayer(player);
        map=mapLoader.load("livello3.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / BitHeroes.PPM);

        new B2WorldCreator(world,map,animatedTileObjects,game);

        atlDragon = new TextureAtlas("dragonP/dragon.pack");

        MapLayer l;
        l = map.getLayers().get(3);
        for (MapObject obj : l.getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) obj).getRectangle();
            enemyList.add(new Dragon(world,getAtlasDragon(),new Vector2(rect.getX(),rect.getY()),game));
        }


    }

    @Override
    protected void playMusic() {
        try{
            music = game.getManager().get("sounds/bossfight.mp3",Music.class);
            music.setLooping(true);
            music.setVolume(0.5f);
            music.play();
        }catch (Exception e){
            Gdx.app.log("Error","audio file not found");
        }
    }
}

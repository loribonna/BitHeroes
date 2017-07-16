package com.my.game.screens.PlayScreens;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.my.game.MyGame;
import com.my.game.screens.Hud;
import com.my.game.sprites.Enemies.Dragon;
import com.my.game.sprites.Players.Archer;
import com.my.game.sprites.Players.FireBender;
import com.my.game.sprites.Players.Warrior;
import com.my.game.tools.B2WorldCreator;
import com.my.game.tools.Interfaces.EntityInterface;
import com.my.game.tools.PlayScreen;
import com.my.game.tools.WorldContactListener;

/**
 * Created by lorib on 30/05/2017.
 */

public class ThirdLevel extends PlayScreen {

    /**
     * Initialize game world and any every entity. Sets initial score.
     * The only enemy is created in the position defined in the level map.
     * @param game
     * @param player
     * @param score
     */
    public ThirdLevel(final MyGame game, EntityInterface.PlayerName player, int score) {
        super(game);
        hud=new Hud(game,3,score);
        game.setCurrentPlayScreen(this);
        game.setCurrentPlayer(player);
        map=mapLoader.load("livello3.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / MyGame.PPM);

        new B2WorldCreator(world,map,animatedTileObjects,game);

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

        atlDragon = new TextureAtlas("dragonP/dragon.pack");

        MapLayer l;
        l = map.getLayers().get(3);
        for (MapObject obj : l.getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) obj).getRectangle();
            enemyList.add(new Dragon(world,getAtlasDragon(),new Vector2(rect.getX(),rect.getY()),game));
        }

        world.setContactListener(new WorldContactListener(game));
    }
}

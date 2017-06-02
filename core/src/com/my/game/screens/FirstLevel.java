package com.my.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.my.game.MyGame;
import com.my.game.scenes.Hud;
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
        map=mapLoader.load("livello1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / MyGame.PPM);

        new B2WorldCreator(world,map,animatedTileObjects);

        if(player=="warrior") {
            atl = new TextureAtlas("warrior.pack");
            this.player = new Warrior(world, getAtlas(), new Vector2(100, 64));
        }

        enemyList.add(new Orch(world,getAtlas(),new Vector2(150,64)));

        world.setContactListener(new WorldContactListener());

        current=this;
    }

}

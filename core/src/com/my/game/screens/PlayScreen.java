package com.my.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
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
import com.my.game.sprites.Warrior;
import com.my.game.tools.B2WorldCreator;

/**
 * Created by lorib on 03/05/2017.
 */

public class PlayScreen implements Screen {
    private MyGame g;
    private OrthographicCamera camera;
    private Viewport port;
    private Hud hud;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    private World world;
    private Box2DDebugRenderer b2dr;

    private TextureAtlas atl;

    private Warrior player;

    public PlayScreen(MyGame g) {
        this.g=g;
        atl = new TextureAtlas("warrior.pack");
        camera=new OrthographicCamera();
        port=new FitViewport(MyGame.V_WIDTH / MyGame.PPM,MyGame.V_HEIGHT / MyGame.PPM,camera);
        hud=new Hud(g.batch);
        mapLoader=new TmxMapLoader();
        map=mapLoader.load("level1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / MyGame.PPM);
        //camera.position.set((MyGame.V_WIDTH / MyGame.PPM)/2,(MyGame.V_HEIGHT / MyGame.PPM)/2 ,0);
        camera.position.set(port.getScreenWidth()/2,port.getScreenHeight()/2,0);

        world=new World(new Vector2(0,-10),true);
        b2dr=new Box2DDebugRenderer();

        new B2WorldCreator(world,map);

        player = new Warrior(world,this);


    }

    public TextureAtlas getAtlas(){
        return this.atl;
    }

    public void handleInput(float dt){
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            //camera.position.x+=100*dt;
            player.body.applyLinearImpulse(new Vector2(0,4f),player.body.getWorldCenter(),true);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.body.getLinearVelocity().x<=2){
            player.body.applyLinearImpulse(new Vector2(0.1f,0),player.body.getWorldCenter(),true);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.body.getLinearVelocity().x>=-2){
            player.body.applyLinearImpulse(new Vector2(-0.1f,0),player.body.getWorldCenter(),true);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            player.attack();
        }
    }

    public void update(float dt){

        handleInput(dt);

        camera.position.x = player.body.getPosition().x;

        player.update(dt);

        world.step(1/60f,6,2);

        camera.update();
        mapRenderer.setView(camera);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(1,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mapRenderer.render();

        b2dr.render(world,camera.combined);

        g.batch.setProjectionMatrix(camera.combined);
        g.batch.begin();
        player.draw(g.batch);
        g.batch.end();

        g.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        port.update(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        mapRenderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}

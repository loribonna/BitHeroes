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
import com.my.game.sprites.Orch;
import com.my.game.sprites.Warrior;
import com.my.game.tools.B2WorldCreator;
import com.my.game.tools.Entity;
import com.my.game.tools.EntityInterface;
import com.my.game.tools.WorldContactListener;

/**
 * Created by lorib on 03/05/2017.
 */

public class PlayScreen implements Screen {
    public static PlayScreen current;

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

    private Entity player;
    private Entity enemy;
    /**
     * Initialize game world and any entity
     * @param g Reference to main game instance
     */
    public PlayScreen(MyGame g) {
        this.g=g;
        atl = new TextureAtlas("warrior.pack");
        camera=new OrthographicCamera();
        port=new FitViewport(MyGame.V_WIDTH / MyGame.PPM,MyGame.V_HEIGHT / MyGame.PPM,camera);
        hud=new Hud(g.batch);
        mapLoader=new TmxMapLoader();
        map=mapLoader.load("livello1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / MyGame.PPM);
        camera.position.set(port.getWorldWidth()/2,port.getWorldHeight()/2,0);

        world=new World(new Vector2(0,-10),true);
        b2dr=new Box2DDebugRenderer();

        new B2WorldCreator(world,map);

        enemy = new Orch(world,this,new Vector2(150,64));
        player = new Warrior(world,this,new Vector2(100,64));

        world.setContactListener(new WorldContactListener());

        current=this;
    }

    /**
     * @return Current TextureAtlas
     */
    public TextureAtlas getAtlas(){
        return this.atl;
    }

    /**
     * Process Inputs from Keyboard or Touch
     * @param dt: Current DeltaTime between frame calls
     */
    public void handleInput(float dt){
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)
                && player.getState()!= EntityInterface.State.JUMP
                && player.getState()!= EntityInterface.State.FALL
                && player.getState()!= EntityInterface.State.ATTACK){
            playerJump();
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

    public void gameOver(){
        //TODO:Schermata finale
        Gdx.app.exit();
    }

    /**
     * @param dt: Current DeltaTime between frame calls.
     */
    public void update(float dt){

        handleInput(dt);

        camera.position.x = player.body.getPosition().x;

        player.update(dt);
        enemy.update(dt);

        world.step(1/60f,6,2);

        camera.update();
        mapRenderer.setView(camera);
    }

    @Override
    public void show() {

    }

    public EntityInterface.State getPlayerState(){
        return this.player.currentState;
    }

    /**
     * This method is called once every frame call.
     * @param delta: Current DeltaTime between this frame call and the previous.
     */
    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(1,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mapRenderer.render();

        b2dr.render(world,camera.combined);

        g.batch.setProjectionMatrix(camera.combined);
        g.batch.begin();
        enemy.draw(g.batch);
        player.draw(g.batch);
        g.batch.end();

        g.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    public void playerJump(){
        player.body.applyLinearImpulse(new Vector2(0,4f),player.body.getWorldCenter(),true);
    }

    /**
     * Resize current ViewPort.
     * @param width
     * @param height
     */
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

    /**
     * Dispose unused texture or data.
     */
    @Override
    public void dispose() {
        map.dispose();
        mapRenderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}

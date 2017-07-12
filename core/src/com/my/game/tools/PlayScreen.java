package com.my.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.my.game.MyGame;
import com.my.game.screens.Hud;
import com.my.game.screens.GameOverScreen;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by lorib on 29/05/2017.
 */

public abstract class PlayScreen implements Screen{
    public boolean gameOver=false;
    public static PlayScreen current;
    protected final MyGame game;
    protected OrthographicCamera camera;
    protected Viewport port;
    protected Hud hud;
    protected TmxMapLoader mapLoader;
    protected TiledMap map;
    protected OrthogonalTiledMapRenderer mapRenderer;
    protected World world;
    protected Box2DDebugRenderer b2dr;
    protected ArrayList<Enemy> enemyList;
    protected Entity player;
    protected ArrayList<Bullet> bullets;
    protected ArrayList<TileObject> animatedTileObjects;
    public ArrayList<Object> objectsToRemove;
    protected TextureAtlas atlOrch;
    protected TextureAtlas atlPlayer;
    protected TextureAtlas atlSkeleton;
    /**
     * Initialize game world and any entity
     * @param game Reference to main game instance
     */
    public PlayScreen(MyGame game){
        this.game=game;
        objectsToRemove=new ArrayList<Object>();
        camera=new OrthographicCamera();
        port=new FitViewport(MyGame.V_WIDTH / MyGame.PPM,MyGame.V_HEIGHT / MyGame.PPM,camera);
        hud=new Hud(game.batch);
        mapLoader=new TmxMapLoader();
        camera.position.set(port.getWorldWidth()/2,port.getWorldHeight()/2,0);

        world=new World(new Vector2(0,-10),true);
        b2dr=new Box2DDebugRenderer();
        enemyList=new ArrayList<Enemy>();
        animatedTileObjects=new ArrayList<TileObject>();
        bullets=new ArrayList<Bullet>();
    }

    public TextureAtlas getAtlasPlayer(){
        return this.atlPlayer;
    }

    public TextureAtlas getAtlasOrch(){
        return this.atlOrch;
    }

    public TextureAtlas getAtlasSkeleton(){
        return this.atlSkeleton;
    }

    /**
     * Add a bullet in the current world.
     * @param bullet
     */
    public void addBullet(Bullet bullet){
        this.bullets.add(bullet);
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

        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.body.getLinearVelocity().x<=1){
            player.body.applyLinearImpulse(new Vector2(0.1f,0),player.body.getWorldCenter(),true);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.body.getLinearVelocity().x>=-1){
            player.body.applyLinearImpulse(new Vector2(-0.1f,0),player.body.getWorldCenter(),true);

        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            player.throwAttack(EntityInterface.AttackType.FIRST);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT)){
            player.throwAttack(EntityInterface.AttackType.SECOND);
        }
    }

    /**
     * @return: Current player state.
     */
    public EntityInterface.State getPlayerState(){
        return this.player.currentState;
    }

    /**
     * Perform current player jump
     */
    public void playerJump(){
        player.body.applyLinearImpulse(new Vector2(0,4f),player.body.getWorldCenter(),true);
    }

    /**
     * @return: Current player body position.
     */
    public Vector2 getPlayerPosition(){
        return player.getPosition();
    }

    /**
     * This method is called once every frame call.
     * Update Entities and AnimatedTileObject actions and animations.
     * @param delta: Current DeltaTime between this frame call and the previous.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(!gameOver) {
            update(delta);
            mapRenderer.render();

            b2dr.render(world, camera.combined);

            game.batch.setProjectionMatrix(camera.combined);
            game.batch.begin();
            for(Enemy enemy : enemyList){
                enemy.draw(game.batch);
            }
            for(Bullet bullet : bullets){
            //    bullet.draw(game.batch);
            }
            for(TileObject object : animatedTileObjects){
           //     object.draw(game.batch);
            }
            player.draw(game.batch);
            game.batch.end();

            game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
            hud.stage.draw();
        }
    }


    /**
     * Udate Entity and TiledObject. Handle player inputs.
     * @param dt: Current DeltaTime between frame calls.
     */
    public void update(float dt) {
        handleInput(dt);
        hud.update();

        camera.position.x = player.body.getPosition().x;

        player.update(dt);
        for(Enemy enemy : enemyList){
            enemy.update(dt);
        }

        for(Bullet bullet : bullets){
            bullet.update(dt);
        }

        for(TileObject tileObject : animatedTileObjects){
            tileObject.update();
        }

        world.step(1 / 60f, 6, 2);

        camera.update();
        mapRenderer.setView(camera);

        sweepDeadBodies();

    }

    /**
     * Instantiate the GameOver screen and dispose current bodies.
     */
    public void gameOver(){
        game.setScreen(new GameOverScreen(game));
        gameOver = true;
        dispose();
    }

    /**
     * Since LibGdx doesn't like if a body gets removed inside world simulation,
     * this must be done outside. Check if body has flag "isFlaggedForDelete" and remove
     * bodies awaiting for deletion.
     */
    public void sweepDeadBodies() {
        Array<Body> bodies = new Array<Body>();
        world.getBodies(bodies);
        for (Iterator<Object> iter = objectsToRemove.iterator(); iter.hasNext(); ) {
            Object obj = iter.next();
            if (obj != null) {
                if(obj instanceof Bullet) {
                    bullets.remove((Bullet) obj);
                }
                if(obj instanceof TileObject) {
                    animatedTileObjects.remove((TileObject) obj);
                }
                if(obj instanceof Enemy) {
                    enemyList.remove((Enemy) obj);
                }
            }
        }
        for (Iterator<Body> iter = bodies.iterator(); iter.hasNext(); ) {
            Body body = iter.next();
            if (body != null) {
                if (body.getUserData() instanceof Boolean && ((Boolean) body.getUserData())) {
                    for(Fixture f : body.getFixtureList()){
                        body.destroyFixture(f);
                    }
                    body.setUserData(null);
                    body = null;
                }
            }
        }
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

    @Override
    public void show() {

    }

    /**
     * Dispose all enemies and objects.
     */
    @Override
    public void dispose() {
        Array<Body> bodies=new Array<Body>();
        world.getBodies(bodies);
        for(Body b : bodies){
            b.setUserData(true);
        }
        animatedTileObjects.clear();
        enemyList.clear();
        bullets.clear();
        map.dispose();
        //mapRenderer.dispose();
        //world.dispose();
        //b2dr.dispose();
        hud.dispose();
    }
}

package com.my.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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
import com.my.game.BitHeroes;
import com.my.game.screens.Hud;
import com.my.game.screens.GameOverScreen;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Abstract class with PlayScreen controls
 */

public abstract class PlayScreen implements Screen{
    protected boolean gameOver=false;
    protected final BitHeroes game;
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
    protected TextureAtlas atlBat;
    protected TextureAtlas atlGolem;
    protected TextureAtlas atlLizard;
    protected TextureAtlas atlMummy;
    protected TextureAtlas atlDragon;
    protected TextureAtlas atlBlob;
    protected Music music;

    /**
     * Initialize game world and play game music.
     * @param game Reference to main game instance
     */
    public PlayScreen(BitHeroes game){
        this.game=game;
        objectsToRemove=new ArrayList<Object>();
        camera=new OrthographicCamera();
        port=new FitViewport(BitHeroes.V_WIDTH / BitHeroes.PPM, BitHeroes.V_HEIGHT / BitHeroes.PPM,camera);

        mapLoader=new TmxMapLoader();
        camera.position.set(port.getWorldWidth()/2,port.getWorldHeight()/2,0);

        world=new World(new Vector2(0,-10),true);
        b2dr=new Box2DDebugRenderer();
        enemyList=new ArrayList<Enemy>();
        animatedTileObjects=new ArrayList<TileObject>();
        bullets=new ArrayList<Bullet>();
    }

    /**
     * @return current score from the hud
     */
    public int getCurrentScore(){
        return hud.getScore();
    }

    /**
     * @return blob TextureAtlas
     */
    public TextureAtlas getAtlasBlob() {
        return this.atlBlob;
    }

    /**
     * @return player TextureAtlas
     */
    public TextureAtlas getAtlasPlayer(){
        return this.atlPlayer;
    }

    /**
     * @return orch TextureAtlas
     */
    public TextureAtlas getAtlasOrch(){
        return this.atlOrch;
    }

    /**
     * @return skeleton TextureAtlas
     */
    public TextureAtlas getAtlasSkeleton(){
        return this.atlSkeleton;
    }

    /**
     * @return bat TextureAtlas
     */
    public TextureAtlas getAtlasBat(){
        return this.atlBat;
    }

    /**
     * @return golem TextureAtlas
     */
    public TextureAtlas getAtlasGolem(){
        return this.atlGolem;
    }

    /**
     * @return lizard TextureAtlas
     */
    public TextureAtlas getAtlasLizard(){
        return this.atlLizard;
    }

    /**
     * @return mummy TextureAtlas
     */
    public TextureAtlas getAtlasMummy(){
        return this.atlMummy;
    }

    /**
     * @return dragon TextureAtlas
     */
    public TextureAtlas getAtlasDragon(){ return this.atlDragon; }

    /**
     * Add a bullet in the bullet list
     * @param bullet
     */
    public void addBullet(Bullet bullet){
        this.bullets.add(bullet);
    }

    /**
     * Process Inputs from Keyboard
     * @param dt Current DeltaTime between frame calls
     */
    public void handleInput(float dt){
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)
                && player.getState()!= com.my.game.tools.Interfaces.IEntity.State.JUMP
                && player.getState()!= com.my.game.tools.Interfaces.IEntity.State.FALL
                && player.getState()!= com.my.game.tools.Interfaces.IEntity.State.ATTACK){
            playerJump();
        }

        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.body.getLinearVelocity().x<=1){
            player.body.applyLinearImpulse(new Vector2(0.1f,0),player.body.getWorldCenter(),true);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.body.getLinearVelocity().x>=-1){
            player.body.applyLinearImpulse(new Vector2(-0.1f,0),player.body.getWorldCenter(),true);

        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            player.throwAttack(com.my.game.tools.Interfaces.IFight.AttackType.MELEE);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT)){
            player.throwAttack(com.my.game.tools.Interfaces.IFight.AttackType.MELEE);
        }
    }

    /**
     * Makes the current player jump
     */
    public void playerJump(){
        player.body.applyLinearImpulse(new Vector2(0,4f),player.body.getWorldCenter(),true);
    }

    /**
     * @return current player body position.
     */
    public Vector2 getPlayerPosition(){
        return player.getPosition();
    }

    /**
     * This method is called once every frame call.
     * Draws the animations for the Entities and TileObjects.
     * Draw the hud and current camera view.
     * @param delta Current DeltaTime between this frame call and the previous.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(!gameOver) {
            update(delta);
            mapRenderer.render();

            // Show border around shapes if in Debug
            //b2dr.render(world, camera.combined);

            game.getBatch().setProjectionMatrix(camera.combined);
            game.getBatch().begin();
            for(Enemy enemy : enemyList){
                enemy.draw(game.getBatch());
            }
            for(Bullet bullet : bullets){
                bullet.draw(game.getBatch());
            }
            for(TileObject object : animatedTileObjects){
                object.draw(game.getBatch());
            }
            player.draw(game.getBatch());
            game.getBatch().end();

            hud.getStage().draw();
        }
    }

    /**
     * Increase the score in the add of the value of a coin
     */
    public void addCoin(){
        hud.addCoin();
    }

    /**
     * Update Entities and AnimatedTileObject actions and animations.
     * Handle player input and clean object to dispose.
     * @param dt Current DeltaTime between frame calls.
     */
    public void update(float dt) {
        handleInput(dt);

        camera.position.x = player.body.getPosition().x;

        player.update(dt);
        for(Enemy enemy : enemyList){
            enemy.update(dt);
        }

        for(Bullet bullet : bullets){
            bullet.update(dt);
        }

        for(TileObject tileObject : animatedTileObjects){
            tileObject.update(dt);
        }

        hud.setPlayerLife(player.getLife());

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
     * this must be done outside.
     * Check bodies in current world and in bodiesToRemove list.
     * In the first case the body gets removed if has a flag for deletion.
     * In the second case the object gets removed from the update list.
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
     * Flags all bodies in the current world and dispose used resources.
     */
    @Override
    public void dispose() {
        try{
            music.setLooping(false);
            music.stop();
        }catch (Exception e){
            Gdx.app.log("Error",e.getMessage());
        }
        Array<Body> bodies=new Array<Body>();
        world.getBodies(bodies);
        for(Body b : bodies){
            b.setUserData(true);
        }
        animatedTileObjects.clear();
        enemyList.clear();
        bullets.clear();
        map.dispose();
        hud.dispose();
    }
}

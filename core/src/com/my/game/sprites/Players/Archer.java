package com.my.game.sprites.Players;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.my.game.BitHeroes;
import com.my.game.sprites.Throwables.Arrow;
import com.my.game.tools.Entity;

/**
 * Create a playable Archer entity
 */

public class Archer extends Entity {

    /**
     * Create a Archer Entity with player controls
     * @param world
     * @param screenAtlas
     * @param position
     * @param game
     */
    public Archer(World world, TextureAtlas screenAtlas,Vector2 position,BitHeroes game) {
        super(world, screenAtlas,position,game);
        life=150;
    }

    /**
     * Import enity-specific animations from the Archer atlas.
     * @param atlas
     */
    @Override
    public void getAnimations(TextureAtlas atlas) {
        standAnimation = new TextureRegion(atlas.findRegion("archer_normal_attack"), 7, 6, 34, 32);
        setBounds(0, 0, 24 / BitHeroes.PPM, 20 / BitHeroes.PPM);
        setRegion(standAnimation);
        currentState = State.STAND;
        previusState = State.STAND;
        stateTimer = 0;
        runRight = true;
        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(atlas.findRegion("archer_walking"), 23, 10, 27, 32));
        frames.add(new TextureRegion(atlas.findRegion("archer_walking"), 52, 10, 27, 32));
        frames.add(new TextureRegion(atlas.findRegion("archer_walking"), 90, 10, 27, 32));
        frames.add(new TextureRegion(atlas.findRegion("archer_walking"), 121, 10, 27, 32));

        runAnimation = new Animation(0.1f, frames);
        frames.clear();

        frames.add(new TextureRegion(atlas.findRegion("archer_normal_attack"), 7, 6, 34, 32));
        frames.add(new TextureRegion(atlas.findRegion("archer_normal_attack"), 45, 6, 34, 32));
        frames.add(new TextureRegion(atlas.findRegion("archer_normal_attack"), 84, 6, 34, 32));
        frames.add(new TextureRegion(atlas.findRegion("archer_normal_attack"), 123, 6, 34, 32));
        frames.add(new TextureRegion(atlas.findRegion("archer_normal_attack"), 164, 6, 34, 32));

        throwAnimation = new Animation(0.1f, frames);
        frames.clear();
    }

    /**
     * Update current state and animations.
     * @param delta
     */
    @Override
    public void update(float delta) {
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(delta));
    }

    /**
     * Perform shake movement after hit.
     */
    @Override
    public void recoil() {
        if(currentState!=State.JUMP) {
            body.applyLinearImpulse(new Vector2(0, 1), body.getWorldCenter(), true);
        }
    }

    /**
     * If the player gets destroyed the game is over
     */
    @Override
    public void destroy() {
        music = game.getManager().get("sounds/morte.wav",Music.class);
        music.setLooping(false);
        music.setVolume(1);
        music.play();
        game.getCurrentPlayScreen().gameOver();
    }

    /**
     * Add a bullet in the currentPlayScreen
     */
    private void throwBullet() {
        game.getCurrentPlayScreen().addBullet(new Arrow(getPosition(), world, isFlipX(),true,game));
    }

    /**
     * @return player filter bits to set collisions.
     */
    @Override
    public Filter getFilter() {
        Filter f = new Filter();
        f.categoryBits = BitHeroes.PLAYER_BIT;
        f.maskBits =(BitHeroes.DEFAULT_BIT | BitHeroes.BRICK_BIT | BitHeroes.COIN_BIT | BitHeroes.ENEMY_BIT |
                BitHeroes.VOID_BIT | BitHeroes.WALL_BIT | BitHeroes.EXIT_BIT | BitHeroes.ENEMY_BULLET_BIT | BitHeroes.ENEMY_MELEE_BIT);
        f.groupIndex = BitHeroes.GROUP_PLAYER;
        return f;
    }

    /**
     * Set fixtures in the current body.
     */
    @Override
    public void createBorders() {
        FixtureDef fdef = new FixtureDef();
        Filter filter = getFilter();

        fdef.filter.groupIndex=filter.groupIndex;
        fdef.filter.categoryBits= filter.categoryBits;
        fdef.filter.maskBits=filter.maskBits;

        CircleShape bShape = new CircleShape();
        bShape.setRadius(6/ BitHeroes.PPM);
        fdef.shape=bShape;
        body.createFixture(fdef).setUserData(this);

        EdgeShape feet = new EdgeShape();
        feet.set(new Vector2(-4,-6).scl(1/ BitHeroes.PPM),new Vector2(4,-6).scl(1/ BitHeroes.PPM));
        fdef.shape = feet;
        fdef.isSensor=true;
        body.createFixture(fdef).setUserData("good_feet");

    }

    /**
     * Replace the first attack with a distance attack
     */
    @Override
    public void firstAttack() {
        currentState = State.THROW;
        previusState = State.THROW;
        stateTimer = 0;
        setRegion(getFrame(0));

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                lockAttack=false;
            }
        },throwAnimation.getAnimationDuration());

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                music = game.getManager().get("sounds/freccia.wav",Music.class);
                music.setLooping(false);
                music.setVolume(1);
                music.play();
                throwBullet();
            }
        },throwAnimation.getAnimationDuration()/2);

    }

}
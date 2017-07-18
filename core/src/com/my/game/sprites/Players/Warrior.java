package com.my.game.sprites.Players;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.my.game.BitHeroes;
import com.my.game.tools.Entity;


/**
 * Create a playable Warrior entity
 */

public class Warrior extends Entity {

    /**
     * Create a Archer Warrior with player controls
     * @param world
     * @param screenAtlas
     * @param position
     * @param game
     */
    public Warrior(World world, TextureAtlas screenAtlas,Vector2 position,BitHeroes game) {
        super(world, screenAtlas,position,game);
        life=480;
    }

    /**
     * Import enity-specific animations from the Warrior atlas.
     * @param atlas
     */
    @Override
    public void getAnimations(TextureAtlas atlas) {
        standAnimation = new TextureRegion(atlas.findRegion("warrior_idle"), 0, 0, 14, 16);
        setBounds(0, 0, 16 / BitHeroes.PPM, 16 / BitHeroes.PPM);
        setRegion(standAnimation);
        currentState = State.STAND;
        previusState = State.STAND;
        stateTimer = 0;
        runRight = true;
        Array<TextureRegion> frames = new Array<TextureRegion>();
        int i = 0;
        for (; i < 3; i++) {
            frames.add(new TextureRegion(atlas.findRegion("warrior_walk"), i * 16, 0, 14, 16));
            i++;
        }
        runAnimation = new Animation(0.1f, frames);
        frames.clear();
        frames.add(new TextureRegion(atlas.findRegion("warrior_attack"), 0, 0, 27, 16));
        frames.add(new TextureRegion(atlas.findRegion("warrior_attack"), 27, 0, 27, 16));
        attackAnimation = new Animation(0.5f, frames);
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
        try{
            music = game.getManager().get("sounds/morte.wav",Music.class);
            music.setLooping(false);
            music.setVolume(1);
            music.play();
        }catch (Exception e){
            Gdx.app.log("Error","audio file not found");
        }
        game.getCurrentPlayScreen().gameOver();
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
     * Replace the first attack with a melee attack
     */
    @Override
    public void firstAttack() {
        currentState = State.ATTACK;
        previusState = State.ATTACK;
        stateTimer = 0;
        setSize(27 / BitHeroes.PPM, 16 / BitHeroes.PPM);
        setRegion(getFrame(0));

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                lockAttack=false;
                setSize(16 / BitHeroes.PPM, 16 / BitHeroes.PPM);
            }
        },attackAnimation.getAnimationDuration());


        final BodyDef bDef=new BodyDef();
        bDef.position.set(body.getPosition());
        bDef.type = BodyDef.BodyType.DynamicBody;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                final Body attackBody = world.createBody(bDef);
                attackBody.setGravityScale(0);
                try{
                    music = game.getManager().get("sounds/spadata.mp3",Music.class);
                    music.setLooping(false);
                    music.setVolume(1);
                    music.play();
                }catch (Exception e){
                    Gdx.app.log("Error","audio file not found");
                }
                if (isFlipX()) {
                    final Fixture f = attackBody.createFixture(createBackAttackFixture());
                    f.setUserData(meleeDamage);

                } else {
                    final Fixture f = attackBody.createFixture(createFrontAttackFixture());
                    f.setUserData(meleeDamage);
                }
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        attackBody.setUserData(true);
                    }
                }, attackAnimation.getAnimationDuration() / 2);
            }
        }, attackAnimation.getAnimationDuration() / 2);
    }

}

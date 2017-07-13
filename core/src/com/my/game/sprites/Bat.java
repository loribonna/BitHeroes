package com.my.game.sprites;

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
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.my.game.MyGame;
import com.my.game.tools.*;

/**
 * Created by lorib on 11/05/2017.
 */

public class Bat extends Enemy {
    public Bat(World w, TextureAtlas screenAtlas,Vector2 position) {
        super(w, screenAtlas,position);
        attackRange=0.18f;
        life=1;
    }

    @Override
    protected void distanceAttack() {
        secondAttack();
    }

    @Override
    protected void meleeAttack() {
        firstAttack();
    }

    @Override
    public void getAnimations(TextureAtlas atlas) {
        standAnimation = new TextureRegion(atlas.findRegion("bat_flying").getTexture(), 17,-1, 15, 34);
        setBounds(0, 0, 24 / MyGame.PPM, 30 / MyGame.PPM);
        setRegion(standAnimation);
        currentState = State.STAND;
        previusState = State.STAND;
        stateTimer = 0;
        runRight = true;
        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(atlas.findRegion("bat_flying"),  1, -1, 15, 34));
        frames.add(new TextureRegion(atlas.findRegion("bat_flying"),  17, -1, 15, 34));
        frames.add(new TextureRegion(atlas.findRegion("bat_flying"),  36, -1 ,15, 34));

        runAnimation = new Animation(0.1f, frames);
        frames.clear();
        frames.add(new TextureRegion(atlas.findRegion("bat_attack"), -1, 2, 15, 14));
        frames.add(new TextureRegion(atlas.findRegion("bat_attack"), 15, 2, 15, 14));
        attackAnimation = new Animation (0.1f, frames);
        frames.clear();
    }

    @Override
    public void firstAttack() {
        //TODO: fly attack
        lockAttack=false;
    }

    @Override
    public void secondAttack() {
        lockAttack=false;
    }

    @Override
    public void specialAttack() {
        lockAttack=false;
    }

    @Override
    protected FixtureDef createFrontAttackFixture() {
        return null;
    }

    @Override
    protected FixtureDef createBackAttackFixture() {
        return null;
    }

}
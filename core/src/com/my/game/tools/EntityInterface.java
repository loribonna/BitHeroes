package com.my.game.tools;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by lorib on 11/05/2017.
 */

public interface EntityInterface {
    public static enum State {
        FALL,
        ATTACK,
        STAND,
        RUN,
        JUMP,
        THROW
    }
    public static enum EntityType{
        WARRIOR,
        ENEMY
    }

    public void update(float delta);
    public TextureRegion getFrame(float dt);
    public void attack();
    public State getState();
    public void define(Vector2 position);
    public void getAnimations(TextureAtlas atlas);
}

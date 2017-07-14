package com.my.game.tools.Interfaces;

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
        THROW,
        SPECIAL
    }
    public static enum AttackType {
        FIRST,
        SECOND,
        SPECIAL
    }

    public void update(float delta);
    public TextureRegion getFrame(float dt);
    public State getState();
    public void define(Vector2 position);
    public void hit(int damage);
    public void getAnimations(TextureAtlas atlas);
}

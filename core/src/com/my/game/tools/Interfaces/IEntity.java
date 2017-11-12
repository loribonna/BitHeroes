package com.my.game.tools.Interfaces;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * Interface for Entity with enums
 */

public interface IEntity {
    enum PlayerName {
        WARRIOR,
        ARCHER,
        FIREBENDER
    }

    enum State {
        FALL,
        ATTACK,
        STAND,
        RUN,
        JUMP,
        THROW,
        SPECIAL
    }

    void getAnimations(TextureAtlas atlas);

    Filter getFilter();

    /**
     * Update position, target (if enemy) and animation.
     * @param delta
     */
    void update(float delta);

    /**
     * Process hit event.
     * @param damage
     */
    void hit(int damage);

    /**
     * Perform some action after Hit event.
     */
    void recoil();

    /**
     * Destroy current body and fixtures.
     */
    void destroy();

    /**
     * @param dt
     * @return current frame of animation based on the current state.
     */
    TextureRegion getFrame(float dt);

    /**
     * @return new state based on the action being performed and movement of the body.
     */
    State getState();

    /**
     * Create the body of the entity in the given position in the world.
     * @param position
     */
    void define(Vector2 position);

    /**
     * Set fixtures in the current body.
     */
    void createBorders();
}

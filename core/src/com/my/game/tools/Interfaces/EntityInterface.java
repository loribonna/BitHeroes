package com.my.game.tools.Interfaces;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * Interface for Entity with enums
 */

public interface EntityInterface {
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
    enum AttackType {
        FIRST,
        SECOND,
        SPECIAL
    }

    /**
     * Import enity-specific animations from the atlas.
     * @param atlas
     */
    public abstract void getAnimations(TextureAtlas atlas);

    /**
     * @return current entity filter to set collisions.
     */
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
    public State getState();

    /**
     * Create the body of the entity in the given position in the world.
     * @param position
     */
    void define(Vector2 position);

    /**
     * Set fixtures in the current body.
     */
    void createBorders();

    /**
     * Perform entity primary attack. Default is nothing.
     */
    void firstAttack();

    /**
     * Perform entity secondary attack. Default is nothing.
     */
    void secondAttack();

    /**
     * Perform entity's special attack. Default is nothing.
     */
    void specialAttack();

    /**
     * Perform attack based on the attackType parameter.
     * Control if lockAttack is released before perform another attack.
     * @param attackType
     */
    void throwAttack(AttackType attackType);

    /**
     * @return fixture to trigger collision for Melee attack if the body is flipped.
     */
    FixtureDef createBackAttackFixture();

    /**
     * @return fixture to trigger collision for Melee attack if the attack is front
     */
    FixtureDef createFrontAttackFixture();
}

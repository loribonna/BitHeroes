package com.my.game.tools.Interfaces;

import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * Created by lorib on 12/11/2017.
 */

public interface IFight {
    enum AttackType {
        MELEE,
        DISTANCE,
        SPECIAL
    }

    /**
     * Control if lockAttack is released before perform another attack.
     * @param attackType
     */
    void throwAttack(AttackType attackType);

    /**
     * Perform entity primary attack. Default is nothing.
     */
    void meleeAttack();

    /**
     * Perform entity secondary attack. Default is nothing.
     */
    void distanceAttack();

    /**
     * Perform entity's special attack. Default is nothing.
     */
    void specialAttack();

    /**
     * @return fixture to trigger collision for Melee attack if the body is flipped.
     */
    FixtureDef createBackAttackFixture();

    /**
     * @return fixture to trigger collision for Melee attack if the attack is front
     */
    FixtureDef createFrontAttackFixture();
}

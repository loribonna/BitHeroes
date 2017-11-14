package com.my.game.tools;

/**
 * Created by lorib on 13/11/2017.
 */

public class AppConstants {
    public enum AttackType {
        MELEE,
        DISTANCE,
        SPECIAL
    }

    public enum Direction{
        RIGHT,
        LEFT,
        STOP,
        UP,
        NONE
    }

    public enum PlayerName {
        WARRIOR,
        ARCHER,
        FIREBENDER
    }

    public enum State {
        FALL,
        ATTACK,
        STAND,
        RUN,
        JUMP,
        THROW,
        SPECIAL
    }
}

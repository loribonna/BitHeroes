package com.my.game.tools;

/**
 * Created by lorib on 13/11/2017.
 */

public class AppConstants {
    public static class Float2 {
        public float x;
        public float y;

        public Float2(float x,float y){
            this.x=x;
            this.y=y;
        }

        public String toString(){
            return (Float.toString(x)+","+Float.toString(y));
        }
    };

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

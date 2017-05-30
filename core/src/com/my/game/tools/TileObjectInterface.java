package com.my.game.tools;

/**
 * Created by lorib on 13/05/2017.
 */

public interface TileObjectInterface {
    public void define();
    public void setCategoryBits(short categoryBits);
    /**
     *
     * @param entity: If String is the contact point with player, else is Enemy.
     */
    public void onHit(Object entity);
}

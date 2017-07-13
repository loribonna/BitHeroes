package com.my.game.tools;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

/**
 * Created by lorib on 13/05/2017.
 */

public interface TileObjectInterface {
    /**
     * Create body and set position on the current position rect.
     * Create fixture around the body.
     */
    public void define();
    /**
     * Set filter bits to trigger collisions in the current Fixture.
     * @param filterBits
     */
    public void setCategoryBits(short filterBits);
    /**
    * @param entity: If String is the contact point with player, else is Enemy.
    */
    public void onHit(Entity entity);
    /**
     * Get tile cell coordinate by scaling up and dividing by the tile size.
     * @return Tile Cell Coordinate.
     */
    public TiledMapTileLayer.Cell getCell();
    /**
     * Remove current body fixtures.
     */
    public void dispose();
    /**
     * Update object frames and position if the TileObject has animations.
     */
    public abstract void update(float delta);
}

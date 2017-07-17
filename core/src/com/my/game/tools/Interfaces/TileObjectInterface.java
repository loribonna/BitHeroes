package com.my.game.tools.Interfaces;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.my.game.tools.Entity;

/**
 * Interface for TileObject
 */

public interface TileObjectInterface {
    /**
     * Create body and set position on the current position rect.
     * Create fixture around the body.
     */
    void define();

    /**
     * Set filter bits to trigger collisions with other fixtures.
     * @param filterBits
     */
    void setCategoryBits(short filterBits);

    /**
     * Called from WorldContactListener when hit
     * @param entity Enemy or Player
     */
    void onHit(Entity entity);

    /**
     * Update object frames and position of the TileObject
     */
    void update(float delta);

    /**
     * Remove object and body
     */
    void dispose();
}

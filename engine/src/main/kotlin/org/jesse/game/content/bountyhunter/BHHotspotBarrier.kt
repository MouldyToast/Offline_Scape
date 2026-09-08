package org.jesse.game.content.bountyhunter

import org.jesse.game.world.entity.Location
import org.jesse.game.world.`object`.WorldObject

/**
 * This represents a hotspot barrier object in the game world
 * @author John J. Woloszyk / Kryeus
 */
class BHHotspotBarrier(id: Int, type: Int = DEFAULT_TYPE, rotation: Int = DEFAULT_ROTATION, tile: Location) :
    WorldObject(id, type, rotation, tile) {
}
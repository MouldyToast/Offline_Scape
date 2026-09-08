package org.jesse.game.content.dt2.npc.theduke

import org.jesse.game.world.entity.Location
import org.jesse.game.world.`object`.WorldObject

/**
 * Mack wrote original logic - Kry rewrote in NR terms
 * @author John J. Woloszyk / Kryeus
 * @date 8.14.2024
 */
data class Extremity(
    var location: Location,
    var asleepId: Int,
    var awakenedId: Int,
    val rotation: Int,
    val gazeAnim: Int,
    val spotLightNPC: Int,
    val animationId: Int,
    val restAnimationId: Int = 10185
) {
    fun awakened(): WorldObject = WorldObject(
        id = awakenedId,
        rotation = rotation,
        tile = location
    )

    fun sleeping(): WorldObject = WorldObject(
        id = asleepId,
        rotation = rotation,
        tile = location
    )

}
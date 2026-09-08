package org.jesse.game.content.dt2.npc.theduke

import org.jesse.game.content.East
import org.jesse.game.content.West
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.npc.NPC

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-01-09
 */
class SpotlightNpc(
    spotlightNpcId: Int,
    val spawnLocation: Location,
    side: ExtremitySide
) : NPC(
    spotlightNpcId,
    spawnLocation,
    true
) {

    init {
        radius = 0
        faceDirection(if (side == ExtremitySide.LEFT) East else West)
    }
}
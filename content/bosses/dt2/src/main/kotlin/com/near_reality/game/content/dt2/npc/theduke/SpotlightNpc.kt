package com.near_reality.game.content.dt2.npc.theduke

import com.near_reality.game.content.East
import com.near_reality.game.content.West
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.npc.NPC

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
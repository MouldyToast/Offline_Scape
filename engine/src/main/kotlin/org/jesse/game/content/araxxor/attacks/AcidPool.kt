package org.jesse.game.content.araxxor.attacks

import org.jesse.game.world.entity.Location
import org.jesse.game.obj.ids.*
import org.jesse.game.world.`object`.WorldObject

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-22
 */
class AcidPool(
    val spawn: Location
): WorldObject(
    ACID_POOL_54148,
    10,
    0,
    spawn
)
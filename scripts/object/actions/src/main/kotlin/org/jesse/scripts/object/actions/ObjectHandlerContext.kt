package org.jesse.scripts.`object`.actions

import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

/**
 * @author Jire
 */
data class ObjectHandlerContext(
    val player: Player,
    val obj: WorldObject,
    val name: String?,
    val optionID: Int,
    val option: String?
)
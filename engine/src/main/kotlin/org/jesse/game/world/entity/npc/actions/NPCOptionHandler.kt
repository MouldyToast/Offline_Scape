package org.jesse.game.world.entity.npc.actions

import org.jesse.game.world.entity.npc.NPC
import org.jesse.game.world.entity.player.Player

/**
 * @author Jire
 */
data class NPCOptionHandler(
    val player: Player,
    val npc: NPC
)
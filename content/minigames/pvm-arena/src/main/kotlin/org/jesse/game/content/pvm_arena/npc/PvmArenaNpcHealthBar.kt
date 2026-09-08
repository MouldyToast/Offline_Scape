package org.jesse.game.content.pvm_arena.npc

import org.jesse.game.world.entity.EntityHitBar
import org.jesse.game.world.entity.npc.NPC

/**
 * Represents a hit bar for a PvM Arena boss.
 *
 * @author Stan van der Bend
 */
class PvmArenaNpcHealthBar(npc: NPC) : EntityHitBar(npc) {

    override fun getType(): Int = 17
}

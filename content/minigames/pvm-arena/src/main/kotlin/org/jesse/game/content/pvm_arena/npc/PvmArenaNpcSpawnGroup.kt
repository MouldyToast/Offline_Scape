package org.jesse.game.content.pvm_arena.npc

import com.runespawn.util.weakMutableSetOf
import org.jesse.game.world.entity.npc.NPC

/**
 * Represents a group of NPCs that can spawn in the PvM Arena during a wave.
 *
 * @author Stan van der Bend
 */
class PvmArenaNpcSpawnGroup(val name: String, npcs: Set<NPC>)
    : MutableSet<NPC> by weakMutableSetOf(*npcs.toTypedArray())
{
    constructor(npc: NPC) : this(npc.name, setOf(npc))

    override fun toString(): String = "PvmArenaNpcSpawnGroup(name='$name')"
}

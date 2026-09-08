package org.jesse.game.content

import mgi.types.config.npcs.NPCDefinitions
import org.jesse.scripts.npc.drops.NPCDropTableScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.item.ids.*
import org.jesse.scripts.npc.drops.table.DropTableType.*
import org.jesse.game.world.entity.npc.drop.matrix.Drop
import org.jesse.game.world.entity.npc.drop.matrix.Drop.GUARANTEED_RATE
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor.PredicatedDrop
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor.DisplayedDrop

class AccumulatorAltSourcesDroptable : NPCDropTableScript() {

    val npcNames = setOf("thug")

    init {

        //println(npcNames)

        npcs(*NPCDefinitions.getDefinitions()
            .filterNotNull()
            .mapNotNull { npcDef -> npcDef.takeIf { npcNames.contains(it.name.lowercase()) }?.id }
            .toSet()
            .toIntArray()
        )

        onDeath {
            rollStaticTableAndDrop(killer, Main)
        }

        buildTable {
            Main(100) { // 2% chance
                AVAS_ACCUMULATOR quantity 1 rarity 2
            }
        }
    }
}

package com.zenyte.game.content

import mgi.types.config.npcs.NPCDefinitions
import com.near_reality.scripts.npc.drops.NPCDropTableScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.near_reality.game.util.invoke
import com.zenyte.game.item.ItemId
import com.near_reality.scripts.npc.drops.table.DropTableType.*
import com.zenyte.game.world.entity.npc.drop.matrix.Drop
import com.zenyte.game.world.entity.npc.drop.matrix.Drop.GUARANTEED_RATE
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor.PredicatedDrop
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor.DisplayedDrop
import com.zenyte.game.item.ItemId.AVAS_ACCUMULATOR

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

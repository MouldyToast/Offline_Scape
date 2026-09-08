package org.jesse.game.content

import org.jesse.game.item.Item
import org.jesse.game.util.Utils
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

class SarachnisDroptable : NPCDropTableScript() {

    init {
        npcs(SARACHNIS)

        onDeath {
            val cudgel = Utils.randomNoPlus(125)
            val jarOfEyes = Utils.randomNoPlus(600)

            if(cudgel == 0) {
                npc.dropItem(killer, Item(SARACHNIS_CUDGEL))
            }

            if(jarOfEyes == 0) {
                npc.dropItem(killer, Item(JAR_OF_EYES))
            }

        }
        appendDrop(DisplayedDrop(SARACHNIS_CUDGEL, 1, 1, 125.0))
        appendDrop(DisplayedDrop(JAR_OF_EYES, 1, 1, 600.0))

        buildTable(200, overrideTable = false){
        }
    }
}

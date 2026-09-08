package org.jesse.game.content.origins.elementals

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class BalanceElementals : NPCSpawnsScript() {

    init {
        /**
         * @author John J. Woloszyk / Kryeus
         * @date 7.3.2025
         */

        /* Wilderness */
        BALANCE_ELEMENTAL_MELEE(2984, 3951, 0, walkRadius = 3)
        BALANCE_ELEMENTAL_RANGED(2976, 3953, 0, walkRadius = 3)
        BALANCE_ELEMENTAL_MAGIC(2979, 3960, 0, walkRadius = 3)
    }
}

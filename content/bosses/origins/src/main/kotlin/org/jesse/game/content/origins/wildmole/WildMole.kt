package org.jesse.game.content.origins.wildmole

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Wildmole : NPCSpawnsScript() {

    init {
        /**
         * @author John J. Woloszyk / Kryeus
         * @date 8.6.2025
         */

        WILD_MOLE(3136, 3911, 0, walkRadius = 10)
        WILD_MOLE(3150, 3908, 0, walkRadius = 10)
        WILD_MOLE(3135, 3944, 0, walkRadius = 10)
    }
}

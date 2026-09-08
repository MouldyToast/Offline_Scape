package com.near_reality.game.content.origins.wildmole

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.npc.ids.*
import com.near_reality.game.util.invoke
import com.zenyte.game.util.Direction.*

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

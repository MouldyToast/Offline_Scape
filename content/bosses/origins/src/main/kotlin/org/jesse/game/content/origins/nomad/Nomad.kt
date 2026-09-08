package org.jesse.game.content.origins.nomad

import org.jesse.game.npc.ids.*
import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Nomad : NPCSpawnsScript() {

    init {
        /**
         * @author John J. Woloszyk / Kryeus
         * @date 7.11.2025
         */

        NOMAD_16086(3205, 3877, 0, walkRadius = 30)
    }
}

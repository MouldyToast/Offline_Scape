package org.jesse.game.content.origins.slashbash

import org.jesse.game.npc.ids.*
import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Slashbash : NPCSpawnsScript() {

    init {
        /**
         * @author John J. Woloszyk / Kryeus
         * @date 7.11.2025
         */

        SLASH_BASH_16088(2438, 4395, 0, walkRadius = 5)
        SLASH_BASH_16088(2439, 4385, 0, walkRadius = 5)
    }
}

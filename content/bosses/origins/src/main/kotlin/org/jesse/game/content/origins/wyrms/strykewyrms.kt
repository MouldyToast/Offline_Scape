package org.jesse.game.content.origins.wyrms

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Strykewyrms : NPCSpawnsScript() {

    init {
        /**
         * @author John J. Woloszyk / Kryeus
         * @date 7.7.2025
         * These are located on the new mini islands
         */

        ICE_STRYKEWYRM(1824, 2713, 0, walkRadius = 5)
        ICE_STRYKEWYRM(1817, 2716, 0, walkRadius = 5)
        ICE_STRYKEWYRM(1820, 2721, 0, walkRadius = 5)
        ICE_STRYKEWYRM(1825, 2727, 0, walkRadius = 5)
        ICE_STRYKEWYRM(1838, 2720, 0, walkRadius = 5)

        DESERT_STRYKEWYRM(1760, 2727, 0, walkRadius = 5)
        DESERT_STRYKEWYRM(1755, 2722, 0, walkRadius = 5)
        DESERT_STRYKEWYRM(1764, 2716, 0, walkRadius = 5)
        DESERT_STRYKEWYRM(1761, 2710, 0, walkRadius = 5)

        JUNGLE_STRYKEWYRM(1828, 2652, 0, walkRadius = 5)
        JUNGLE_STRYKEWYRM(1822, 2649, 0, walkRadius = 5)
        JUNGLE_STRYKEWYRM(1825, 2655, 0, walkRadius = 5)
        JUNGLE_STRYKEWYRM(1813, 2657, 0, walkRadius = 5)
        JUNGLE_STRYKEWYRM(1818, 2663, 0, walkRadius = 5)
    }
}

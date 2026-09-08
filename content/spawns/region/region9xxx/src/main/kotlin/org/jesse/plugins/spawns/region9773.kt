package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region9773 : NPCSpawnsScript() {

    init {
        FISHING_SPOT_7946(2453, 2891, 0, SOUTH, 5)
        FISHING_SPOT_7946(2456, 2893, 0, SOUTH, 5)
        FISHING_SPOT_7946(2458, 2890, 0, SOUTH, 5)
        WOLF(2482, 2923, 0, SOUTH, 6)
        BIG_WOLF_115(2487, 2924, 0, SOUTH, 5)
        WOLF(2491, 2922, 0, SOUTH, 6)
        WOLF(2491, 2927, 0, SOUTH, 6)
        WOLF(2491, 2931, 0, SOUTH, 6)
    }
}

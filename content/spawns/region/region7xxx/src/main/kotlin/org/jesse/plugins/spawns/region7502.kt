package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region7502 : NPCSpawnsScript() {

    init {
        GOBLIN_5509(1883, 5026, 0, SOUTH, 0)
        GOBLIN_5508(1883, 5029, 0, SOUTH, 0)
        MR_MORDAUT(1884, 5020, 0, SOUTH, 0)
        ZOMBIE_5507(1885, 5026, 0, SOUTH, 0)
        GIANT(1885, 5029, 0, SOUTH, 0)
        MUMMY_5506(1887, 5026, 0, SOUTH, 0)
        DUNCE(1890, 5018, 0, SOUTH, 0)
    }
}

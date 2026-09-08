package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region12859 : NPCSpawnsScript() {

    init {
        LAVA_DRAGON(3201, 3814, 0, SOUTH, 4)
        LAVA_DRAGON(3205, 3834, 0, SOUTH, 4)
        5861(3210, 3803, 0, SOUTH, 0)
        LAVA_DRAGON(3211, 3809, 0, SOUTH, 4)
        LAVA_DRAGON(3213, 3819, 0, SOUTH, 4)
        LAVA_DRAGON(3223, 3830, 0, SOUTH, 4)
        CHAOS_DWARF(3233, 3783, 0, SOUTH, 14)
        CHAOS_DWARF(3241, 3796, 0, SOUTH, 14)
        CHAOS_DWARF(3253, 3782, 0, SOUTH, 14)
    }
}

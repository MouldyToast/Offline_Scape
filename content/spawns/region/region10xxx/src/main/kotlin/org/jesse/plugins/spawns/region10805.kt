package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region10805 : NPCSpawnsScript() {

    init {
        UNICORN(2700, 3423, 0, SOUTH, 15)
        UNICORN(2700, 3442, 0, SOUTH, 15)
        IGNATIUS_VULCAN(2719, 3431, 0, SOUTH, 0)
        SHERLOCK(2733, 3415, 0, SOUTH, 3)
        FLAX_KEEPER(2744, 3444, 0, SOUTH, 3)
        THORMAC(2702, 3405, 3, SOUTH, 2)
    }
}

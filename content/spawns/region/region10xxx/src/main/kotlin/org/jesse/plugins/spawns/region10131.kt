package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region10131 : NPCSpawnsScript() {

    init {
        SCARED_SKAVID(2499, 9433, 0, SOUTH, 5)
        SKAVID_4378(2500, 9451, 0, SOUTH, 5)
        SKAVID_4379(2503, 9415, 0, SOUTH, 5)
        SKAVID_4377(2515, 9452, 0, SOUTH, 5)
        MAD_SKAVID(2523, 9411, 0, SOUTH, 5)
        SKAVID_4376(2530, 9465, 0, SOUTH, 5)
    }
}

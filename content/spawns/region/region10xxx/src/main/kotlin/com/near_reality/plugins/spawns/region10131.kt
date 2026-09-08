package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.npc.ids.*
import com.near_reality.game.util.invoke
import com.zenyte.game.util.Direction.*

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

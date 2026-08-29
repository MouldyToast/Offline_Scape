package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.near_reality.game.util.invoke
import com.zenyte.game.util.Direction.*

class Region15000 : NPCSpawnsScript() {

    init {
        BAT(3727, 9755, 1, SOUTH, 23)
        9824(3727, 9759, 1, SOUTH, 5)
        BAT(3729, 9763, 1, SOUTH, 23)
        SISTER_SALOHCIN(3742, 9768, 1, SOUTH, 5)
    }
}

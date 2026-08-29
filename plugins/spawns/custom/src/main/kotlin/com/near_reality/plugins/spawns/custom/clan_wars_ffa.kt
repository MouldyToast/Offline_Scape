package com.near_reality.plugins.spawns.custom

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.near_reality.game.util.invoke
import com.zenyte.game.util.Direction.*

class ClanWarsFfa : NPCSpawnsScript() {

    init {
        COMBAT_DUMMY_16019(3331, 4752, 0, WEST)
    }
}

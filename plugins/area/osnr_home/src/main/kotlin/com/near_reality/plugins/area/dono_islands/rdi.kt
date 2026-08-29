package com.near_reality.plugins.area.dono_islands

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.near_reality.game.util.invoke
import com.zenyte.game.util.Direction.*

class Rdi : NPCSpawnsScript() {

    init {
        FISHING_SPOT_6488(1642, 2683, 0, WEST, 0)
        FISHING_SPOT_6488(1640, 2683, 0, WEST, 0)
        FISHING_SPOT_6488(1638, 2683, 0, WEST, 0)
        FISHING_SPOT_6488(1636, 2683, 0, WEST, 0)

        CARNIVOROUS_CHINCHOMPA(1687, 2668, 0, SOUTH, 3)
        CARNIVOROUS_CHINCHOMPA(1688, 2659, 0, SOUTH, 3)
        CARNIVOROUS_CHINCHOMPA(1691, 2663, 0, SOUTH, 3)
        CARNIVOROUS_CHINCHOMPA(1690, 2668, 0, SOUTH, 3)
        CARNIVOROUS_CHINCHOMPA(1690, 2659, 0, SOUTH, 3)

        CHINCHOMPA(1692, 2666, 0, SOUTH, 3)
        CHINCHOMPA(1686, 2668, 0, SOUTH, 3)
        CHINCHOMPA(1684, 2664, 0, SOUTH, 3)
        CHINCHOMPA(1685, 2660, 0, SOUTH, 3)
        CHINCHOMPA(1686, 2657, 0, SOUTH, 3)

        TOOL_LEPRECHAUN(1677, 2674, 0, SOUTH, 1)

        16032(1671, 2672, 0, WEST, 1) // RDI Shop
        16051(1674, 2677, 0, WEST, 1) // RDI Mage of Zamorak
    }
}

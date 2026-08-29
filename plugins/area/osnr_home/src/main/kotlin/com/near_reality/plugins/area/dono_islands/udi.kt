package com.near_reality.plugins.area.dono_islands

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.near_reality.game.util.invoke
import com.zenyte.game.util.Direction.*

class Udi : NPCSpawnsScript() {

    init {
        TOOL_LEPRECHAUN(1645, 2600, 0, SOUTH, 1)
        MASTER_FARMER(1652, 2599, 0, SOUTH, 2)
        KYLIE_MINNOW_7728(1684, 2580, 0, NORTH, 3)

        16033(1663, 2570, 0, NORTH, 0) // UDI Shop


        BLACK_CHINCHOMPA(1631, 4436, 0, SOUTH, 6)
        BLACK_CHINCHOMPA(1627, 4437, 0, SOUTH, 6)
        BLACK_CHINCHOMPA(1622, 4438, 0, SOUTH, 6)
        BLACK_CHINCHOMPA(1622, 4433, 0, SOUTH, 6)
        BLACK_CHINCHOMPA(1622, 4426, 0, SOUTH, 6)
        BLACK_CHINCHOMPA(1625, 4429, 0, SOUTH, 6)
        BLACK_CHINCHOMPA(1628, 4429, 0, SOUTH, 6)


        ROD_FISHING_SPOT_6825(1700, 2584, 0, SOUTH, 0)
        ROD_FISHING_SPOT_6825(1692, 2580, 0, SOUTH, 0)
        ROD_FISHING_SPOT_6825(1684, 2575, 0, SOUTH, 0)

        FISHING_SPOT_7730(1693, 2571, 0, SOUTH, 0)
        FISHING_SPOT_7730(1690, 2571, 0, SOUTH, 0)
        FISHING_SPOT_7730(1693, 2569, 0, SOUTH, 0)
        FISHING_SPOT_7730(1690, 2569, 0, SOUTH, 0)

        FISHING_SPOT_7730(1699, 2571, 0, SOUTH, 0)
        FISHING_SPOT_7730(1702, 2571, 0, SOUTH, 0)
        FISHING_SPOT_7730(1699, 2569, 0, SOUTH, 0)
        FISHING_SPOT_7730(1702, 2569, 0, SOUTH, 0)
    }
}

package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region5178 : NPCSpawnsScript() {

    init {
        IMP_5007(1283, 3720, 0, SOUTH, 100)
        SQUIRREL_1418(1290, 3758, 0, SOUTH, 10)
        SQUIRREL_1418(1291, 3753, 0, SOUTH, 10)
        SQUIRREL_1418(1295, 3751, 0, SOUTH, 10)
        SQUIRREL_1418(1295, 3758, 0, SOUTH, 10)
        KEITH(1296, 3723, 0, SOUTH, 0)
        COW_2791(1306, 3713, 0, SOUTH, 4)
        COW_CALF(1309, 3714, 0, SOUTH, 9)
        COW_2793(1310, 3717, 0, SOUTH, 7)
        BLACK_BEAR(1322, 3713, 0, SOUTH, 8)
    }
}

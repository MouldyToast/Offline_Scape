package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region5022 : NPCSpawnsScript() {

    init {
        WYRM(1252, 10159, 0, SOUTH, 12)
        WYRM(1255, 10147, 0, SOUTH, 12)
        WYRM(1259, 10154, 0, SOUTH, 12)
        WYRM(1259, 10159, 0, SOUTH, 12)
        WYRM(1266, 10152, 0, SOUTH, 12)
        WYRM(1267, 10157, 0, SOUTH, 12)
        WYRM(1273, 10158, 0, SOUTH, 12)
        GARBEK_QUO_MATEN(1276, 10162, 0, WEST, 0)
    }
}

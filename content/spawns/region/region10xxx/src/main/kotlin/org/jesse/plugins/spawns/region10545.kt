package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region10545 : NPCSpawnsScript() {

    init {
        SHOP_KEEPER_2888(2641, 3171, 0, SOUTH, 2)
        SHOP_KEEPER_2888(2655, 3152, 0, SOUTH, 2)
        MURPHY(2668, 3162, 0, SOUTH, 2)
        MURPHY_10707(2671, 3172, 1, SOUTH, 5)
        1330(2673, 3144, 0, SOUTH, 4)
        1332(2675, 3144, 0, SOUTH, 2)
        TINDEL_MARCHANT(2678, 3153, 0, SOUTH, 2)
    }
}

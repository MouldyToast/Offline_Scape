package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region8011 : NPCSpawnsScript() {

    init {
        10711(2011, 4818, 0, SOUTH, 0)
        10710(2011, 4830, 0, SOUTH, 0)
        MURPHY_5609(2013, 4825, 0, SOUTH, 5)
        GULL_285(2014, 4818, 0, SOUTH, 5)
        GULL_285(2014, 4832, 0, SOUTH, 5)
        GULL_285(2018, 4831, 0, SOUTH, 5)
        GULL_284(2020, 4816, 0, SOUTH, 5)
        GULL_285(2023, 4820, 0, SOUTH, 5)
        10711(2011, 4818, 1, SOUTH, 0)
        10710(2011, 4830, 1, SOUTH, 0)
    }
}

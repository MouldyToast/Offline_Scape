package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region12703 : NPCSpawnsScript() {

    init {
        REVENANT_CYCLOPS(3167, 10195, 0, SOUTH, 2)
        REVENANT_CYCLOPS(3173, 10185, 0, SOUTH, 2)
        REVENANT_DEMON(3184, 10189, 0, SOUTH, 2)
        REVENANT_DEMON(3184, 10199, 0, SOUTH, 2)
        EMBLEM_TRADER_7943(3196, 10224, 0, SOUTH, 4)
    }
}

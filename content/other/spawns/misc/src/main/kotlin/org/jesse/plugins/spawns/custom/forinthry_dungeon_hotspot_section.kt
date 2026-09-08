package org.jesse.plugins.spawns.custom

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class ForinthryDungeonHotspotSection : NPCSpawnsScript() {

    init {
        REVENANT_ORK(3237, 10167, walkRadius = 2)
        REVENANT_KNIGHT(3244, 10171, walkRadius = 6)
        REVENANT_DRAGON(3242, 10179, walkRadius = 2)
        REVENANT_HELLHOUND(3251, 10168, walkRadius = 2)
        REVENANT_IMP(3254, 10185, walkRadius = 3)
        REVENANT_DEMON(3251, 10181, walkRadius = 2)
    }
}

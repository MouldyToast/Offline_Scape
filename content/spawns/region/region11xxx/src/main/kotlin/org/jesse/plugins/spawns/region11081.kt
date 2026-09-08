package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region11081 : NPCSpawnsScript() {

    init {
        GRAIL_MAIDEN(2760, 4688, 0, SOUTH, 5)
        GRAIL_MAIDEN(2763, 4690, 0, SOUTH, 5)
        PEASANT(2765, 4722, 0, SOUTH, 5)
        GRAIL_MAIDEN(2766, 4680, 0, SOUTH, 5)
        GRAIL_MAIDEN(2769, 4686, 0, SOUTH, 5)
        PEASANT(2776, 4718, 0, SOUTH, 5)
        PEASANT(2777, 4703, 0, SOUTH, 5)
        PEASANT(2788, 4721, 0, SOUTH, 5)
        PEASANT(2789, 4698, 0, SOUTH, 5)
        BLACK_KNIGHT_TITAN(2791, 4722, 0, SOUTH, 5)
        FISHERMAN_4065(2802, 4706, 0, SOUTH, 5)
        THE_FISHER_KING(2762, 4688, 1, SOUTH, 5)
    }
}

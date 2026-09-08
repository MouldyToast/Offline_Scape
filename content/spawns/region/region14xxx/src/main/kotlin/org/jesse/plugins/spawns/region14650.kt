package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region14650 : NPCSpawnsScript() {

    init {
        CHARLES_CHARLINGTON(3654, 3746, 0, SOUTH, 5)
        TAR_BUBBLES(3669, 3742, 0, SOUTH, 2)
        TAR_BUBBLES(3676, 3744, 0, SOUTH, 2)
        TAR_BUBBLES(3677, 3767, 0, SOUTH, 2)
        HOOP_SNAKE(3678, 3753, 0, SOUTH, 20)
        HOOP_SNAKE(3680, 3756, 0, SOUTH, 20)
        TAR_BUBBLES(3683, 3746, 0, SOUTH, 2)
        HOOP_SNAKE(3684, 3771, 0, SOUTH, 20)
        HOOP_SNAKE(3686, 3766, 0, SOUTH, 20)
    }
}

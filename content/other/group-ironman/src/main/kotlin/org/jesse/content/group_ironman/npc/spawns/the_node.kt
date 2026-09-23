package org.jesse.content.group_ironman.npc.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class TheNode : NPCSpawnsScript() {

    init {
        D3AD1I_F15HER(3114, 3045, 0, SOUTH, 0)
        SEAGULL(3107, 3045, 0, SOUTH, 5)
        SEAGULL(3108, 3039, 0, SOUTH, 5)
        GULL_11297(3101, 3050, 0, SOUTH, 20)
        BUTTERFLY_236(3104, 3030, 0, SOUTH, 10)
        8735(3099, 3025, 0, EAST, 7)
        GROUP_STORAGE_TUTOR(3096, 3027, 0, EAST, 0)
        GROUP_IRONMAN_TUTOR(3107, 3028, 0, WEST, 2)
        ENRAGED_BOAR(3101, 3011, 0, WEST, 3)
        BOAR(3107, 3014, 0, NORTH, 7)
        BOAR(3102, 3011, 0, NORTH, 7)
        BOAR(3098, 3012, 0, SOUTH, 7)
    }
}

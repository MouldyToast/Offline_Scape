package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region13439 : NPCSpawnsScript() {

    init {
        4712(3366, 8156, 2, SOUTH, 0)
        4710(3351, 8156, 2, SOUTH, 0)

        1536(3351, 8284, 2, SOUTH, 0)
        1536(3366, 8284, 2, SOUTH, 0)

        16031(3361, 8180, 2, SOUTH, 0)

        TOOL_LEPRECHAUN(3420, 7787, 0, SOUTH, 2)
        TOOL_LEPRECHAUN(3397, 7998, 0, SOUTH, 2)
        TOOL_LEPRECHAUN(3362, 8274, 2, SOUTH, 2)
        TOOL_LEPRECHAUN(3362, 8146, 2, SOUTH, 2)
    }
}

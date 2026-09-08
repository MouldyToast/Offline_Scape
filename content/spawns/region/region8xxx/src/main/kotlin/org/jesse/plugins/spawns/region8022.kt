package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region8022 : NPCSpawnsScript() {

    init {
        BUTTERFLY_236(1986, 5564, 0, SOUTH, 9)
        GNOME_6095(2033, 5530, 1, SOUTH, 4)
        GNOME_GUARD_6082(2012, 5535, 2, SOUTH, 5)
        GNOME_GUARD_6082(2023, 5544, 2, SOUTH, 5)
    }
}

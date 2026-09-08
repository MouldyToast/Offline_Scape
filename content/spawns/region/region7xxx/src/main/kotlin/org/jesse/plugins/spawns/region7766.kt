package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region7766 : NPCSpawnsScript() {

    init {
        GNOME_GUARD_6081(1964, 5519, 3, SOUTH, 5)
        GNOME_TROOP_4974(1964, 5522, 3, SOUTH, 4)
        GNOME_GUARD_6081(1970, 5519, 3, SOUTH, 5)
        GNOME_TROOP(1970, 5522, 3, SOUTH, 3)
    }
}

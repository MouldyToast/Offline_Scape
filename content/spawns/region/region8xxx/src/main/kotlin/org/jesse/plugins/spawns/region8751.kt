package org.jesse.plugins.spawns

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Region8751 : NPCSpawnsScript() {

    init {
        FISHING_SPOT_6488(2182, 3067, 0, SOUTH, 0)
        ZULONAN(2189, 3069, 0, SOUTH, 0)
        HIGH_PRIESTESS_ZULHARCINQA(2192, 3055, 0, EAST, 0)
        ZULANIEL(2194, 3059, 0, SOUTH, 2)
        ZULARETH(2197, 3061, 0, SOUTH, 2)
        ZULURGISH(2199, 3055, 0, SOUTH, 2)
        FISHING_SPOT_6488(2199, 3066, 0, SOUTH, 0)
        ZULGUTUSOLLY(2201, 3048, 0, SOUTH, 2)
        ZULCHERAY(2204, 3050, 0, SOUTH, 0)
        SACRIFICE(2210, 3056, 0, SOUTH, 2)
        PRIESTESS_ZULGWENWYNIG_2033(2212, 3057, 0, SOUTH, 2)
    }
}

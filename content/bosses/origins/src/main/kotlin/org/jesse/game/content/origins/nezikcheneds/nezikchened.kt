package org.jesse.game.content.origins.nezikcheneds

import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Nezikchened : NPCSpawnsScript() {

    init {
        NEZIKCHENED_6379(2981, 3915, 0, walkRadius = 5)
        NEZIKCHENED_6379(2986, 3910, 0, walkRadius = 5)
        NEZIKCHENED_6379(2981, 3907, 0, walkRadius = 5)
        NEZIKCHENED_6379(2971, 3918, 0, walkRadius = 5)


        NEZIKCHENED_6379(2985, 3798, 0, walkRadius = 6)
        NEZIKCHENED_6379(2990, 3804, 0, walkRadius = 6)
        NEZIKCHENED_6379(2995, 3798, 0, walkRadius = 6)
        NEZIKCHENED_6379(2995, 3791, 0, walkRadius = 6)
    }
}

package org.jesse.game.content.origins.di

import org.jesse.game.npc.ids.*
import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Rdi2 : NPCSpawnsScript() {

    init {
        NOMAD_16086(2909, 5428, 0, walkRadius = 5)
        NOMAD_16086(2922, 5420, 0, walkRadius = 5)

        NEZIKCHENED_6379(2923, 5410, 0, walkRadius = 0)
        NEZIKCHENED_6379(2923, 5406, 0, walkRadius = 0)
        NEZIKCHENED_6379(2923, 5402, 0, walkRadius = 0)
        NEZIKCHENED_6379(2923, 5398, 0, walkRadius = 0)
        NEZIKCHENED_6379(2928, 5410, 0, walkRadius = 0)
        NEZIKCHENED_6379(2928, 5406, 0, walkRadius = 0)
        NEZIKCHENED_6379(2928, 5402, 0, walkRadius = 0)
        NEZIKCHENED_6379(2928, 5398, 0, walkRadius = 0)
        NEZIKCHENED_6379(2933, 5410, 0, walkRadius = 0)
        NEZIKCHENED_6379(2933, 5406, 0, walkRadius = 0)
        NEZIKCHENED_6379(2933, 5402, 0, walkRadius = 0)
        NEZIKCHENED_6379(2933, 5398, 0, walkRadius = 0)

        BALANCE_ELEMENTAL_MELEE(2891, 5418, 0, walkRadius = 3)
        BALANCE_ELEMENTAL_RANGED(2899, 5418, 0, walkRadius = 3)
        BALANCE_ELEMENTAL_MAGIC(2895, 5415, 0, walkRadius = 3)
        BALANCE_ELEMENTAL_RANGED(2891, 5412, 0, walkRadius = 3)
        BALANCE_ELEMENTAL_MELEE(2899, 5412, 0, walkRadius = 3)
    }
}

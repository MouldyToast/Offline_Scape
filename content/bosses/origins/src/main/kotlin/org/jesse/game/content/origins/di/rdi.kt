package org.jesse.game.content.origins.di

import org.jesse.game.npc.ids.*
import org.jesse.scripts.npc.spawns.NPCSpawnsScript
import org.jesse.game.util.invoke
import org.jesse.game.util.Direction.*

class Rdi : NPCSpawnsScript() {

    init {
        //CORPOREAL_BEAST(2928, 5468, 0, walkRadius = 5)

        BALANCE_ELEMENTAL_MELEE(2928, 5471, 0, walkRadius = 3)
        BALANCE_ELEMENTAL_RANGED(2927, 5466, 0, walkRadius = 3)
        BALANCE_ELEMENTAL_MAGIC(2924, 5461, 0, walkRadius = 3)
        BALANCE_ELEMENTAL_RANGED(2932, 5462, 0, walkRadius = 3)
        BALANCE_ELEMENTAL_MELEE(2932, 5468, 0, walkRadius = 3)

        TORRMENTED_DEMON(2911, 5492, 0, walkRadius = 0)
        TORRMENTED_DEMON(2911, 5489, 0, walkRadius = 0)
        TORRMENTED_DEMON(2911, 5486, 0, walkRadius = 0)
        BORK(2916, 5492, 0, walkRadius = 0)
        BORK(2916, 5489, 0, walkRadius = 0)
        BORK(2916, 5486, 0, walkRadius = 0)
        KING_BLACK_DRAGON(2921, 5490, 0, walkRadius = 0)
        KING_BLACK_DRAGON(2921, 5485, 0, walkRadius = 0)

        PLANE_FREEZER_LAKHRAHNAZ(2890, 5482, 0, walkRadius = 0)
        PLANE_FREEZER_LAKHRAHNAZ(2893, 5482, 0, walkRadius = 0)
        PLANE_FREEZER_LAKHRAHNAZ(2896, 5482, 0, walkRadius = 0)
        PLANE_FREEZER_LAKHRAHNAZ(2899, 5482, 0, walkRadius = 0)

        ICE_STRYKEWYRM(2890, 5478, 0, walkRadius = 0)
        ICE_STRYKEWYRM(2893, 5478, 0, walkRadius = 0)
        ICE_STRYKEWYRM(2896, 5478, 0, walkRadius = 0)
        ICE_STRYKEWYRM(2899, 5478, 0, walkRadius = 0)

        DESERT_STRYKEWYRM(2890, 5475, 0, walkRadius = 0)
        DESERT_STRYKEWYRM(2893, 5475, 0, walkRadius = 0)
        DESERT_STRYKEWYRM(2896, 5475, 0, walkRadius = 0)
        DESERT_STRYKEWYRM(2899, 5475, 0, walkRadius = 0)

        JUNGLE_STRYKEWYRM(2890, 5472, 0, walkRadius = 0)
        JUNGLE_STRYKEWYRM(2893, 5472, 0, walkRadius = 0)
        JUNGLE_STRYKEWYRM(2896, 5472, 0, walkRadius = 0)
        JUNGLE_STRYKEWYRM(2899, 5472, 0, walkRadius = 0)
    }
}

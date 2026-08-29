package com.near_reality.game.content.remnantpets

import com.near_reality.game.item.CustomItemId.*

/**
 * @author John J. Woloszyk / Kryeus
 * @date 7.25.2025
 */
object PrimalComponentsValues {
    @JvmStatic val valueMap = mutableMapOf<Int, IntRange>().run {
        put(PRIMAL_FULL_HELM, 3..6)
        put(PRIMAL_CHAINBODY, 4..7)
        put(PRIMAL_GAUNTLETS, 3..6)
        put(PRIMAL_PLATEBODY, 5..8)
        put(PRIMAL_PLATESKIRT, 4..7)
        put(PRIMAL_PLATELEGS, 4..7)
        put(PRIMAL_BOOTS, 3..6)

        put(PRIMAL_2H_SWORD, 4..7)
        put(PRIMAL_PICKAXE, 3..5)
        put(PRIMAL_SPEAR, 4..6)
        put(PRIMAL_KITESHIELD, 3..5)
        put(PRIMAL_HATCHET, 3..5)
        put(PRIMAL_MAUL, 4..6)
        put(PRIMAL_LONGSWORD, 3..5)
        put(PRIMAL_RAPIER, 4..6)
        put(PRIMAL_WARHAMMER, 4..6)
        put(PRIMAL_BATTLEAXE, 4..6)
        this
    }

    @JvmStatic val locMap = mutableMapOf<Int, String>().run {
        put(PRIMAL_FULL_HELM, "World Events")
        put(PRIMAL_CHAINBODY, "World Events")
        put(PRIMAL_GAUNTLETS, "World Events")
        put(PRIMAL_PLATEBODY, "World Events")
        put(PRIMAL_PLATESKIRT, "World Events")
        put(PRIMAL_PLATELEGS,"World Events")
        put(PRIMAL_BOOTS, "World Events")

        put(PRIMAL_2H_SWORD, "Kalphite Queen")
        put(PRIMAL_PICKAXE, "Shooting Stars")
        put(PRIMAL_SPEAR, "Nomad")
        put(PRIMAL_KITESHIELD, "Bork")
        put(PRIMAL_HATCHET, "Gauntlet")
        put(PRIMAL_MAUL, "Araxxor")
        put(PRIMAL_LONGSWORD, "God Wars")
        put(PRIMAL_RAPIER, "Corporeal Beast")
        put(PRIMAL_WARHAMMER, "Chaos Chest")
        put(PRIMAL_BATTLEAXE, "Larrans Keys")
        this
    }


}
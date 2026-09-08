package com.near_reality.cache_tool.packing.custom

import com.zenyte.game.item.ids.*
import mgi.types.config.items.ItemDefinitions

object NearRealityRaidsItemDefinitions {

    @JvmStatic
    fun makeCavernGrubsStackable() {
        ItemDefinitions.get(CAVERN_GRUBS).apply {
            setIsStackable(1)
            pack()
        }
    }

    @JvmStatic
    fun makeKindlingStackable() {
        ItemDefinitions.get(KINDLING_20799).apply {
            setIsStackable(1)
            pack()
        }
    }
}

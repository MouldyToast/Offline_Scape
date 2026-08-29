package com.near_reality.cache_tool.packing.custom

import com.zenyte.game.item.ItemId
import mgi.types.config.items.ItemDefinitions

object NearRealityReducePricesItemDefinitions {

    private val itemIds = listOf(
        ItemId.WATCH,
        ItemId.SEXTANT,
        ItemId.CHART,
        ItemId.SECURITY_BOOK
    )

    @JvmStatic
    fun reducePrices() {
        itemIds.forEach {
            val itemDef = ItemDefinitions.get(it)
            itemDef.price = 1
            itemDef.pack()
        }

        var itemDef = ItemDefinitions.get(ItemId.STEEL_PICKAXE) //originally 500
        itemDef.price = 353
        itemDef.pack()

        itemDef = ItemDefinitions.get(ItemId.STEEL_AXE) //originally 200
        itemDef.price = 186
        itemDef.pack()

        itemDef = ItemDefinitions.get(ItemId.IRON_PICKAXE) //originally 140
        itemDef.price = 55
        itemDef.pack()
    }
}

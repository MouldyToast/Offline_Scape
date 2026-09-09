package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class ProspectorPercySNuggetShop : ShopScript() {

    init {
        "Prospector Percy's Nugget Shop"(500, GOLD_NUGGETS, STOCK_ONLY) {
            PROSPECTOR_HELMET(100, 12, 16)
            PROSPECTOR_JACKET(100, 19, 24)
            PROSPECTOR_LEGS(100, 16, 20)
            PROSPECTOR_BOOTS(100, 9, 12)
            COAL_BAG_12019(1, 32, 40)
            GEM_BAG_12020(1, 32, 40)
            SOFT_CLAY_PACK(1000, 3, 4)
            BAG_FULL_OF_GEMS(100, 12, 16)
        }
    }
}

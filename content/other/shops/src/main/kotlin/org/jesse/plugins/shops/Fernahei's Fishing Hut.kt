package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*
import org.jesse.game.content.universalshop.*
import org.jesse.game.content.universalshop.UnivShopItem
import org.jesse.game.content.universalshop.UnivShopItem.*

class FernaheiSFishingHut : ShopScript() {

    init {
        "Fernahei's Fishing Hut"(107, ShopCurrency.COINS, STOCK_ONLY) {
            FISHING_ROD(5, 3, 5)
            FLY_FISHING_ROD(5, 3, 5)
            FISHING_BAIT(200, 2, 3)
            BAIT_PACK(30, 210, 300)
            FEATHER(800, 1, 2)
            FEATHER_PACK(50, 142, 200)
            RAW_TROUT(0, 14, 20)
            RAW_PIKE(0, 15, 25)
            RAW_SALMON(0, 35, 50)
        }
    }
}

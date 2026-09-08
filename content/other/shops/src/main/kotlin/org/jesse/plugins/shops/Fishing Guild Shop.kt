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

class FishingGuildShop : ShopScript() {

    init {
        "Fishing Guild Shop"(58, ShopCurrency.COINS, STOCK_ONLY) {
            FISHING_BAIT(2000, -1, 3)
            BAIT_PACK(150, -1, 300)
            FEATHER(1500, -1, 2)
            FEATHER_PACK(150, -1, 200)
            RAW_COD(0, -1, 25)
            RAW_MACKEREL(0, -1, 17)
            RAW_BASS(0, -1, 120)
            RAW_TUNA(0, -1, 100)
            RAW_LOBSTER(0, -1, 150)
            RAW_SWORDFISH(0, -1, 200)
            COD(0, -1, 25)
            MACKEREL(0, -1, 17)
            BASS(0, -1, 120)
            TUNA(0, -1, 100)
            LOBSTER(0, -1, 150)
            SWORDFISH(0, -1, 200)
        }
    }
}

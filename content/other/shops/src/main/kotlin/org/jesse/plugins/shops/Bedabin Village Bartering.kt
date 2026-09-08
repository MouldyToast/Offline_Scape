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

class BedabinVillageBartering : ShopScript() {

    init {
        "Bedabin Village Bartering"(131, ShopCurrency.COINS, CAN_SELL) {
            WATERSKIN4(5, -1, 36)
            WATERSKIN0(5, -1, 18)
            JUG_OF_WATER(5, -1, 1)
            BOWL_OF_WATER(5, -1, 4)
            BUCKET_OF_WATER(5, -1, 7)
            KNIFE(5, -1, 7)
            HAMMER(5, -1, 1)
        }
    }
}

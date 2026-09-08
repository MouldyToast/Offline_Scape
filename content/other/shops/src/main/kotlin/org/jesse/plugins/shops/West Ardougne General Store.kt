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

class WestArdougneGeneralStore : ShopScript() {

    init {
        "West Ardougne General Store"(96, ShopCurrency.COINS, CAN_SELL, 0.550000011920929) {
            POT(3, 0, 1)
            ROPE(3, 9, 21)
            BRONZE_PICKAXE(2, 0, 1)
            BUCKET(2, 1, 2)
            TINDERBOX(2, 0, 1)
            HAMMER(3, 0, 1)
            LEATHER_BOOTS(2, 3, 7)
            LONGBOW(2, 44, 96)
            BRONZE_ARROW(20, 0, 1)
            SALMON(10, 27, 60)
            MEAT_PIE(10, 8, 18)
            BREAD(5, 6, 14)
            COOKED_MEAT(10, 2, 4)
        }
    }
}

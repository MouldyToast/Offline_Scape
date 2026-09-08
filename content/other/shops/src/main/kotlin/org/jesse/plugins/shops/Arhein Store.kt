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

class ArheinStore : ShopScript() {

    init {
        "Arhein Store"(71, ShopCurrency.COINS, CAN_SELL) {
            BUCKET(10, 1, 3)
            BRONZE_PICKAXE(2, 0, 1)
            BOWL(2, 1, 5)
            CAKE_TIN(2, 4, 13)
            TINDERBOX(2, 0, 1)
            CHISEL(2, 0, 1)
            HAMMER(5, 0, 1)
            ROPE(2, 7, 23)
            POT(2, 0, 1)
            KNIFE(2, 2, 7)
        }
    }
}

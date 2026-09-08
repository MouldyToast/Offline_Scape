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

class RanaelSSuperSkirtStore : ShopScript() {

    init {
        "Ranael's Super Skirt Store"(126, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_PLATESKIRT(5, 24, 80)
            IRON_PLATESKIRT(3, 84, 280)
            STEEL_PLATESKIRT(2, 300, 1000)
            BLACK_PLATESKIRT(1, 576, 1920)
            MITHRIL_PLATESKIRT(1, 780, 2600)
            ADAMANT_PLATESKIRT(1, 1920, 6400)
        }
    }
}

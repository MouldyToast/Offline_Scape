package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class GaiusTwoHandedShop : ShopScript() {

    init {
        "Gaius' Two-Handed Shop"(24, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_2H_SWORD(4, 48, 80)
            IRON_2H_SWORD(3, 168, 280)
            STEEL_2H_SWORD(2, 600, 1000)
            BLACK_2H_SWORD(1, 1152, 1920)
            MITHRIL_2H_SWORD(1, 1560, 2600)
            ADAMANT_2H_SWORD(1, 3840, 6400)
        }
    }
}

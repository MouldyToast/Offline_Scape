package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class NurmofSPickaxeShop : ShopScript() {

    init {
        "Nurmof's Pickaxe Shop"(7, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_PICKAXE(6, 0, 1)
            IRON_PICKAXE(5, 33, 140)
            STEEL_PICKAXE(4, 212, 500)
            MITHRIL_PICKAXE(3, 780, 1300)
            ADAMANT_PICKAXE(2, 1920, 3200)
            RUNE_PICKAXE(1, 19200, 32000)
        }
    }
}

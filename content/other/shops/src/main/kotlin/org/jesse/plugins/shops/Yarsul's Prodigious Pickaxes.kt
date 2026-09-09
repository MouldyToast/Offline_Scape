package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class YarsulSProdigiousPickaxes : ShopScript() {

    init {
        "Yarsul's Prodigious Pickaxes"(62, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_PICKAXE(6, -1, 1)
            IRON_PICKAXE(5, -1, 140)
            STEEL_PICKAXE(4, -1, 500)
            MITHRIL_PICKAXE(3, -1, 1300)
            ADAMANT_PICKAXE(2, -1, 3200)
            RUNE_PICKAXE(1, -1, 32000)
        }
    }
}

package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class PickaxeIsMine : ShopScript() {

    init {
        "Pickaxe-Is-Mine"(190, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_PICKAXE(6, 1, 1)
            STEEL_PICKAXE(4, 166, 650)
            MITHRIL_PICKAXE(3, 433, 1690)
            ADAMANT_PICKAXE(2, 1066, 4160)
            RUNE_PICKAXE(1, 10666, 41600)
        }
    }
}

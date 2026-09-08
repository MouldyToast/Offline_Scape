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

class ToothySPickaxes : ShopScript() {

    init {
        "Toothy's Pickaxes"(211, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_PICKAXE(5, -1, 1)
            IRON_PICKAXE(3, -1, 182)
            STEEL_PICKAXE(3, -1, 650)
            MITHRIL_PICKAXE(2, -1, 1690)
            ADAMANT_PICKAXE(1, -1, 4160)
            RUNE_PICKAXE(1, 17600, 41600)
            POT(50000, 0, 1)
        }
    }
}

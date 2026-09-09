package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class WeaponsGalore : ShopScript() {

    init {
        "Weapons galore"(40, ShopCurrency.COINS, STOCK_ONLY) {
            MITHRIL_LONGSWORD(4, 910, 1430)
            MITHRIL_WARHAMMER(4, 1162, 1826)
            MITHRIL_BATTLEAXE(4, 1183, 1859)
            MITHRIL_CLAWS(4, 332, 522)
            MITHRIL_2H_SWORD(4, 1820, 2860)
        }
    }
}

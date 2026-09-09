package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class ValaineSShopOfChampions : ShopScript() {

    init {
        "Valaine's Shop of Champions"(56, ShopCurrency.COINS, STOCK_ONLY) {
            BLUE_CAPE(2, 12, 41)
            BLACK_FULL_HELM(1, 422, 1372)
            BLACK_PLATELEGS(1, 767, 2496)
            ADAMANT_PLATEBODY(1, 5120, 21632)
        }
    }
}

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

class BrianSBattleaxeBazaar : ShopScript() {

    init {
        "Brian's Battleaxe Bazaar"(16, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_BATTLEAXE(4, 28, 52)
            IRON_BATTLEAXE(3, 100, 182)
            STEEL_BATTLEAXE(2, 260, 650)
            BLACK_BATTLEAXE(1, 686, 1248)
            MITHRIL_BATTLEAXE(1, 929, 1690)
            ADAMANT_BATTLEAXE(1, 2160, 4160)
        }
    }
}

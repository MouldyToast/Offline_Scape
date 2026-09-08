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

class LegendsGuildShopOfUsefulItems : ShopScript() {

    init {
        "Legends Guild Shop of Useful Items"(61, ShopCurrency.COINS, STOCK_ONLY) {
            DUSTY_KEY(5, 0, 1)
            MAZE_KEY(3, 0, 1)
            SHIELD_RIGHT_HALF(1, 250000, 750000)
            CAPE_OF_LEGENDS(3, 0, 675)
        }
    }
}

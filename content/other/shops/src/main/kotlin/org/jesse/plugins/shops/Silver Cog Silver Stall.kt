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

class SilverCogSilverStall : ShopScript() {

    init {
        "Silver Cog Silver Stall"(191, ShopCurrency.COINS, STOCK_ONLY) {
            UNSTRUNG_SYMBOL(2, 160, 300)
            SILVER_ORE(1, 60, 112)
            SILVER_BAR(1, 120, 225)
        }
    }
}

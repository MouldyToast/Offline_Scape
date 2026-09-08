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

class ArdougneGemStall : ShopScript() {

    init {
        "Ardougne Gem Stall"(80, ShopCurrency.COINS, STOCK_ONLY) {
            SAPPHIRE(2, 200, 375)
            EMERALD(1, 400, 750)
            RUBY(1, 800, 1500)
            DIAMOND(0, 1600, 3000)
        }
    }
}

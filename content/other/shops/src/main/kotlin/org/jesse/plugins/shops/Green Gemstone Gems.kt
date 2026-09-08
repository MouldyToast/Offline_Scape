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

class GreenGemstoneGems : ShopScript() {

    init {
        "Green Gemstone Gems"(186, ShopCurrency.COINS, STOCK_ONLY) {
            SAPPHIRE(3, 200, 390)
            EMERALD(1, 400, 750)
            RUBY(1, 800, 1500)
            DIAMOND(0, 1600, 3000)
        }
    }
}

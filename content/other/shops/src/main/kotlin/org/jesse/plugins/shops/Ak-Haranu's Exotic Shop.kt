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

class AkHaranuSExoticShop : ShopScript() {

    init {
        "Ak-Haranu's Exotic Shop"(177, ShopCurrency.COINS, STOCK_ONLY) {
            BOLT_RACK(10_000_000, 27, 50)
        }
    }
}

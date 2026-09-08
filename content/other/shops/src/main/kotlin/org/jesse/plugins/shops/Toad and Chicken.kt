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

class ToadAndChicken : ShopScript() {

    init {
        "Toad and Chicken"(3, ShopCurrency.COINS, STOCK_ONLY) {
            ASGARNIAN_ALE(12, 0, 2)
            WIZARDS_MIND_BOMB(12, 0, 2)
            DWARVEN_STOUT(12, 0, 2)
        }
    }
}

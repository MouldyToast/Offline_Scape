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

class HappyHeroesHEmporium : ShopScript() {

    init {
        "Happy Heroes' H'Emporium"(59, ShopCurrency.COINS, STOCK_ONLY) {
            DRAGON_BATTLEAXE(1, 110000, 200000)
            DRAGON_MACE(1, 27500, 50000)
        }
    }
}

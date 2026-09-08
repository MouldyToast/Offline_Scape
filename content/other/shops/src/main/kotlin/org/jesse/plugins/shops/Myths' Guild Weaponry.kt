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

class MythsGuildWeaponry : ShopScript() {

    init {
        "Myths' Guild Weaponry"(32, ShopCurrency.COINS, STOCK_ONLY) {
            DRAGON_DAGGER(2, 18000, 30000)
            DRAGON_LONGSWORD(2, 60000, 100000)
            DRAGON_MACE(2, 30000, 50000)
            DRAGON_BATTLEAXE(2, 120000, 200000)
        }
    }
}

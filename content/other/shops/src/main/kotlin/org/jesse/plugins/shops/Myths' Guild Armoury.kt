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

class MythsGuildArmoury : ShopScript() {

    init {
        "Myths' Guild Armoury"(31, ShopCurrency.COINS, STOCK_ONLY) {
            SHIELD_RIGHT_HALF(1, 250000, 750000)
            DRAGON_METAL_SHARD(1, 600000, 1800000)
        }
    }
}

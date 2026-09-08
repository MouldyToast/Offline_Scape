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

class KingNarnodeSRoyalSeedPods : ShopScript() {

    init {
        "King Narnode's Royal Seed Pods"(94, ShopCurrency.COINS, STOCK_ONLY) {
            ROYAL_SEED_POD(30, 0, 2)
        }
    }
}

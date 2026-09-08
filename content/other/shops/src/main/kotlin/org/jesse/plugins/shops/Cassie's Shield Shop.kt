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

class CassieSShieldShop : ShopScript() {

    init {
        "Cassie's Shield Shop"(10, ShopCurrency.COINS, STOCK_ONLY) {
            WOODEN_SHIELD(5, 12, 20)
            BRONZE_SQ_SHIELD(3, 28, 48)
            BRONZE_KITESHIELD(3, 40, 68)
            IRON_SQ_SHIELD(2, 100, 168)
            IRON_KITESHIELD(0, 142, 223)
            STEEL_SQ_SHIELD(0, 360, 600)
            STEEL_KITESHIELD(0, 510, 850)
            MITHRIL_SQ_SHIELD(0, 936, 1560)
        }
    }
}

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

class QualityWeaponsShop : ShopScript() {

    init {
        "Quality Weapons Shop"(197, ShopCurrency.COINS, STOCK_ONLY) {
            STEEL_BATTLEAXE(10, 216, 650)
            MITHRIL_BATTLEAXE(10, 563, 1690)
            MITHRIL_SWORD(10, 281, 845)
            ADAMANT_SWORD(10, 693, 2080)
            STEEL_SCIMITAR(10, 133, 400)
            BLACK_LONGSWORD(10, 320, 960)
            RUNE_LONGSWORD(10, 10666, 42560)
            MAPLE_SHORTBOW(10, 133, 400)
            MAPLE_LONGBOW(10, 213, 640)
            MITHRIL_ARROW(100, 25, 76)
            ADAMANT_ARROW(100, 57, 172)
        }
    }
}

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

class MountKaruulmWeaponShop : ShopScript() {

    init {
        "Mount Karuulm Weapon Shop"(ShopCurrency.COINS, STOCK_ONLY) {
            STEEL_SPEAR(2, 195, 325)
            MITHRIL_SPEAR(1, 507, 845)
            ADAMANT_SPEAR(1, 1248, 2080)
            RUNE_SPEAR(1, 12480, 20800)
            STEEL_BATTLEAXE(2, 390, 650)
            MITHRIL_BATTLEAXE(1, 1014, 1690)
            ADAMANT_BATTLEAXE(1, 2496, 4160)
            STEEL_WARHAMMER(2, 384, 640)
            MITHRIL_WARHAMMER(1, 996, 1660)
            ADAMANT_WARHAMMER(1, 2472, 4120)
        }
    }
}

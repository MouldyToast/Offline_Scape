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

class MoonClanFineClothes : ShopScript() {

    init {
        "Moon Clan Fine Clothes"(44, ShopCurrency.COINS, STOCK_ONLY) {
            MOONCLAN_HELM(9, 0, 1000)
            MOONCLAN_HAT(10, 0, 1000)
            MOONCLAN_ARMOUR(10, 0, 1000)
            MOONCLAN_SKIRT(10, 0, 1000)
            MOONCLAN_GLOVES(9, 0, 900)
            MOONCLAN_BOOTS(12, 0, 900)
            MOONCLAN_CAPE(15, 0, 200)
        }
    }
}

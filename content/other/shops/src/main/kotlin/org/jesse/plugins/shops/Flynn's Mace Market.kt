package org.jesse.plugins.shops

import org.jesse.scripts.shops.ShopScript
import org.jesse.game.model.shop.*
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.game.model.shop.ShopPolicy.*
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopCurrency.*
import org.jesse.game.item.ids.*

class FlynnSMaceMarket : ShopScript() {

    init {
        "Flynn's Mace Market"(12, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_MACE(5, 10, 18)
            IRON_MACE(4, 37, 63)
            STEEL_MACE(4, 135, 225)
            MITHRIL_MACE(3, 351, 585)
            ADAMANT_MACE(2, 864, 1440)
        }
    }
}

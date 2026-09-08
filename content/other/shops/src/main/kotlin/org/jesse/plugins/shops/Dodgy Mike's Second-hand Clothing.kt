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

class DodgyMikeSSecondHandClothing : ShopScript() {

    init {
        "Dodgy Mike's Second-hand Clothing"(229, ShopCurrency.COINS, STOCK_ONLY) {
            PIRATE_BOOTS(15, 210, 350)
            STRIPY_PIRATE_SHIRT(10, 180, 300)
            PIRATE_BANDANA(20, 60, 100)
            PIRATE_LEGGINGS(10, 210, 350)
            STRIPY_PIRATE_SHIRT_7122(10, 180, 300)
            PIRATE_BANDANA_7124(20, 60, 100)
            PIRATE_LEGGINGS_7126(10, 210, 350)
            STRIPY_PIRATE_SHIRT_7128(10, 180, 300)
            PIRATE_BANDANA_7130(20, 60, 100)
            PIRATE_LEGGINGS_7132(10, 210, 350)
            STRIPY_PIRATE_SHIRT_7134(10, 180, 300)
            PIRATE_BANDANA_7136(20, 60, 100)
            PIRATE_LEGGINGS_7138(10, 210, 350)
        }
    }
}

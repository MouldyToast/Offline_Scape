package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.model.shop.*
import com.zenyte.game.model.shop.ShopPolicy
import com.zenyte.game.model.shop.ShopPolicy.*
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopCurrency.*
import com.zenyte.game.item.ids.*
import com.near_reality.game.content.universalshop.*
import com.near_reality.game.content.universalshop.UnivShopItem
import com.near_reality.game.content.universalshop.UnivShopItem.*

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

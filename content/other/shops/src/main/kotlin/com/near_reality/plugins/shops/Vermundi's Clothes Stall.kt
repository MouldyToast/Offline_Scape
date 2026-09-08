package com.near_reality.plugins.shops

import com.near_reality.scripts.shops.ShopScript
import com.zenyte.game.model.shop.*
import com.zenyte.game.model.shop.ShopPolicy
import com.zenyte.game.model.shop.ShopPolicy.*
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.model.shop.ShopCurrency.*
import com.zenyte.game.item.ItemId
import com.near_reality.game.content.universalshop.*
import com.near_reality.game.content.universalshop.UnivShopItem
import com.near_reality.game.content.universalshop.UnivShopItem.*
import com.zenyte.game.item.ItemId.SHIRT
import com.zenyte.game.item.ItemId.SHORTS
import com.zenyte.game.item.ItemId.SILK
import com.zenyte.game.item.ItemId.SKIRT
import com.zenyte.game.item.ItemId.TROUSERS
import com.zenyte.game.item.ItemId.WOVEN_TOP

class VermundiSClothesStall : ShopScript() {

    init {
        "Vermundi's Clothes Stall"(192, ShopCurrency.COINS, STOCK_ONLY) {
            SKIRT(3, 192, 455)
            TROUSERS(3, 302, 715)
            SHORTS(3, 154, 364)
            WOVEN_TOP(3, 275, 650)
            SHIRT(3, 247, 585)
            SILK(5, 16, 39)
        }
    }
}

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
import com.zenyte.game.item.ItemId.DIAMOND_AMULET
import com.zenyte.game.item.ItemId.DIAMOND_NECKLACE
import com.zenyte.game.item.ItemId.DIAMOND_RING
import com.zenyte.game.item.ItemId.EMERALD_AMULET
import com.zenyte.game.item.ItemId.EMERALD_NECKLACE
import com.zenyte.game.item.ItemId.EMERALD_RING
import com.zenyte.game.item.ItemId.GOLD_AMULET
import com.zenyte.game.item.ItemId.GOLD_NECKLACE
import com.zenyte.game.item.ItemId.GOLD_RING
import com.zenyte.game.item.ItemId.RUBY_AMULET
import com.zenyte.game.item.ItemId.RUBY_NECKLACE
import com.zenyte.game.item.ItemId.RUBY_RING
import com.zenyte.game.item.ItemId.SAPPHIRE_AMULET
import com.zenyte.game.item.ItemId.SAPPHIRE_NECKLACE
import com.zenyte.game.item.ItemId.SAPPHIRE_RING

class GrumSGoldExchange : ShopScript() {

    init {
        "Grum's Gold Exchange"(18, ShopCurrency.COINS, STOCK_ONLY) {
            GOLD_RING(0, 245, 350)
            SAPPHIRE_RING(0, 630, 900)
            EMERALD_RING(0, 892, 1275)
            RUBY_RING(0, 1417, 2025)
            DIAMOND_RING(0, 2467, 3525)
            GOLD_NECKLACE(0, 315, 450)
            SAPPHIRE_NECKLACE(0, 735, 1050)
            EMERALD_NECKLACE(0, 997, 1425)
            RUBY_NECKLACE(0, 1522, 2175)
            DIAMOND_NECKLACE(0, 2570, 3675)
            GOLD_AMULET(0, 245, 350)
            SAPPHIRE_AMULET(0, 630, 900)
            EMERALD_AMULET(0, 892, 1275)
            RUBY_AMULET(0, 1417, 2025)
            DIAMOND_AMULET(0, 2467, 3525)
        }
    }
}

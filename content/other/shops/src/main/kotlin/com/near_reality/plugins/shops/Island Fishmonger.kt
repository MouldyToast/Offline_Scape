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
import com.zenyte.game.item.ItemId.BAIT_PACK
import com.zenyte.game.item.ItemId.BIG_FISHING_NET
import com.zenyte.game.item.ItemId.FEATHER
import com.zenyte.game.item.ItemId.FEATHER_PACK
import com.zenyte.game.item.ItemId.FISHING_BAIT
import com.zenyte.game.item.ItemId.FISHING_ROD
import com.zenyte.game.item.ItemId.FLY_FISHING_ROD
import com.zenyte.game.item.ItemId.HARPOON
import com.zenyte.game.item.ItemId.LOBSTER_POT
import com.zenyte.game.item.ItemId.RAW_ANCHOVIES
import com.zenyte.game.item.ItemId.RAW_BASS
import com.zenyte.game.item.ItemId.RAW_COD
import com.zenyte.game.item.ItemId.RAW_HERRING
import com.zenyte.game.item.ItemId.RAW_LOBSTER
import com.zenyte.game.item.ItemId.RAW_MACKEREL
import com.zenyte.game.item.ItemId.RAW_PIKE
import com.zenyte.game.item.ItemId.RAW_SALMON
import com.zenyte.game.item.ItemId.RAW_SARDINE
import com.zenyte.game.item.ItemId.RAW_SHARK
import com.zenyte.game.item.ItemId.RAW_SHRIMPS
import com.zenyte.game.item.ItemId.RAW_SWORDFISH
import com.zenyte.game.item.ItemId.RAW_TROUT
import com.zenyte.game.item.ItemId.RAW_TUNA
import com.zenyte.game.item.ItemId.SMALL_FISHING_NET

class IslandFishmonger : ShopScript() {

    init {
        "Island Fishmonger"(47, ShopCurrency.COINS, STOCK_ONLY) {
            SMALL_FISHING_NET(5, -1, 6)
            FISHING_ROD(5, -1, 6)
            FLY_FISHING_ROD(5, -1, 6)
            HARPOON(2, -1, 6)
            LOBSTER_POT(2, -1, 26)
            FISHING_BAIT(1500, -1, 3)
            BAIT_PACK(80, -1, 390)
            FEATHER(1000, -1, 2)
            FEATHER_PACK(100, -1, 260)
            BIG_FISHING_NET(5, -1, 26)
            RAW_SHRIMPS(0, -1, 6)
            RAW_SARDINE(200, -1, 13)
            RAW_HERRING(0, -1, 19)
            RAW_MACKEREL(0, -1, 22)
            RAW_COD(0, -1, 32)
            RAW_ANCHOVIES(0, -1, 19)
            RAW_TROUT(0, -1, 26)
            RAW_PIKE(0, -1, 32)
            RAW_SALMON(0, -1, 65)
            RAW_TUNA(0, -1, 130)
            RAW_LOBSTER(0, -1, 195)
            RAW_BASS(0, -1, 156)
            RAW_SWORDFISH(0, -1, 260)
            RAW_SHARK(0, -1, 390)
        }
    }
}

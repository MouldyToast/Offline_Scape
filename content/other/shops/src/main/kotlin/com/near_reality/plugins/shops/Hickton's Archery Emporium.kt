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
import com.zenyte.game.item.ItemId.ADAMANT_ARROW
import com.zenyte.game.item.ItemId.ADAMANT_ARROWTIPS
import com.zenyte.game.item.ItemId.ADAMANT_BRUTAL
import com.zenyte.game.item.ItemId.BLACK_BRUTAL
import com.zenyte.game.item.ItemId.BRONZE_ARROW
import com.zenyte.game.item.ItemId.BRONZE_ARROWTIPS
import com.zenyte.game.item.ItemId.BRONZE_BOLTS
import com.zenyte.game.item.ItemId.BRONZE_BRUTAL
import com.zenyte.game.item.ItemId.COMP_OGRE_BOW
import com.zenyte.game.item.ItemId.CROSSBOW
import com.zenyte.game.item.ItemId.IRON_ARROW
import com.zenyte.game.item.ItemId.IRON_ARROWTIPS
import com.zenyte.game.item.ItemId.IRON_BRUTAL
import com.zenyte.game.item.ItemId.LONGBOW
import com.zenyte.game.item.ItemId.MITHRIL_ARROW
import com.zenyte.game.item.ItemId.MITHRIL_ARROWTIPS
import com.zenyte.game.item.ItemId.MITHRIL_BRUTAL
import com.zenyte.game.item.ItemId.OAK_LONGBOW
import com.zenyte.game.item.ItemId.OAK_SHORTBOW
import com.zenyte.game.item.ItemId.RUNE_ARROW
import com.zenyte.game.item.ItemId.RUNE_ARROWTIPS
import com.zenyte.game.item.ItemId.RUNE_BRUTAL
import com.zenyte.game.item.ItemId.SHORTBOW
import com.zenyte.game.item.ItemId.STEEL_ARROW
import com.zenyte.game.item.ItemId.STEEL_ARROWTIPS
import com.zenyte.game.item.ItemId.STEEL_BRUTAL
import com.zenyte.game.item.ItemId.STUDDED_BODY
import com.zenyte.game.item.ItemId.STUDDED_CHAPS

class HicktonSArcheryEmporium : ShopScript() {

    init {
        "Hickton's Archery Emporium"(74, ShopCurrency.COINS, STOCK_ONLY) {
            BRONZE_BOLTS(200, 0, 1)
            BRONZE_ARROW(1000, 0, 1)
            IRON_ARROW(800, 1, 3)
            STEEL_ARROW(0, 4, 12)
            MITHRIL_ARROW(0, 10, 32)
            ADAMANT_ARROW(0, 26, 80)
            RUNE_ARROW(0, 133, 400)
            BRONZE_BRUTAL(0, 1, 5)
            IRON_BRUTAL(0, 3, 10)
            STEEL_BRUTAL(0, 6, 20)
            BLACK_BRUTAL(0, 11, 35)
            MITHRIL_BRUTAL(0, 16, 50)
            ADAMANT_BRUTAL(0, 31, 95)
            RUNE_BRUTAL(0, 150, 450)
            BRONZE_ARROWTIPS(1000, 0, 1)
            IRON_ARROWTIPS(800, 0, 2)
            STEEL_ARROWTIPS(600, 2, 6)
            MITHRIL_ARROWTIPS(400, 8, 16)
            ADAMANT_ARROWTIPS(200, 20, 40)
            RUNE_ARROWTIPS(100, 100, 200)
            SHORTBOW(4, 16, 50)
            LONGBOW(2, 26, 80)
            CROSSBOW(2, 23, 70)
            OAK_SHORTBOW(4, 33, 100)
            OAK_LONGBOW(4, 53, 160)
            COMP_OGRE_BOW(0, 60, 180)
            STUDDED_BODY(2, 283, 850)
            STUDDED_CHAPS(2, 250, 750)
        }
    }
}

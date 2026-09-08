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
import com.zenyte.game.item.ItemId.OBSIDIAN_CAPE
import com.zenyte.game.item.ItemId.OBSIDIAN_HELMET
import com.zenyte.game.item.ItemId.OBSIDIAN_PLATEBODY
import com.zenyte.game.item.ItemId.OBSIDIAN_PLATELEGS
import com.zenyte.game.item.ItemId.TOKTZKETXIL
import com.zenyte.game.item.ItemId.TOKTZMEJTAL
import com.zenyte.game.item.ItemId.TOKTZXILAK
import com.zenyte.game.item.ItemId.TOKTZXILEK
import com.zenyte.game.item.ItemId.TOKTZXILUL
import com.zenyte.game.item.ItemId.TZHAARKETEM
import com.zenyte.game.item.ItemId.TZHAARKETOM

class TzhaarHurZalSEquipmentStore : ShopScript() {

    init {
        "TzHaar-Hur-Zal's Equipment Store"(113, ShopCurrency.TOKKUL, STOCK_ONLY) {
            TOKTZXILUL(500, 34, 375)
            TOKTZXILAK(1, 6000, 60000)
            TOKTZXILEK(1, 3550, 37500)
            TZHAARKETOM(1, 7500, 75000)
            TOKTZMEJTAL(1, 5000, 52500)
            TZHAARKETEM(1, 4000, 45000)
            TOKTZKETXIL(1, 6750, 67500)
            OBSIDIAN_CAPE(1, 9000, 90000)
            OBSIDIAN_HELMET(1, 8448, 84480)
            OBSIDIAN_PLATEBODY(1, 12600, 126000)
            OBSIDIAN_PLATELEGS(1, 10050, 100500)
        }
    }
}

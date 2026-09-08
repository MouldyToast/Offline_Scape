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
import com.zenyte.game.item.ItemId.BAILING_BUCKET
import com.zenyte.game.item.ItemId.BLACK_TOY_HORSEY
import com.zenyte.game.item.ItemId.BLUE_HAT
import com.zenyte.game.item.ItemId.COCKTAIL_GUIDE
import com.zenyte.game.item.ItemId.DESERT_BOOTS
import com.zenyte.game.item.ItemId.DRAGON_DAGGER
import com.zenyte.game.item.ItemId.FLAMTAER_HAMMER
import com.zenyte.game.item.ItemId.FREMENNIK_PINK_CLOAK
import com.zenyte.game.item.ItemId.GREENMANS_ALE
import com.zenyte.game.item.ItemId.GREY_BOOTS
import com.zenyte.game.item.ItemId.HOLY_MOULD
import com.zenyte.game.item.ItemId.INSTRUCTION_MANUAL
import com.zenyte.game.item.ItemId.KEG_OF_BEER
import com.zenyte.game.item.ItemId.LIMESTONE_BRICK
import com.zenyte.game.item.ItemId.MACHETE
import com.zenyte.game.item.ItemId.NEWCOMER_MAP
import com.zenyte.game.item.ItemId.OLIVE_OIL3
import com.zenyte.game.item.ItemId.PAPYRUS
import com.zenyte.game.item.ItemId.POISON
import com.zenyte.game.item.ItemId.SAMPLE_BOTTLE
import com.zenyte.game.item.ItemId.SHANTAY_PASS
import com.zenyte.game.item.ItemId.SICKLE_MOULD
import com.zenyte.game.item.ItemId.SPINACH_ROLL
import com.zenyte.game.item.ItemId.SWAMP_PASTE
import com.zenyte.game.item.ItemId.WATERSKIN4

class RasoloTheWanderingMerchant : ShopScript() {

    init {
        "Rasolo the Wandering Merchant"(100, ShopCurrency.COINS, STOCK_ONLY) {
            SPINACH_ROLL(1, 0, 2)
            COCKTAIL_GUIDE(1, 0, 4)
            BLUE_HAT(1, 16, 320)
            DRAGON_DAGGER(1, 3000, 60000)
            NEWCOMER_MAP(1, 0, 2)
            BAILING_BUCKET(1, 1, 20)
            SWAMP_PASTE(1, 3, 60)
            POISON(1, 0, 2)
            PAPYRUS(1, 1, 20)
            MACHETE(1, 4, 80)
            HOLY_MOULD(1, 0, 10)
            SICKLE_MOULD(1, 1, 20)
            WATERSKIN4(1, 3, 60)
            DESERT_BOOTS(1, 2, 40)
            SHANTAY_PASS(1, 0, 10)
            BLACK_TOY_HORSEY(1, 10, 200)
            SAMPLE_BOTTLE(1, 0, 10)
            GREY_BOOTS(1, 50, 1000)
            GREENMANS_ALE(1, 0, 4)
            FREMENNIK_PINK_CLOAK(1, 25, 500)
            KEG_OF_BEER(1, 25, 500)
            FLAMTAER_HAMMER(1, 1000, 20000)
            OLIVE_OIL3(1, 2, 40)
            LIMESTONE_BRICK(1, 2, 40)
            INSTRUCTION_MANUAL(1, 0, 20)
        }
    }
}

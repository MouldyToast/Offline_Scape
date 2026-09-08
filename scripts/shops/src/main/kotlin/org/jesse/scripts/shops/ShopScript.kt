package org.jesse.scripts.shops

import org.jesse.scripts.Script
import org.jesse.game.model.shop.JsonShop
import org.jesse.game.model.shop.Shop
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.model.shop.ShopDiscount
import org.jesse.game.model.shop.ShopPolicy
import org.jesse.plugins.InitPlugin
import kotlin.script.experimental.annotations.KotlinScript

/**
 * @author Jire
 */
@KotlinScript(
    "Shop Script",
    "shop.kts",
    compilationConfiguration = ShopScriptCompilation::class
)
abstract class ShopScript : Script, InitPlugin {

    operator fun String.invoke(
        uid: Int = JsonShop.INVALID_SHOP_UID,
        currency: ShopCurrency = ShopCurrency.COINS,
        policy: ShopPolicy = ShopPolicy.STOCK_ONLY,
        sellMultiplier: Double = Shop.DEFAULT_SELL_MULTIPLIER,
        discount: ShopDiscount = ShopDiscount.NONE,
        build: ShopBuilder.() -> Unit
    ) {
        val builder = ShopBuilder(this, uid, currency, policy, sellMultiplier, discount)
        builder.build()

        val jsonShop = builder.toJsonShop()
        Shop.shops[this] = Shop(jsonShop, false)
        Shop.ironmanShops[this] = Shop(jsonShop, true)
    }

    operator fun String.invoke(
        currency: ShopCurrency = ShopCurrency.COINS,
        policy: ShopPolicy = ShopPolicy.STOCK_ONLY,
        sellMultiplier: Double = Shop.DEFAULT_SELL_MULTIPLIER,
        discount: ShopDiscount = ShopDiscount.NONE,
        build: ShopBuilder.() -> Unit
    ) = invoke(JsonShop.INVALID_SHOP_UID, currency, policy, sellMultiplier, discount, build)

    operator fun String.invoke(
        uid: Int = JsonShop.INVALID_SHOP_UID,
        policy: ShopPolicy = ShopPolicy.STOCK_ONLY,
        sellMultiplier: Double = Shop.DEFAULT_SELL_MULTIPLIER,
        discount: ShopDiscount = ShopDiscount.NONE,
        build: ShopBuilder.() -> Unit
    ) = invoke(uid, ShopCurrency.COINS, policy, sellMultiplier, discount, build)

}
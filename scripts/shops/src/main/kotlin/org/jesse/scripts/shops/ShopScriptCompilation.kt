package org.jesse.scripts.shops

import org.jesse.scripts.DefaultCompilation
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.defaultImports

/**
 * @author Jire
 */
object ShopScriptCompilation : ScriptCompilationConfiguration(
    DefaultCompilation, body = {
        defaultImports(
            "org.jesse.game.model.shop.*",

            "org.jesse.game.model.shop.ShopPolicy",
            "org.jesse.game.model.shop.ShopPolicy.*",

            "org.jesse.game.model.shop.ShopCurrency",
            "org.jesse.game.model.shop.ShopCurrency.*",

            "org.jesse.game.item.ids.*",

            "org.jesse.game.content.universalshop.*",
            "org.jesse.game.content.universalshop.UnivShopItem",
            "org.jesse.game.content.universalshop.UnivShopItem.*"
        )
    }
) {
    private fun readResolve(): Any = ShopScriptCompilation
}

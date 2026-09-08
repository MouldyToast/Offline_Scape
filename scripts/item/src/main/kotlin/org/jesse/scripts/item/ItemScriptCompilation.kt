package org.jesse.scripts.item

import org.jesse.scripts.DefaultCompilation
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.defaultImports

/**
 * @author Jire
 */
object ItemScriptCompilation : ScriptCompilationConfiguration(
    DefaultCompilation, body = {
        defaultImports(
            "org.jesse.game.item.ids.*",

            "org.jesse.game.model.item.*"
        )
    }) {
    private fun readResolve(): Any = ItemScriptCompilation
}

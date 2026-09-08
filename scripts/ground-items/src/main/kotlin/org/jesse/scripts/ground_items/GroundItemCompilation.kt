package org.jesse.scripts.ground_items

import org.jesse.scripts.DefaultCompilation
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.defaultImports

/**
 * @author Jire
 */
object GroundItemCompilation : ScriptCompilationConfiguration(
    DefaultCompilation, body = {
        defaultImports(
            "org.jesse.game.item.ids.*"
        )
    }
) {
    private fun readResolve(): Any = GroundItemCompilation
}

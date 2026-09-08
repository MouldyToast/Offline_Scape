package org.jesse.scripts.`object`

import org.jesse.scripts.DefaultCompilation
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.defaultImports

/**
 * @author Jire
 */
object ObjectScriptCompilation : ScriptCompilationConfiguration(
    DefaultCompilation, body = {
        defaultImports(
            "org.jesse.game.obj.ids.*",

            "org.jesse.game.world.object.*"
        )
    }) {
    private fun readResolve(): Any = ObjectScriptCompilation
}

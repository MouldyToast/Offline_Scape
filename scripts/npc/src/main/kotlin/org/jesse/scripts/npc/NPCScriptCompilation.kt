package org.jesse.scripts.npc

import org.jesse.scripts.DefaultCompilation
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.defaultImports

/**
 * @author Jire
 */
object NPCScriptCompilation : ScriptCompilationConfiguration(
    DefaultCompilation, body = {
        defaultImports(
            "org.jesse.game.npc.ids.*",

            "org.jesse.game.util.invoke",
        )
    }) {
    private fun readResolve(): Any = NPCScriptCompilation
}

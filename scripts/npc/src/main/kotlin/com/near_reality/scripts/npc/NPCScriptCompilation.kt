package com.near_reality.scripts.npc

import com.near_reality.scripts.DefaultCompilation
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.defaultImports

/**
 * @author Jire
 */
object NPCScriptCompilation : ScriptCompilationConfiguration(
    DefaultCompilation, body = {
        defaultImports(
            "com.zenyte.game.npc.ids.*",

            "com.near_reality.game.util.invoke",
        )
    }) {
    private fun readResolve(): Any = NPCScriptCompilation
}

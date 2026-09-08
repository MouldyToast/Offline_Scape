package org.jesse.scripts.npc.spawns

import org.jesse.scripts.npc.NPCScriptCompilation
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.defaultImports

/**
 * @author Jire
 */
object NPCSpawnsCompilation : ScriptCompilationConfiguration(
    NPCScriptCompilation, body = {
        defaultImports("org.jesse.game.util.Direction.*")
    }) {
    private fun readResolve(): Any = NPCSpawnsCompilation
}

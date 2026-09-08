package org.jesse.scripts.npc.actions

import org.jesse.scripts.npc.NPCScriptCompilation
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.defaultImports

/**
 * @author Jire
 */
object NPCActionCompilation : ScriptCompilationConfiguration(
    NPCScriptCompilation, body = {
        defaultImports("org.jesse.game.world.entity.npc.actions.*")
    }) {
    private fun readResolve(): Any = NPCActionCompilation
}

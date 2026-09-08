package org.jesse.scripts.npc.drops

import org.jesse.scripts.npc.NPCScriptCompilation
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.defaultImports

/**
 * @author Jire
 */
object NPCDropsCompilation : ScriptCompilationConfiguration(
    NPCScriptCompilation, body = {
        defaultImports(
            "org.jesse.game.item.ids.*",

            "org.jesse.game.world.entity.npc.drop.matrix.Drop",
            "org.jesse.game.world.entity.npc.drop.matrix.Drop.GUARANTEED_RATE"
        )
    }) {
    private fun readResolve(): Any = NPCDropsCompilation
}

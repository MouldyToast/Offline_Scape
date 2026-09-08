package com.near_reality.scripts.npc.drops

import com.near_reality.scripts.npc.NPCScriptCompilation
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.defaultImports

/**
 * @author Jire
 */
object NPCDropsCompilation : ScriptCompilationConfiguration(
    NPCScriptCompilation, body = {
        defaultImports(
            "com.zenyte.game.item.ids.*",

            "com.zenyte.game.world.entity.npc.drop.matrix.Drop",
            "com.zenyte.game.world.entity.npc.drop.matrix.Drop.GUARANTEED_RATE"
        )
    }) {
    private fun readResolve(): Any = NPCDropsCompilation
}

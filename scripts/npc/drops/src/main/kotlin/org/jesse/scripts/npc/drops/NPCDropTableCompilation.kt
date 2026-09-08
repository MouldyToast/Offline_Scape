package org.jesse.scripts.npc.drops

import org.jesse.scripts.npc.NPCScriptCompilation
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.defaultImports

/**
 * Represents a [ScriptCompilationConfiguration] for [NPCDropTableScript].
 *
 * @author Stan van der Bend
 */
object NPCDropTableCompilation : ScriptCompilationConfiguration(
    NPCScriptCompilation, body = {
        defaultImports(
            "org.jesse.game.item.ids.*",
            "org.jesse.scripts.npc.drops.table.DropTableType.*",

            "org.jesse.game.world.entity.npc.drop.matrix.Drop",
            "org.jesse.game.world.entity.npc.drop.matrix.Drop.GUARANTEED_RATE",

            "org.jesse.game.world.entity.npc.drop.matrix.DropProcessor",
            "org.jesse.game.world.entity.npc.drop.matrix.DropProcessor.PredicatedDrop",
            "org.jesse.game.world.entity.npc.drop.matrix.DropProcessor.DisplayedDrop",
        )
    }) {
    private fun readResolve(): Any = NPCDropTableCompilation
}

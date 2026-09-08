package org.jesse.scripts.npc.drops

import org.jesse.scripts.npc.NPCScript
import org.jesse.game.world.entity.npc.drop.matrix.Drop
import org.jesse.game.world.entity.npc.drop.matrix.NPCDrops
import org.jesse.plugins.InitPlugin
import org.jesse.plugins.PluginPriority
import kotlin.script.experimental.annotations.KotlinScript

/**
 * @author Jire
 */
@KotlinScript(
    "NPC Drops Script",
    fileExtension = "drops.kts",
    compilationConfiguration = NPCDropsCompilation::class
)
@PluginPriority(1_000)
@Deprecated("Use NPCDropTableScript")
abstract class NPCDropsScript : NPCScript, InitPlugin {

    val guaranteed = Drop.GUARANTEED_RATE

    operator fun Int.invoke(weight: Int = 0, build: DropTableBuilder.() -> Unit) {
        val table = DropTableBuilder(this, weight).apply(build).buildTable()
        NPCDrops.initDropTable(table)
    }

}

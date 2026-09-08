package org.jesse.game.content.dt2.npc.theduke

import org.jesse.game.content.dt2.npc.whisperer.whispererTimerCurrent
import org.jesse.game.content.dt2.npc.whisperer.whispererTimerMax
import org.jesse.game.task.TickTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.util.Direction
import org.jesse.game.world.entity.HitBar
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.RemoveHitBar
import org.jesse.game.world.entity.masks.UpdateFlag
import org.jesse.game.world.entity.npc.NPC
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.WorldObject

/**
 * Mack wrote original logic - Kry rewrote in NR terms
 * @author John J. Woloszyk / Kryeus
 * @date 8.14.2024
 */
data class FermentationVatEntity(
    val player: Player,
    val spawnLoc: Location,
    private var progress: Int = 0,
    private var completion: Int = FERMENTATION_COMPLETION
) {
    private var fermentationTask: TickTask? = null

    fun beginFermentation(player: Player, vat: WorldObject, finishTask: () -> Unit) {
        vat.id = 47537
        player.sendMessage("The mixture in the vat begins to ferment.")
        val npc = NPC(12197, vat.position, Direction.NORTH, 0).spawn()
        val hitbar = VatProgressiveHitBar(npc)

        npc.whispererTimerMax = FERMENTATION_COMPLETION + 1
        npc.whispererTimerCurrent = 0
        npc.addHitbar(hitbar)

        fermentationTask = object: TickTask() {
            override fun run() {
                npc.whispererTimerCurrent++
                npc.addHitbar(hitbar)

                if (progress < FERMENTATION_COMPLETION) {
                    progress = (progress + 1).coerceAtMost(FERMENTATION_COMPLETION)
                    ticks++
                }
                else {
                    npc.addHitbar(RemoveHitBar(hitbar.type))

                    progress = 0
                    finishTask()
                    player.sendMessage("<col=229628>A fermentation vat is ready to be emptied.")
                    npc.remove()
                    stop()
                }
            }
        }
        WorldTasksManager.schedule(fermentationTask as TickTask, 0, 0)
    }

    companion object {
        private const val FERMENTATION_COMPLETION: Int = 18
    }
}

package com.near_reality.game.content.dt2.npc.theduke

import com.near_reality.game.content.dt2.npc.whisperer.whispererTimerCurrent
import com.near_reality.game.content.dt2.npc.whisperer.whispererTimerMax
import com.zenyte.game.task.TickTask
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Direction
import com.zenyte.game.world.entity.HitBar
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.RemoveHitBar
import com.zenyte.game.world.entity.masks.UpdateFlag
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.WorldObject

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

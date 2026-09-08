package org.jesse.game.content.dt2.npc.vardorvis.attacks.impl.swingingaxe

import org.jesse.game.content.dt2.area.VardorvisInstance
import org.jesse.game.content.seq
import org.jesse.game.task.TickTask
import org.jesse.game.world.entity.player.Player

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-08
 */
class SwingingAxeTask(
    private var instance: VardorvisInstance,
    private var player: Player,
    private var axes: List<SwingingAxe>
) : TickTask() {
    override fun run() {
        if (instance.killAxes) {
            instance.killAxes = false
            stop()
        }
        when (ticks++) {
            1 -> axes.forEach { it seq 10365 }
            2 -> {
                axes.forEach { it.setTransformation(12227) }
                checkOverlap(true)
            }

            8 -> stop()
            in 3..7 -> checkOverlap(false)
        }
    }

    override fun stop() {
        super.stop()
        axes.forEach { it.remove() }
    }

    private fun checkOverlap(first: Boolean) {
        for (axe in axes) {
            axe.path(first)
            if (axe.check(player, first)) {
                instance.applyAxeDamage()
            }
        }
    }
}
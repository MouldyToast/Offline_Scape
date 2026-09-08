package org.jesse.game.content.pvm_arena.npc

import org.jesse.game.content.pvm_arena.PvmArenaManager
import org.jesse.game.content.pvm_arena.PvmArenaState
import org.jesse.game.util.formattedString
import org.jesse.game.util.Direction
import org.jesse.game.world.WorldThread
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.npc.NPC
import org.jesse.game.npc.ids.*
import org.jesse.game.world.entity.npc.Spawnable
import kotlin.time.Duration.Companion.hours

/**
 * Handles the PvM Arena Ghost NPC (Sir Eldric), announces the time left until the arena opens.
 *
 * @author Stan van der Bend
 */
@Suppress("unused", "SpellCheckingInspection")
class PvmArenaSirEldric(id: Int, tile: Location?, facing: Direction?, radius: Int) : NPC(id, tile, facing, radius), Spawnable {

    override fun processNPC() {
        super.processNPC()
        if (everyNthWorldCycle(15)) {
            when(val state = PvmArenaManager.state) {
                is PvmArenaState.Idle -> {
                    setForceTalk("The arena will open in ${state.timeLeft.formattedString}")
                }
                else -> Unit
            }
        }
    }

    @Suppress("SameParameterValue")
    private fun everyNthWorldCycle(n: Int) = WorldThread.getCurrentCycle() % n == 0L

    override fun validate(id: Int, name: String?): Boolean {
        return id == GHOST_3516
    }
}

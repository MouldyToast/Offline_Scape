package org.jesse.game.content.bountyhunter.tasks

import org.jesse.game.content.bountyhunter.BountyHunterController
import org.jesse.game.content.bountyhunter.BountyHunterHotspot
import org.jesse.game.content.bountyhunter.BountyHunterVars
import org.jesse.game.GameConstants.DEV_DEBUG
import org.jesse.game.task.WorldTask
import org.jesse.game.world.broadcasts.BroadcastType
import org.jesse.game.world.broadcasts.WorldBroadcasts
import org.jesse.utils.TimeUnit
import java.time.Instant

/**
 * This represents the world task that schedules a new [BountyHunterHotspot]
 * at given intervals.
 * @author John J. Woloszyk / Kryeus
 */
class BountyHunterHotspotScheduler : WorldTask {

    override fun run() {
        val former = BountyHunterController.currentHotspot
        BountyHunterController.currentHotspot = BountyHunterHotspot.randomExcludeCurrent()
        BountyHunterController.currentHotspot.spawnBoundary()
        former.removeBoundary()
        val next = if (DEV_DEBUG)
            TimeUnit.MINUTES.toTicks(BountyHunterVars.HOTSPOT_CYCLE_DEV.toLong()).toInt()
        else TimeUnit.MINUTES.toTicks(BountyHunterVars.HOTSPOT_CYCLE_MAIN.toLong()).toInt()
        BountyHunterController.countdownTimer = Instant.now().plusSeconds(TimeUnit.TICKS.toSeconds(next.toLong()))
        WorldBroadcasts.broadcast(null, BroadcastType.BOUNTY_HUNTER, BountyHunterController.currentHotspot.zoneName)
    }
}
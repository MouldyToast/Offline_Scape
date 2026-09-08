package org.jesse.game.content.bountyhunter.tasks

import org.jesse.game.world.entity.player.bountyHunterInfoCooldown
import org.jesse.game.world.entity.player.bountyHunterInterfaceRateLimit
import org.jesse.game.task.WorldTask
import org.jesse.game.world.entity.player.Player

/**
 * @author John J. Woloszyk / Kryeus
 */
class BountyHunterPlayerCooldown(val player: Player) : WorldTask {

    override fun run() {
        if (player.isNulled || player.isFinished) {
            super.stop()
            return
        }

        if (player.bountyHunterInfoCooldown == 0) {
            player.bountyHunterInterfaceRateLimit = 0
            super.stop()
            return
        }

        player.bountyHunterInfoCooldown--
    }

}
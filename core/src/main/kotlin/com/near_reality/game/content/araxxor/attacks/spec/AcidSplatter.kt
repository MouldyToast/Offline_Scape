package com.near_reality.game.content.araxxor.attacks.spec

import com.near_reality.game.content.araxxor.Araxxor
import com.near_reality.game.content.araxxor.AraxxorInstance
import com.near_reality.game.content.araxxor.attacks.Attack
import com.near_reality.game.content.seq
import com.zenyte.game.task.WorldTasksManager.schedule
import com.zenyte.game.world.Projectile
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.player.Player

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-25
 */
class AcidSplatter(val instance: AraxxorInstance) : Attack {

    private val projectileBlob: Projectile = Projectile(2924, 64, 32, 64, 0)

    override fun invoke(araxxor: Araxxor, target: Entity?) {
        target ?: return

        if (target !is Player) {
            return
        }

        if (target.isDead || target.isFinished || target.mapInstance !is AraxxorInstance) {
            return
        }

        val location = target.location.copy()

        araxxor seq 11476

        // Fire one at the player
        fireAcidBall(araxxor, location)

        // fire 8 more around the player
        repeat(8) { fireAcidBall(araxxor, getRandomLocationAroundPlayer(location)) }
    }

    private fun getRandomLocationAroundPlayer(location: Location): Location = instance.getAcidSplatterLocation(location)

    private fun fireAcidBall(araxxor: Araxxor, landing: Location) {
        val delay = World.sendProjectile(araxxor.middleLocation, landing, projectileBlob)
        schedule(delay + 1) {
            World.sendGraphics(Graphics(2923, 0, 0), landing)
            instance.spawnAcidPool(landing)
        }
    }
}
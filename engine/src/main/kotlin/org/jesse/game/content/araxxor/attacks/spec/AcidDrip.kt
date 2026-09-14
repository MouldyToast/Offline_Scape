package org.jesse.game.content.araxxor.attacks.spec

import org.jesse.game.content.araxxor.Araxxor
import org.jesse.game.content.araxxor.AraxxorInstance
import org.jesse.game.content.araxxor.attacks.AcidPool
import org.jesse.game.content.araxxor.attacks.Attack
import org.jesse.game.content.seq
import org.jesse.game.task.WorldTasksManager.schedule
import org.jesse.game.world.Projectile
import org.jesse.game.world.World
import org.jesse.game.world.entity.Entity

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-25
 */
class AcidDrip(val instance: AraxxorInstance) : Attack {

    private val projectileBlob: Projectile = Projectile(2924, 256, 128, 96, 0)

    override fun invoke(araxxor: Araxxor, target: Entity?) {
        if (target == null || target.isDead || target.isFinished) return
        araxxor seq 11478
        World.sendProjectile(araxxor, target, projectileBlob)
        val delay = araxxor.middleLocation.getDistance(target.location).toInt() - 3
        schedule(delay) {
            araxxor.instance?.spawnAcidPool(target.location) ?: return@schedule
            target.temporaryAttributes["araxxor_acid_drip"] = 6
        }
    }
}
package org.jesse.game.content.araxxor.araxytes.impl

import org.jesse.game.content.araxxor.AraxxorInstance
import org.jesse.game.content.araxxor.araxytes.Araxyte
import org.jesse.game.content.damage
import org.jesse.game.content.hit
import org.jesse.game.content.seq
import org.jesse.game.content.spotanim
import org.jesse.game.task.WorldTasksManager.schedule
import org.jesse.game.world.Projectile
import org.jesse.game.world.World
import org.jesse.game.world.entity.Entity
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Graphics
import org.jesse.game.npc.ids.*
import org.jesse.game.world.entity.npc.combatdefs.AttackType
import org.jesse.game.world.entity.player.action.combat.CombatUtilities

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-20
 */
class AcidicAraxyte(
    val instance: AraxxorInstance,
    spawnLocation: Location
): Araxyte(
    EGG_13674,
    spawnLocation
) {

    private val projectile: Projectile = Projectile(1560, 64, 128, 64, 0)

    private val projectileBlob: Projectile = Projectile(2924, 256, 128, 64, 0)

    override fun attack(target: Entity?): Int {
        if (getId() == EGG_13674) return 1
        if (target == null) return 1
        this seq 11498
        val damage = CombatUtilities.getRandomMaxHit(this, 15, AttackType.RANGED, target)
        val delay = World.sendProjectile(this, target, projectile)
        target.scheduleHit(this, this hit target damage damage, delay)
        return 6
    }

    override fun hatchEgg(target: Entity?) {
        if (target == null) return
        if (isDead || isFinished) return
        this seq 11509
        this spotanim 2927
        schedule(2) {
            setTransformationPreservingStats(ACIDIC_ARAXYTE)
            addWalkSteps(instance.centerArenaTile.x, instance.centerArenaTile.y, 6)
            schedule(3) { setTarget(target) }
        }
    }

    override fun onDeath(source: Entity?) {
        super.onDeath(source)
        val deathLocation = this.location.copy()
        instance.araxytes.remove(this)
        schedule(2) {
            repeat(9) {
                val landing = instance.getAcidSplatterLocation(deathLocation)
                val delay = World.sendProjectile(deathLocation, landing, projectileBlob)
                World.sendGraphics(Graphics(2923, delay, 0), landing)
                instance.spawnAcidPool(landing)
            }
        }
    }

}
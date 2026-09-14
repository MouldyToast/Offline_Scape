package org.jesse.game.content.theatreofblood.room.verzikvitur.first

import org.jesse.game.content.theatreofblood.room.verzikvitur.VerzikVitur
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.util.Utils
import org.jesse.game.world.Projectile
import org.jesse.game.world.World
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.Graphics
import org.jesse.game.world.entity.masks.Hit
import org.jesse.game.world.entity.masks.HitType
import it.unimi.dsi.fastutil.objects.Object2IntMap
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap

/**
 * @author Jire
 */

internal fun VerzikVitur.firstPhase() {
    ticks++
    if (ticks > 0 && ticks % (if (firstHit) 19 else 15) != 0) return

    firstHit = false
    animation = attackAnimation
    WorldTasksManager.schedule({
        val pillarToCount: Object2IntMap<SupportingPillar> = Object2IntOpenHashMap(room.supportingPillars.size)
        val validTargets = room.validTargets
        for (p in validTargets) {
            val behindPillar = room.supportingPillars.firstOrNull { it?.playerIsBehind(p) ?: false }
            if (behindPillar == null) {
                val blueProjectile = blueProjectile(112)
                val delay = World.sendProjectile(this, p, blueProjectile)

                WorldTasksManager.schedule({
                    p.graphics = attackPlayerGraphics
                    p.scheduleHit(this, magic(p, 137), -1)
                }, delay)
            } else {
                pillarToCount[behindPillar] =
                    pillarToCount.getInt(behindPillar) + 1
            }
        }

        /* And attack that pillar if one was found */
        val pillarToAttack = pillarToCount
            .filter { !it.key.isDead && !it.key.isDying && !it.key.isFinished }
            .minByOrNull { (_, v) -> v }
            ?.key
            ?: return@schedule
        val blueProjectile = blueProjectile(208)
        val delay = World.sendProjectile(this, pillarToAttack, blueProjectile)

        WorldTasksManager.schedule({
            pillarToAttack.graphics = attackPillarGraphics
            pillarToAttack.applyHit(Hit(this, Utils.random(40, 60), HitType.MAGIC))
        }, delay)
    }, 1)
}

private val attackAnimation = Animation(8109)
private val attackPillarGraphics = Graphics(1582)
private val attackPlayerGraphics = Graphics(1581)

private fun blueProjectile(endHeight: Int) = Projectile(
    1580, 504, endHeight,
    20, 29, 90, 64, 0
)
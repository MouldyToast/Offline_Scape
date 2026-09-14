package org.jesse.game.content.theatreofblood.room.verzikvitur.third.passivespells

import org.jesse.game.content.theatreofblood.room.verzikvitur.VerzikVitur
import org.jesse.game.content.theatreofblood.room.verzikvitur.third.PassiveSpell
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.util.Utils
import org.jesse.game.world.Projectile
import org.jesse.game.world.World
import org.jesse.game.world.entity.Entity
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.Graphics
import org.jesse.game.world.entity.masks.Hit
import org.jesse.game.world.entity.masks.HitType
import org.jesse.game.world.entity.player.Player

/**
 * @author Jire
 */
internal object MeteorPassiveSpell : PassiveSpell {

    override val nextSpell = NylocasPassiveSpell

    private val castAnimation = Animation(8125)
    private val finalBounceGraphics = Graphics(1600, 0, 96)
    override fun VerzikVitur.cast() {
        val first = room.validTargets.firstOrNull() ?: return
        animation = castAnimation

        fun ball(from: Entity, target: Player, bounces: Int) {
            val projectile = Projectile(1598, if (first == target) 192 else 112, 112, 0, 9, 180, 0, 5)
            val delay = World.sendProjectile(from, target, projectile)
            WorldTasksManager.schedule({
                var closest: Player? = null
                var closestDistance = Double.MAX_VALUE
                for (p in room.validTargets) {
                    if (target == p) continue
                    val distance = p.location.getDistance(target.location)
                    if (distance < closestDistance) {
                        closest = p
                        closestDistance = distance
                    }
                }

                if (closest == null || closestDistance > 2) {
                    // the final bounce
                    val hit = when(room.raid.bypassMode) {
                        true -> 74
                        false -> 30
                    }

                    target.graphics = finalBounceGraphics
                    target.applyHit(Hit(this, hit, HitType.MAGIC))
                } else {
                    target.applyHit(Hit(this, Utils.random(15), HitType.MAGIC))
                    if (bounces < 3)
                        ball(target, closest, bounces + 1)
                }
            }, delay)
        }

        WorldTasksManager.schedule({
            ball(this, first, 0)
        }, 2)
    }

}
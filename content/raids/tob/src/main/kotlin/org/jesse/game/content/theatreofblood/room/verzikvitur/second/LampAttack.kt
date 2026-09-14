package org.jesse.game.content.theatreofblood.room.verzikvitur.second

import org.jesse.game.content.theatreofblood.room.verzikvitur.VerzikVitur
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.util.Utils
import org.jesse.game.world.Projectile
import org.jesse.game.world.World
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.Graphics
import org.jesse.game.world.entity.masks.Hit
import org.jesse.game.world.entity.masks.HitType

/**
 * @author Jire
 */

internal fun VerzikVitur.lampAttack() {
    animation = lampAttackAnimation
    for (p in room.validTargets) {
        val endLoc = p.location.copy()
        val delay = World.sendProjectile(this, endLoc, projectile)
        WorldTasksManager.schedule({
            World.sendGraphics(Graphics(1584), endLoc)
            if (p.location == endLoc) {
                p.scheduleHit(this, Hit(this, Utils.random(50), HitType.RANGED), -1)
            }
        }, delay)
    }
}

val lampAttackAnimation = Animation(8114)
private val projectile = Projectile(1583, 208, 0, 30, 0, 40, 128, 0)
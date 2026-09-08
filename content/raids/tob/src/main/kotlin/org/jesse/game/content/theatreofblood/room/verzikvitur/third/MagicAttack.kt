package org.jesse.game.content.theatreofblood.room.verzikvitur.third

import org.jesse.game.content.theatreofblood.room.verzikvitur.VerzikVitur
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.Projectile
import org.jesse.game.world.World
import org.jesse.game.world.entity.masks.Animation

/**
 * @author Jire
 */

internal fun VerzikVitur.magicAttack() {
    animation = magicAttackAnimation
    WorldTasksManager.schedule({
        for (p in room.validTargets) {
            val delay = World.sendProjectile(this, p, projectile)
            delayHit(delay, p, magic(p, 32))
        }
    }, 1)
}

private val magicAttackAnimation = Animation(8124)
private val projectile = Projectile(1594, 32, 10, 0, 32, 50, 128, 0)
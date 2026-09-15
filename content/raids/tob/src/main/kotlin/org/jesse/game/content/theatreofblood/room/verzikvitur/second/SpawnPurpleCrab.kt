package org.jesse.game.content.theatreofblood.room.verzikvitur.second

import org.jesse.game.content.theatreofblood.room.verzikvitur.VerzikVitur
import org.jesse.game.content.theatreofblood.room.verzikvitur.spiders.NylocasAthanatos
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.util.Utils
import org.jesse.game.world.Projectile
import org.jesse.game.world.World
import org.jesse.game.world.entity.masks.Hit
import org.jesse.game.world.entity.masks.HitType
import org.jesse.game.world.entity.npc.NPC

/**
 * @author Jire
 */

internal fun VerzikVitur.spawnPurpleCrab() {
    val randomPlayer = Utils.random(room.validTargets) ?: return
    val projectileLocation = randomPlayer.location.copy()
    val ticks = World.sendProjectile(this, projectileLocation, projectile)
    WorldTasksManager.schedule({
        NylocasAthanatos(room, projectileLocation).apply(NPC::spawn)
        if (randomPlayer.location.matches(projectileLocation) && room.isValidTarget(randomPlayer)) {
            randomPlayer.applyHit(Hit(Utils.random(78), HitType.REGULAR))
        }
    }, ticks)
}

private val projectile = Projectile(1586, 216, 104, 20, 0, 160, 0, 0)
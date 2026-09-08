package org.jesse.game.content.theatreofblood.room.verzikvitur.second

import org.jesse.game.content.consumables.ConsumableEffects
import org.jesse.game.content.theatreofblood.room.verzikvitur.VerzikVitur
import org.jesse.game.item.ids.*
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.util.Utils
import org.jesse.game.world.Projectile
import org.jesse.game.world.World
import org.jesse.game.world.entity.Entity
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.Hit
import org.jesse.game.world.entity.masks.HitType
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.action.combat.CombatUtilities
import kotlin.math.abs


/**
 * @author Jire
 */

internal fun VerzikVitur.electricAttack() {
    val order = room.validTargets.shuffled()
    if (order.isEmpty()) return

    animation = electricAttackAnimation

    sendElectricBall(this, order[0], 1, order)
}

private fun VerzikVitur.sendElectricBall(from: Entity, to: Entity, playerIndex: Int, order: List<Player>) {
    val ticks = World.sendProjectile(from, to, projectile)
    WorldTasksManager.schedule({
        if (to is VerzikVitur)
            to.applyHit(Hit(from, Utils.random(15, 20), HitType.MAGIC))
        else {
            if (playerIndex == order.size) {
                val damage : Pair<Int, Int> = Pair(45, 48)
                var finalDamage = Utils.random(damage.first, damage.second)
                if (to is Player && to.equipment.containsAnyOf(BOOTS_OF_BRIMSTONE, INSULATED_BOOTS))
                    finalDamage /= 2
                to.applyHit(Hit(this, CombatUtilities.clampMaxHit(this, to, finalDamage), HitType.MAGIC))
                to.animation = ConsumableEffects.SHOCK_ANIM
                to.graphics = ConsumableEffects.SHOCK_GFX
            }
            else {
                to.applyHit(Hit(this, Utils.random(4, 8), HitType.MAGIC))
                val nextPlayer = order[playerIndex]
                val next = playerIndex + 1
                if (verzikInsidePath(to.x, to.y, nextPlayer.x, nextPlayer.y))
                    sendElectricBall(to, this, next, order)
                else
                    sendElectricBall(to, nextPlayer, next, order)
            }
        }
    }, ticks)
}

private fun VerzikVitur.verzikInsidePath(x0: Int, y0: Int, x1: Int, y1: Int): Boolean {
    var x0 = x0
    var y0 = y0
    val dx = abs(x1 - x0)
    val dy = abs(y1 - y0)
    val sx = if (x0 < x1) 1 else -1
    val sy = if (y0 < y1) 1 else -1
    var err = dx - dy
    var e2: Int
    while (true) {
        if (x0 >= location.x && y0 >= location.y && x0 <= location.x + size && y0 <= location.y) return true
        if (x0 == x1 && y0 == y1) break
        e2 = 2 * err
        if (e2 > -dy) {
            err -= dy
            x0 += sx
        }
        if (e2 < dx) {
            err += dx
            y0 += sy
        }
    }

    return false
}

private val electricAttackAnimation = Animation(8114)
private val projectile = Projectile(1585, 48, 0, 30, 0, 40, 128, 0)
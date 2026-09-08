package org.jesse.game.content.theatreofblood.room.verzikvitur.second

import org.jesse.game.content.theatreofblood.room.verzikvitur.VerzikVitur
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.util.Utils
import org.jesse.game.world.Projectile
import org.jesse.game.world.World
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.Graphics
import org.jesse.game.world.entity.npc.combat.Default
import org.jesse.game.world.entity.player.action.combat.magic.CombatSpell
import kotlin.math.floor

/**
 * @author Jire
 */
internal fun VerzikVitur.bloodAttack() {
    animation = bloodAttackAnimation
    val p = Utils.getRandomCollectionElement(room.validTargets) ?: return
    val ticks = World.sendProjectile(this, p, projectile)

    WorldTasksManager.schedule({
        val hit = magic(p, CombatSpell.BLOOD_BARRAGE.maxHit).onLand {
            val damage = it.damage
            if (damage > 0) {
                p.graphics = bloodAttackGraphics

                val healAmount = floor(damage / 2.0).toInt()
                if (healAmount > 0)
                    heal(healAmount)
            } else {
                p.graphics = Default.SPLASH_GRAPHICS
            }
        }
        p.scheduleHit(this, hit, -1)
    }, ticks)
}

private val bloodAttackAnimation = Animation(8114)
private val bloodAttackGraphics = Graphics(1592)
private val projectile = Projectile(1591, 52, 26, 30, 0, 40, 128, 0)
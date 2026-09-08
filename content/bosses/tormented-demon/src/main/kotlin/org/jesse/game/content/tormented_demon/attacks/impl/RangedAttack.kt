package org.jesse.game.content.tormented_demon.attacks.impl

import org.jesse.game.content.damage
import org.jesse.game.content.hit
import org.jesse.game.content.seq
import org.jesse.game.content.tormented_demon.TormentedDemon
import org.jesse.game.content.tormented_demon.attacks.Attack
import org.jesse.game.content.skills.prayer.Prayer
import org.jesse.game.world.Projectile
import org.jesse.game.world.World
import org.jesse.game.world.entity.AbstractEntity
import org.jesse.game.world.entity.Entity
import org.jesse.game.world.entity.npc.combatdefs.AttackType
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.action.combat.CombatUtilities

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-16
 */
class RangedAttack : Attack {

    private val projectile : Projectile =
        Projectile(2857, 64, 32, 96, 0)

    override fun invoke(demon: TormentedDemon, target: Entity?) {
        if (target == null) return
        demon seq demon.getRangeAttackAnimation()
        var damageMax = 31
        if (target is Player && target.prayerManager.isActive(Prayer.PROTECT_FROM_MISSILES))
            damageMax = 0
        val damage = CombatUtilities.getRandomMaxHit(demon, damageMax, AttackType.RANGED, target)
        val delay = World.sendProjectile(demon, target, projectile)
        target.scheduleHit(demon, demon hit target damage damage, delay)
    }
}
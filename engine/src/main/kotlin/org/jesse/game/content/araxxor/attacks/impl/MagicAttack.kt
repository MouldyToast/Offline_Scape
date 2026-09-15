package org.jesse.game.content.araxxor.attacks.impl

import org.jesse.game.content.araxxor.Araxxor
import org.jesse.game.content.araxxor.attacks.Attack
import org.jesse.game.content.damage
import org.jesse.game.content.hit
import org.jesse.game.content.seq
import org.jesse.game.content.skills.prayer.Prayer
import org.jesse.game.world.Projectile
import org.jesse.game.world.World
import org.jesse.game.world.entity.Entity
import org.jesse.game.world.entity.npc.combatdefs.AttackType
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.action.combat.CombatUtilities

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-20
 */
class MagicAttack: Attack {

    private val projectileBlob : Projectile = Projectile(1622, 256, 128, 64, 0)

    override fun invoke(araxxor: Araxxor, target: Entity?) {
        if (target == null) return
        araxxor seq 11479
        var maxHit = 21
        if ((target as Player).prayerManager.isActive(Prayer.PROTECT_FROM_MAGIC))
            maxHit = (maxHit * 0.20).toInt()
        val damage = CombatUtilities.getRandomMaxHit(araxxor, maxHit, AttackType.MAGIC, target)
        val delay = World.sendProjectile(araxxor, target, projectileBlob)
        val hit = araxxor hit target damage damage

        target.scheduleHit(araxxor, hit, delay)
    }
}
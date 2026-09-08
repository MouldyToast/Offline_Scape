package org.jesse.game.content.tormented_demon.attacks.impl

import org.jesse.game.content.damage
import org.jesse.game.content.hit
import org.jesse.game.content.seq
import org.jesse.game.content.tormented_demon.TormentedDemon
import org.jesse.game.content.tormented_demon.attacks.Attack
import org.jesse.game.content.skills.prayer.Prayer
import org.jesse.game.world.entity.Entity
import org.jesse.game.world.entity.masks.Graphics
import org.jesse.game.world.entity.npc.combatdefs.AttackType
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.action.combat.CombatUtilities

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-16
 */
class MeleeSlash : Attack {

    override fun invoke(demon: TormentedDemon, target: Entity?) {
        if (target == null) return
        // Animate the attack
        demon seq demon.getMeleeSlashAnimation()
        demon.graphics = Graphics(2851)
        var damageMax = 31
        if (target is Player && target.prayerManager.isActive(Prayer.PROTECT_FROM_MELEE))
            damageMax = 0
        val damage = CombatUtilities.getRandomMaxHit(demon, damageMax, AttackType.MELEE, target)
        val hit = demon hit target damage damage
        target.scheduleHit(demon, hit, 0)
    }
}
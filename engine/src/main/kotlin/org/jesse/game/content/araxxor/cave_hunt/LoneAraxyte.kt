package org.jesse.game.content.araxxor.cave_hunt

import org.jesse.game.content.araxxor.araxytes.Araxyte
import org.jesse.game.content.damage
import org.jesse.game.content.hit
import org.jesse.game.content.seq
import org.jesse.game.world.entity.Entity
import org.jesse.game.world.entity.Location
import org.jesse.game.npc.ids.*
import org.jesse.game.world.entity.npc.combatdefs.AttackType
import org.jesse.game.world.entity.player.action.combat.CombatUtilities

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-11-15
 */
class LoneAraxyte(val instance: AraxyteCaveHunt, spawnLocation: Location): Araxyte(ARAXYTE_LV_96, spawnLocation) {

    override fun attack(target: Entity?): Int {
        if (target == null) return 1
        this seq 11497
        val damage = CombatUtilities.getRandomMaxHit(this, 15, AttackType.MELEE, target)
        target.scheduleHit(this, this hit target damage damage, 0)
        return 6
    }

    override fun sendDeath() {
        instance.roomCompleted = true
        val source = mostDamagePlayerCheckIronman
        val spawnDefinitions = combatDefinitions.spawnDefinitions
        setAnimation(spawnDefinitions.deathAnimation)
        optionMask = 0
        val sound = spawnDefinitions.deathSound
        if (sound != null && source != null)
            source.sendSound(sound)
        remove()
    }
    override fun hatchEgg(target: Entity?) {}
}
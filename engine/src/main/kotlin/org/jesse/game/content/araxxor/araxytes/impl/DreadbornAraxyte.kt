package org.jesse.game.content.araxxor.araxytes.impl

import org.jesse.game.content.damage
import org.jesse.game.content.hit
import org.jesse.game.content.seq
import org.jesse.game.world.entity.Entity
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.npc.NPC
import org.jesse.game.npc.ids.*
import org.jesse.game.world.entity.npc.combat.CombatScript
import org.jesse.game.world.entity.npc.combatdefs.AttackType
import org.jesse.game.world.entity.npc.impl.slayer.superior.SuperiorNPC
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.action.combat.CombatUtilities

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-20
 */
class DreadbornAraxyte(
    spawnLocation: Location,
    owner: Player,
    root: NPC
): SuperiorNPC(owner, root, DREADBORN_ARAXYTE, spawnLocation), CombatScript {

    override fun attack(target: Entity?): Int {
        target ?: return 1
        this seq 9140
        val damage = CombatUtilities.getRandomMaxHit(this, 31, AttackType.MELEE, target)
        target.scheduleHit(this, this hit target damage damage, 0)
        return 6
    }
}
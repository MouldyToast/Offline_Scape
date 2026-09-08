package com.near_reality.game.content.araxxor.araxytes.impl

import com.near_reality.game.content.damage
import com.near_reality.game.content.hit
import com.near_reality.game.content.seq
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.npc.ids.*
import com.zenyte.game.world.entity.npc.combat.CombatScript
import com.zenyte.game.world.entity.npc.combatdefs.AttackType
import com.zenyte.game.world.entity.npc.impl.slayer.superior.SuperiorNPC
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities

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
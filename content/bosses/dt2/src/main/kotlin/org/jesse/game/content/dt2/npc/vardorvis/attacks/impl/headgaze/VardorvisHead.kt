package org.jesse.game.content.dt2.npc.vardorvis.attacks.impl.headgaze

import org.jesse.game.util.Direction
import org.jesse.game.world.entity.Entity
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.npc.NPC
import org.jesse.game.npc.ids.*
import org.jesse.game.world.entity.npc.combat.CombatScript
/**
 * @author John J. Woloszyk / Kryeus
 * @date 5.9.2024
 */
class VardorvisHead(
    tile: Location
) : NPC(
    VARDORVIS_HEAD,
    tile,
    Direction.SOUTH,
    0,
    true
), CombatScript {

    override fun attack(target: Entity): Int {
        return 5
    }

    override fun isForceAggressive(): Boolean = false
    override fun isMovementRestricted(): Boolean = true

    override fun setRespawnTask() {}
    override fun isSpawned(): Boolean = true
}
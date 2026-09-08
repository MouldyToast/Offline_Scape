package com.near_reality.game.content.araxxor.araxytes

import com.zenyte.game.item.Item
import com.zenyte.game.util.Utils
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.npc.ids.*
import com.zenyte.game.world.entity.npc.combat.CombatScript
import com.zenyte.game.world.entity.npc.combat.CombatScript.CRUSH
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-20
 */
abstract class Araxyte(
    araxyteId: Int,
    spawnLocation: Location
): NPC(
    araxyteId,
    spawnLocation,
    true
), CombatScript {

    init {
        // Eggs cannot move
        radius = 0
        // The distance before we retreat
        maxDistance = 64
    }

    val eggs = listOf(MIRRORBACK_ARAXYTE_EGG, RUPTURA_ARAXYTE_EGG, ACIDIC_ARAXYTE_EGG)

    override fun autoRetaliate(source: Entity?) {
        if (eggs.contains(id)) return
        super.autoRetaliate(source)
    }

    override fun handleIngoingHit(hit: Hit?) {
        if (hit == null) return
        if (hit.source is Player) {
            val player = hit.source as Player
            val weapon = player.equipment.getItem(EquipmentSlot.WEAPON)
            if (weapon != null) {
                if (!eggs.contains(id)) {
                    if (weapon.isOneHitKill() || hit.hitType == CRUSH)
                        hit.damage = getHitpoints()
                }
            }
        }
        super.handleIngoingHit(hit)
    }

    private fun Item.isOneHitKill(): Boolean {
        return  name.contains("noxious halberd", true) ||
                name.contains("crossbow", true) ||
                name.contains("ballista", true)
    }

    override fun processNPC() {
        if (!isUnderCombat) {
            val targets = getPossibleTargets(Entity.EntityType.PLAYER, getAggressionDistance())
            if (targets.isNotEmpty())
                setTarget(Utils.random(targets))
        }
        super.processNPC()
    }

    override fun superCheckAggressivity(): Boolean = true

    override fun isEntityClipped(): Boolean  = false

    abstract fun hatchEgg(target: Entity?)
}
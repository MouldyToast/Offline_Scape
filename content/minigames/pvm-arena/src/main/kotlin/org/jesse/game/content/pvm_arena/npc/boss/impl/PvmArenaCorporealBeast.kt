package org.jesse.game.content.pvm_arena.npc.boss.impl

import org.jesse.game.content.pvm_arena.npc.PvmArenaNpcHealthBarHudManager
import org.jesse.game.content.pvm_arena.PvmArenaManager
import org.jesse.game.content.pvm_arena.npc.PvmArenaNpc
import org.jesse.game.content.pvm_arena.npc.boss.PvmArenaBoss
import org.jesse.game.content.pvm_arena.player.revive.pvmArenaReviveState
import org.jesse.game.content.boss.corporealbeast.CorporealBeastNPC
import org.jesse.game.item.Item
import org.jesse.game.world.entity.Entity
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Hit
import org.jesse.game.world.entity.npc.NPC
import org.jesse.game.world.entity.player.Player
import org.jesse.plugins.SkipPluginScan

/**
 * Represents the Corporeal Beast npc in the PvM Arena activity.
 *
 * @author Stan van der Bend
 */
@SkipPluginScan
internal class PvmArenaCorporealBeast(override val config: PvmArenaNpc.SpawnConfig) :
    CorporealBeastNPC(config.team.area.randomSpawnLocation(), config.team.area),
    PvmArenaBoss
{

    init {
        setPvmArenaVariant(true)
    }

    override fun spawn(): NPC =
        super.spawn().also { hitpoints = maxHitpoints }

    override fun getMaxHitpoints(): Int =
        config.transformMaxHitPoints(super.getMaxHitpoints()).coerceIn(1500, 7_500)

    override fun onDeath(source: Entity?) {
        super.onDeath(source)
        PvmArenaManager.onNpcDeath(this)
    }

    override fun isPotentialTarget(entity: Entity?): Boolean =
        if (entity is Player && (!entity.pvmArenaReviveState.canBeAttacked || !config.team.containsPlayer(entity)))
            false
        else
            super.isPotentialTarget(entity)

    override fun handleOutgoingHit(target: Entity?, hit: Hit?) {
        if (target is Player && !target.pvmArenaReviveState.canBeAttacked)
            return
        super.handleOutgoingHit(target, hit)
    }

    override fun postHitProcess(hit: Hit?) {
        super.postHitProcess(hit)
        PvmArenaNpcHealthBarHudManager.update()
    }

    override fun sendDeath() {
        super.sendDeath()
        PvmArenaNpcHealthBarHudManager.removeHud(this)
    }

    override fun setRespawnTask() {
        // No respawning for PvM Arena npcs.
    }

    override fun spawnDrop(item: Item?, tile: Location?, killer: Player?) {
        killer?.inventory?.addOrDrop(item)
    }

    override fun isEntityClipped(): Boolean =
        false

    override fun isInWilderness(): Boolean =
        true // Do this so the wilderness weapon buffs are applied
}

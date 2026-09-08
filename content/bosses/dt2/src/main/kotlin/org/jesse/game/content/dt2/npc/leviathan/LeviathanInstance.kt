package org.jesse.game.content.dt2.npc.leviathan

import org.jesse.game.content.dt2.area.DT2Module
import org.jesse.game.content.dt2.npc.*
import org.jesse.game.model.music.Music
import org.jesse.game.util.Direction
import org.jesse.game.world.entity.Entity
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.npc.NPC
import org.jesse.game.npc.ids.*
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.action.combat.magic.CombatSpell
import org.jesse.game.obj.ids.*
import org.jesse.game.world.region.DynamicArea
import org.jesse.game.world.region.area.plugins.CannonRestrictionPlugin
import org.jesse.game.world.region.area.plugins.DeathPlugin
import org.jesse.game.world.region.area.plugins.GravestoneLocationPlugin
import org.jesse.game.world.region.area.plugins.PlayerCombatPlugin
import org.jesse.game.world.region.dynamicregion.AllocatedArea
import org.jesse.game.world.region.dynamicregion.MapBuilder

/**
 * @author Khaled Abdeljaber
 */
class LeviathanInstance(val allocatedArea: AllocatedArea, val awakened: Boolean) :
    DynamicArea(allocatedArea, 8291),
    CannonRestrictionPlugin,
    PlayerCombatPlugin,
    GravestoneLocationPlugin,
    DeathPlugin {

    fun constructed(leviathan: Leviathan) {
        for ((type, position, face, _) in LeviathanConstants.TAIL_DATA) {
            val direction = Direction.getDirection(position, face)
            NPC(type, getLocation(position), direction, 0).spawn()
        }

        leviathan.remove()
        leviathan.waiting = true
        leviathan.schedule(delay = 5, unsafe = true) {
            leviathan.respawn()
            leviathan.animation = LeviathanConstants.LEVIATHAN_RISE_FROM_WATER

            val players = getPlayers()
            players.forEach {
                it.sendSound(LeviathanConstants.LEVIATHAN_RISE_FROM_WATER_SOUND)
                Music.getOrNull("Colossus of the Deep")?.let { sound -> it.music.unlock(sound) }
                if (awakened) {
                    it.bossTimer.startTracking("awakened leviathan")
                } else {
                    it.bossTimer.startTracking("leviathan")
                }
                it.hpHud.open(leviathan.id, leviathan.hitpoints)
            }

            leviathan.schedule(delay = 4) {
                leviathan.waiting = false
                leviathan.startEngagement()
            }
        }

        val handholds = get(2070, 6368, 0).findObject()
        handholds?.transform(HANDHOLDS_47594)
    }

    override fun constructed() {
        val leviathan = Leviathan(
            if (awakened) THE_LEVIATHAN_12215 else THE_LEVIATHAN, get(2078, 6369, 0), this
        )
        leviathan.spawn()

        constructed(leviathan)
    }

    override fun enter(player: Player) {
    }

    override fun leave(player: Player, logout: Boolean) {
        if (player.hpHud.isOpen)
            player.hpHud.close()

        destroyRegion()
        player.packetDispatcher.resetCamera()
        player.blockIncomingHits(3)
    }

    override fun onAttack(player: Player, entity: Entity, style: String?, spell: CombatSpell?, splash: Boolean) {
        if (entity is Leviathan) {
            entity.onScheduledAttack(player, spell)
        }
    }

    override fun processCombat(player: Player, entity: Entity, style: String?): Boolean {
        if (style != "Melee") {
            return true
        }

        player.sendMessage("Your melee attacks can't reach the lure!")
        player.resetWalkSteps()
        return false
    }

    override fun name(): String {
        return "Leviathan Instance"
    }

    override fun onLoginLocation(): Location {
        return Location(2069, 6368, 0)
    }

    override fun getGravestoneLocation(): Location {
        return onLoginLocation()
    }

    companion object {
        fun construct(awakened: Boolean): LeviathanInstance {
            val area = MapBuilder.findEmptyChunk(8, 8)
            val instance = LeviathanInstance(area, awakened)
            instance.constructRegion()

            return instance
        }

        fun construct(player: Player, awakened: Boolean) {
            val constructed = construct(awakened)
            player.teleport(constructed.getLocation(2070, 6368, 0))
        }
    }


    override fun sendDeath(player: Player?, source: Entity?): Boolean {
        player ?: return true
        DT2Module.getLeviathanStatistics(awakened).globalDeathCount++
        if (awakened)
            player.deathsToAwakenedLeviathan++
        else
            player.deathsToLeviathan++
        return false
    }

    override fun isSafe(): Boolean = false

    override fun getDeathInformation(): String? = null

    override fun getRespawnLocation(): Location? = null
}
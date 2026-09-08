package org.jesse.game.content.pvm_arena

import org.jesse.game.content.pvm_arena.area.PvmArenaBlueFightArea
import org.jesse.game.content.pvm_arena.area.PvmArenaFightArea
import org.jesse.game.content.pvm_arena.area.PvmArenaRedFightArea
import org.jesse.util.capitalize
import com.runespawn.util.weakMutableSetOf
import org.jesse.game.item.ids.*
import org.jesse.game.util.Colour
import org.jesse.game.world.entity.player.Player

/**
 * Represents one of two teams that compete against each other in the PvM Arena activity.
 * Each team has a designated [area] that they fight npcs in.
 *
 * @author Stan van der Bend
 */
sealed class PvmArenaTeam(
    val area: PvmArenaFightArea,
    val iconItemId: Int,
    val color: Colour,
    val formattedName: String = color.wrap(color.name.lowercase().capitalize())
) {

    internal var killCount = 0
    internal val players = weakMutableSetOf<Player>()

    val otherTeam get() = when (this) {
        Blue -> Red
        Red -> Blue
    }

    fun addPlayer(player: Player) {
        players.add(player)
        player.sendDeveloperMessage("You are added to team $formattedName.")
    }

    fun removePlayer(player: Player) {
        players.remove(player)
        player.sendDeveloperMessage("You are removed from team $formattedName.")
    }

    fun containsPlayer(player: Player): Boolean {
        return players.contains(player)
    }

    fun clearPlayers() {
        players.clear()
    }

    fun messagePlayers(message: String) {
        players.forEach { it.sendMessage(message) }
    }

    fun reset() {
        killCount = 0
        clearPlayers()
    }

    fun isEmpty(): Boolean = players.isEmpty()

    data object Blue : PvmArenaTeam(PvmArenaBlueFightArea, BLUE_ICON, Colour.BLUE)
    data object Red : PvmArenaTeam(PvmArenaRedFightArea, RED_ICON, Colour.RED)

    companion object {
        fun findTeamContaining(player: Player) = when {
            Blue.containsPlayer(player) -> Blue
            Red.containsPlayer(player) -> Red
            else -> null
        }
        fun findTeamByLocationOf(player: Player) = when {
            Blue.area.polygon.contains(player) -> Blue
            Red.area.polygon.contains(player) -> Red
            else -> null
        }
        fun anyTeamContainsIp(ipAddress: String): Boolean =
            Blue.players.any { it.ip == ipAddress } || Red.players.any { it.ip == ipAddress }
    }
}

package org.jesse.game.content.commands

import org.jesse.game.item.ids.*
import org.jesse.game.world.entity.player.manuallyLeftHelpChat
import org.jesse.game.world.entity.player.pvpDeaths
import org.jesse.game.world.entity.player.pvpKillStreak
import org.jesse.game.world.entity.player.pvpKills
import org.jesse.game.content.skills.magic.spells.teleports.RegularTeleport
import org.jesse.game.item.Item
import org.jesse.game.world.entity.ForceTalk
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.player.GameCommands.Command
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.game.world.entity.player.dialogue.options
import org.jesse.game.world.entity.player.privilege.MemberRank
import org.jesse.game.world.entity.player.privilege.PlayerPrivilege
import org.jesse.utils.TimeUnit

object PlayerCommands {

    fun register() {

        /* Event Info */
        Command(PlayerPrivilege.PLAYER, "avatar", "Teleports the uber rank+ player to the avatar of creation") { player, _ ->
            if (player.isLocked) {
                return@Command
            }
            if (player.getMemberRank().equalToOrGreaterThan(MemberRank.ONYX) || player.isStaff) {
                val teleport = RegularTeleport(Location(1705, 2640, 0))
                player.sendMessage("You teleport to the Avatar of Creation.")
                teleport.teleport(player)
            } else {
                player.sendMessage("You must be an Uber+ to use this teleport.")
            }
        }



        Command(PlayerPrivilege.PLAYER, "kdr") { p, _ ->
            val kills = p.pvpKills
            val deaths = p.pvpDeaths
            val ratio = (if (deaths == 0) kills else kills.toDouble() / deaths.toDouble()).toString().format("%.2f")
            val killStreak = p.pvpKillStreak
            val msg = "Kills: $kills Deaths: $deaths Ratio: $ratio Streak: $killStreak"
            p.forceTalk = ForceTalk(msg)
            p.sendMessage(msg)
        }

        /* Toggles */
        Command(PlayerPrivilege.PLAYER, "togglehelp") { p, _ ->
            p.manuallyLeftHelpChat = !p.manuallyLeftHelpChat
            if(p.manuallyLeftHelpChat)
                p.sendMessage("You will no longer rejoin help chat on login.")
            else p.sendMessage("You will now automatically join help chat on login.")
        }

        /* Teleports */
        Command(PlayerPrivilege.PLAYER, "barrows", "Teleport to the Barrows minigame.") { p, _ ->
            if (p.isLocked)
                return@Command
            val teleport = RegularTeleport(Location(3564,3304,0))
            p.sendMessage("Your words manage to teleport you to Barrows.")
            teleport.teleport(p)
        }

        Command(PlayerPrivilege.PLAYER, "sc", "Teleports you to the wilderness slayer cave.") { p, _ ->
            if (p.isLocked)
                return@Command
            val teleport = RegularTeleport(Location(3291, 3757, 0))
            p.sendMessage("Your words manage to teleport you to the Wilderness Slayer Cave.")
            teleport.teleport(p, true)
        }

        // YouTuber commands stripped — all sendURL removed
    }
}

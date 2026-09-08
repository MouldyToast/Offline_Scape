package org.jesse.game.content.commands

import org.jesse.game.item.ids.*
import org.jesse.game.world.entity.player.claimedFreeMB
import org.jesse.game.world.entity.player.manuallyLeftHelpChat
import org.jesse.game.world.entity.player.pvpDeaths
import org.jesse.game.world.entity.player.pvpKillStreak
import org.jesse.game.world.entity.player.pvpKills
import org.jesse.ContentConstants
import org.jesse.game.content.skills.magic.spells.teleports.RegularTeleport
import org.jesse.game.item.Item
import org.jesse.game.referral.ReferralIPDatabase
import org.jesse.game.referral.ReferralUsageDatabase
import org.jesse.game.util.Colour
import org.jesse.game.world.entity.ForceTalk
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.player.GameCommands.Command
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.game.world.entity.player.dialogue.options
import org.jesse.game.world.entity.player.privilege.MemberRank
import org.jesse.game.world.entity.player.privilege.PlayerPrivilege
import org.jesse.utils.TimeUnit
import java.util.*

object PlayerCommands {

    val referralList = mutableListOf("vihtic", "sohan", "eggy")

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



        Command(PlayerPrivilege.PLAYER, arrayOf("tourny", "tourney"), "Teleport to tournament area.") { p, _ ->
            if (p.isLocked)
                return@Command
            val teleport = RegularTeleport(Location(3104, 3486, 0))
            p.sendMessage("You teleport to the Tournament Area.")
            teleport.teleport(p)
        }

        Command(PlayerPrivilege.SUPPORT, arrayOf("staffzone", "sz"), "Teleport to staff zone.") { p, _ ->
            if (p.isLocked)
                return@Command
            val teleport = RegularTeleport(Location(2080, 7844, 0))
            p.sendMessage("You teleport to the Staff Zone")
            teleport.teleport(p)
        }

        Command(PlayerPrivilege.PLAYER, "referral", "Enter your referral code.") { player, _ ->
            if (player.isLocked)
                return@Command
            player.sendInputString("Who referred you?") { name: String? ->
                val lcName = name?.lowercase(Locale.getDefault()) ?: return@sendInputString
                if (ReferralIPDatabase.ips.contains(player.ip) || player.claimedFreeMB) {
                    player.sendMessage("You have already claimed a referral.")
                    return@sendInputString
                }
                if (!referralList.contains(lcName)) {
                    player.sendMessage("Invalid referral name.")
                    return@sendInputString
                }
                val ip = player.ip
                if (ReferralIPDatabase.contains(ip)) {
                    player.sendMessage("A referral has already been claimed from this IP.")
                    return@sendInputString
                }
                player.claimedFreeMB = true
                player.inventory.addItem(Item(OSNR_MYSTERY_BOX, 1))
                ReferralUsageDatabase.increment(name)
                ReferralUsageDatabase.write()
                ReferralIPDatabase.addIp(ip)
                ReferralIPDatabase.write()
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

        Command(PlayerPrivilege.PLAYER, "claimfounders") { p, _ ->
            if (!p.getBooleanAttribute("claimedFounders") && Calendar.getInstance()[Calendar.YEAR] == 2024 && Calendar.getInstance()[Calendar.MONTH] == Calendar.MARCH && Calendar.getInstance()[Calendar.DAY_OF_MONTH] < 19) {
                p.sendMessage(Colour.RS_GREEN.wrap("Thank you for joining " + ContentConstants.SERVER_NAME + " on our launch weekend!"))
                p.sendMessage(Colour.RS_GREEN.wrap("The powerful Founder's Cape has been added to your inventory."))
                p.getInventory().addItem(Item(FOUNDERS_CAPE))
                p.putBooleanAttribute("claimedFounders", true)
            }
        }

        /* Teleports */
        Command(PlayerPrivilege.PLAYER, "slayer", "Teleport to slayer masters.") { p, _ ->
            if (p.isLocked)
                return@Command
            val teleport = RegularTeleport(Location(3077, 3490, 0))
            p.sendMessage("Your words manage to teleport you to the Slayer Masters.")
            teleport.teleport(p)
        }

        Command(PlayerPrivilege.PLAYER, "afk", "Teleport to the AFK Area.") { p, _ ->
            if (p.isLocked)
                return@Command
            val teleport = RegularTeleport(Location(3124, 3482,0))
            p.sendMessage("Your words manage to teleport you to the AFK Area.")
            teleport.teleport(p)
        }

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

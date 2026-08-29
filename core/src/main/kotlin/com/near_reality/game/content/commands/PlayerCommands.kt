package com.near_reality.game.content.commands

import com.near_reality.game.content.commands.DeveloperCommands.enableWildernessVault
import com.near_reality.game.item.CustomItemId
import com.near_reality.game.world.entity.player.claimedFreeMB
import com.near_reality.game.world.entity.player.manuallyLeftHelpChat
import com.near_reality.game.world.entity.player.pvpDeaths
import com.near_reality.game.world.entity.player.pvpKillStreak
import com.near_reality.game.world.entity.player.pvpKills
import com.zenyte.ContentConstants
import com.zenyte.game.content.skills.magic.spells.teleports.RegularTeleport
import com.zenyte.game.content.wildernessVault.WildernessVaultConstants
import com.zenyte.game.content.wildernessVault.WildernessVaultHandler
import com.zenyte.game.content.wildernessVault.WildernessVaultStatus
import com.zenyte.game.content.xamphur.XamphurHandler
import com.zenyte.game.item.Item
import com.zenyte.game.referral.ReferralIPDatabase
import com.zenyte.game.referral.ReferralUsageDatabase
import com.zenyte.game.util.Colour
import com.zenyte.game.world.entity.ForceTalk
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.player.GameCommands.Command
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options
import com.zenyte.game.world.entity.player.privilege.MemberRank
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege
import com.zenyte.utils.TimeUnit
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


        Command(PlayerPrivilege.PLAYER, "vault", "Displays information about the Wilderness Vault spawn.") { player, _ ->
            val wildernessVault = WildernessVaultHandler.getInstance()
            if (player.privilege.inherits(PlayerPrivilege.ADMINISTRATOR)){
                val header = if (wildernessVault.currentSpawn == null) "<str>" else ""
                val lockDuration = TimeUnit.TICKS.toMinutes(WildernessVaultConstants.LOCK_DURATION.toLong())
                val spawnDuration = TimeUnit.TICKS.toMinutes(WildernessVaultConstants.SPAWN_DELAY.toLong())
                player.dialogue {
                    options("Vault controls") {
                        "Spawn" {
                            WildernessVaultHandler.getInstance().finishEvent(false)
                            WildernessVaultHandler.getInstance().vaultStatus = WildernessVaultStatus.INACTIVE
                            WildernessVaultHandler.getInstance().cycle = WildernessVaultConstants.SPAWN_DELAY - 2
                        }
                        header + "Tele" {
                            val spawnDefinition = WildernessVaultHandler.getInstance().currentSpawn
                            if (spawnDefinition == null)
                                player.sendMessage("Not active.")
                            else
                                player.setLocation(spawnDefinition.locationPlayer())
                        }
                        "Clear" {
                            WildernessVaultHandler.getInstance().finishEvent(false)
                        }
                        "Set lock duration: $lockDuration minute" + if (lockDuration > 1) "s" else "" {
                            player.sendInputInt("Set lock duration minutes") { amount: Int ->
                                WildernessVaultConstants.LOCK_DURATION = TimeUnit.MINUTES.toTicks(amount.toLong()).toInt()
                                player.dialogueManager.start(this@dialogue)
                            }
                        }
                        "Set spawn duration: $spawnDuration minute" + if (spawnDuration > 1) "s" else "" {
                            player.sendInputInt("Set spawn duration minutes") { amount: Int ->
                                WildernessVaultConstants.SPAWN_DELAY = TimeUnit.MINUTES.toTicks(amount.toLong()).toInt()
                                player.dialogueManager.start(this@dialogue)
                            }
                        }
                        (if (enableWildernessVault) "Disable" else "Enable") {
                            enableWildernessVault = !enableWildernessVault
                            player.sendMessage("Wilderness vault is now ${if (enableWildernessVault) "enabled" else "disabled"}.")
                        }
                    }
                }
            }
            when(val status = wildernessVault.vaultStatus) {
                WildernessVaultStatus.INACTIVE -> {
                    val ticksTill = WildernessVaultConstants.SPAWN_DELAY - wildernessVault.cycle
                    if (ticksTill < 0)
                        player.sendMessage("<img=47> <col=8b0000><shad=000000>The Wilderness Vault is currently inactive.")
                    else {
                        val timeTillSpawn = wildernessVault.timeTillNextEvent(true)
                        player.sendMessage("<img=47> <col=8b0000><shad=000000>The Wilderness Vault will spawn next in approximately $timeTillSpawn.")
                    }
                }
               else ->
                   player.sendMessage("<img=47> <col=8b0000><shad=000000>The Wilderness Vault is currently ${status.name.lowercase()} at ${wildernessVault.currentSpawn?.name}.")
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
                player.inventory.addItem(Item(CustomItemId.OSNR_MYSTERY_BOX, 1))
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

        Command(PlayerPrivilege.PLAYER, "votesleft") { p, _ ->
            p.sendMessage("There are ${XamphurHandler.get().amtTillSpawn()} votes remaining until Xamphur spawns!")
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
                p.getInventory().addItem(Item(CustomItemId.FOUNDERS_CAPE))
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

package org.jesse.game.content.tournament

import com.google.common.eventbus.Subscribe
import org.jesse.game.content.shop.ShopCurrencyHandler
import org.jesse.game.content.tournament.area.TournamentFightArea
import org.jesse.game.content.tournament.area.TournamentLobbyArea
import org.jesse.game.content.tournament.area.randomFightingWaitAreaLocation
import org.jesse.game.content.tournament.npc.TournamentGuardHomeNpc
import org.jesse.game.content.tournament.preset.TournamentPreset
import org.jesse.game.content.tournament.spectating.TournamentSpectatorUpdateHook
import org.jesse.game.plugin.administratorCommand
import org.jesse.game.plugin.developerCommand
import org.jesse.game.plugin.optionsMenu
import org.jesse.game.plugin.seniorModeratorCommand
import org.jesse.game.world.entity.player.onLogin
import org.jesse.game.world.hook
import org.jesse.game.GameConstants
import org.jesse.game.model.shop.ShopCurrency
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.World
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.game.world.entity.player.dialogue.options
import org.jesse.plugins.events.ServerLaunchEvent
import kotlin.time.Duration.Companion.minutes

/**
 * Tournament module for registering global hooks.
 *
 * @author Stan van der Bend
 */
@Suppress("unused")
object TournamentModule {

    @JvmStatic
    @Subscribe
    fun onServerLaunchEvent(event: ServerLaunchEvent) {
        event.worldThread.hook(TournamentManager)
        event.worldThread.hook(TournamentSpectatorUpdateHook)
        registerCommands()
        WorldTasksManager.scheduleCreation {
            TournamentGuardHomeNpc().spawn()
        }
        TournamentLobbyArea::class.onLogin(::movePlayerAndRestoreState)
        TournamentFightArea::class.onLogin(::movePlayerAndRestoreState)
    }

    private fun movePlayerAndRestoreState(player: Player) {
        removeOverlaysAndResetState(player)
        disableSpectatorMode(player)
    }

    private fun disableSpectatorMode(player: Player) {
        player.isHidden = false
        player.packetDispatcher.freecam(false)
        player.packetDispatcher.sendClientScript(2070, 0)
        player.packetDispatcher.sendClientScript(2221, 1)
    }

    private fun registerCommands() {
        developerCommand("tt") { _, _ ->
            if (GameConstants.WORLD_PROFILE.isPublic())
                return@developerCommand
            TournamentController.Global.getTournamentIfActive()?.run {
                World.getPlayers().take(20).forEach {
                    it.teleport(lobby.randomFightingWaitAreaLocation)
                }
                WorldTasksManager.schedule(5) {
                    TournamentController.Global.start()
                }
            }
        }
        seniorModeratorCommand("starttournament") { player, _ ->
            player.optionsMenu(TournamentPreset.entries) { preset ->
                player.dialogueManager.finish()
                player.sendInputInt("Enter minutes till start of the tournament.") { duration ->
                    player.dialogueManager.finish()
                    TournamentManager.schedule(preset, duration.minutes)
                }
            }
        }
        administratorCommand("tournaments") { player, _ ->
            player.optionsMenu(TournamentManager.listActiveTournaments()) { tournament ->
                val controller = TournamentManager.getController(tournament)
                if (controller == null) {
                    player.sendMessage("No controller found for tournament.")
                    return@optionsMenu
                }
                player.dialogue {
                    options {
                        "Teleport to Lobby" {
                            tournament.lobby.teleportPlayer(player)
                        }
                        if (tournament.state is TournamentState.Scheduled) {
                            "Start" {
                                controller.start()
                            }
                        }
                    }
                }
            }
        }
        administratorCommand("tpoints") { player, args ->
            val amount = args.getOrNull(0)?.toIntOrNull()?:return@administratorCommand
            ShopCurrencyHandler.add(ShopCurrency.TOURNAMENT_POINTS, player, amount)
            player.sendMessage("You have received $amount tournament points.")
        }
    }
}

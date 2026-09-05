package com.near_reality.game.content.tournament.npc

import com.near_reality.game.content.tournament.TournamentManager
import com.near_reality.game.content.tournament.npc.TournamentGuardHomePlugin.Companion.sendInitialDialogue
import com.near_reality.game.plugin.optionsMenu
import com.zenyte.game.GameInterface
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.actions.NPCPlugin
import com.zenyte.game.world.entity.player.Player

/**
 * Represents the Tournament Guard NPC at home.
 *
 * @author Tommeh | 07/06/2019 | 00:01
 * @author Stan van der Bend
 */
@Suppress("unused")
class TournamentGuardHome : NPCPlugin() {

    override fun handle() {
        bind("Talk-to", ::startDialogue)
        bind("Trade", ::openTournamentShop)
        bind("View Tournaments", ::viewTournaments)
    }

    private fun startDialogue(player: Player, npc: NPC) =
        sendInitialDialogue(player, npc)

    private fun openTournamentShop(player: Player, @Suppress("UNUSED_PARAMETER") ignored: NPC) {
        GameInterface.TOURNAMENT_SHOP.open(player)
    }

    private fun bind(option: String, action: (Player, NPC) -> Unit) {
        bind(option, object : OptionHandler {
            override fun handle(player: Player, npc: NPC) {
                action(player, npc)
            }
            override fun click(player: Player, npc: NPC, option: NPCOption) {
                player.stopAll()
                player.setFaceEntity(npc)
                handle(player, npc)
            }
        })
    }

    private fun viewTournaments(player: Player, @Suppress("UNUSED_PARAMETER") npc: NPC) {
        player.optionsMenu(TournamentManager.listActiveTournaments()) { tournament ->
            tournament.lobby.teleportPlayer(player)
        }
    }

    override fun getNPCs(): IntArray =
        intArrayOf(NpcId.TOURNAMENT_GUARD_16012)
}

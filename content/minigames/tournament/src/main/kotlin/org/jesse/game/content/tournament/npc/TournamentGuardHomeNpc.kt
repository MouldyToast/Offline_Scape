package org.jesse.game.content.tournament.npc

import org.jesse.game.content.tournament.TournamentManager
import org.jesse.game.content.tournament.TournamentState
import org.jesse.game.util.formattedString
import org.jesse.game.util.Direction
import org.jesse.game.world.WorldThread
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.npc.NPC
import org.jesse.game.npc.ids.*

class TournamentGuardHomeNpc : NPC(TOURNAMENT_GUARD_16012, Location(3097, 3505, 0), Direction.SOUTH, 0) {

    private var lastForceChat: String? = null

    override fun processNPC() {
        if (!TournamentManager.enabled)
            return

        val activeTournaments = TournamentManager.listActiveTournaments()
        if (activeTournaments.isNotEmpty()) {
            sendRandomForceChat(
                interval = 20,
                possibleChats = buildSet {
                    if (activeTournaments.size > 1)
                        add("There are currently ${activeTournaments.size} tournaments active.")
                    activeTournaments.forEach {
                        when (val state = it.state) {
                            is TournamentState.Scheduled -> {
                                add("The ${it.preset} tournament is starting in ${state.startTimer.durationRemaining().formattedString}!")
                            }
                            is TournamentState.Ongoing -> {
                                add("Come watch round ${state.round} of the ${it.preset} tournament!")
                                add("There are ${it.participants.size} people left in the ${it.preset} tournament!.")
                            }
                            is TournamentState.Finished -> {
                                add("The ${it.preset} tournament has ended.")
                            }
                        }
                    }
                }
            )
        }
        super.processNPC()
    }

    private fun sendRandomForceChat(interval: Int, possibleChats: Set<String?>) {
        if (everyNthWorldCycle(interval)) {
            val chatPool = if (possibleChats.size > 1)
                possibleChats - lastForceChat
            else
                possibleChats
            if (chatPool.isEmpty())
                return
            val chat = chatPool.random()
            lastForceChat = chat
            setForceTalk(chat)
        }
    }

    private fun everyNthWorldCycle(n: Int) = WorldThread.getCurrentCycle() % n == 0L
}

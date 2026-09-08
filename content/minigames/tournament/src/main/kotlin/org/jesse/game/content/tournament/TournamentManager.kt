package org.jesse.game.content.tournament

import org.jesse.game.content.tournament.preset.TournamentPreset
import org.jesse.game.world.WorldEvent
import org.jesse.game.world.WorldEventListener
import kotlin.time.Duration

object TournamentManager : WorldEventListener<WorldEvent.Tick> {

    var enabled = true

    private val tournamentControllers = mutableListOf<TournamentController>(TournamentController.Global)

    fun schedule(preset: TournamentPreset, durationUntilStart: Duration) =
        tournamentControllers.add(TournamentController.Manual(preset, durationUntilStart))

    fun listActiveTournaments(): List<Tournament> =
        tournamentControllers.mapNotNull { it.getTournamentIfActive() }

    fun getController(tournament: Tournament): TournamentController? =
        tournamentControllers.find { it.isControlling(tournament) }

    override fun on(event: WorldEvent.Tick) {

        tournamentControllers.removeIf { !it.process() }
    }
}

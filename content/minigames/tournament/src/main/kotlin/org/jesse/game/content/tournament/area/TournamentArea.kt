package org.jesse.game.content.tournament.area

import org.jesse.game.content.tournament.Tournament
import org.jesse.game.content.tournament.TournamentState
import org.jesse.game.content.tournament.moveToTournamentPortal
import org.jesse.game.content.tournament.preset.TournamentPreset
import org.jesse.game.content.tournament.sendTournamentMessage
import org.jesse.game.plugin.experienceGainDisabled
import org.jesse.game.plugin.prayersDisabled
import org.jesse.game.plugin.spellsDisabled
import org.jesse.game.plugin.tradingDisabled
import org.jesse.game.util.formattedString
import org.jesse.game.content.skills.magic.spells.lunar.NPCContact
import org.jesse.game.content.skills.magic.spells.lunar.SpellbookSwap
import org.jesse.game.world.region.DynamicArea
import org.jesse.game.world.region.area.plugins.*
import org.jesse.game.world.region.dynamicregion.AllocatedArea
import kotlin.time.Duration.Companion.seconds

abstract class TournamentArea(
    preset: TournamentPreset,
    allocatedArea: AllocatedArea,
    staticChunkX: Int,
    staticChunkY: Int
) :
    DynamicArea(allocatedArea, staticChunkX, staticChunkY),
    IDropPlugin,
    ExperiencePlugin by experienceGainDisabled(),
    SpellPlugin by spellsDisabled(
        NPCContact::class,
        SpellbookSwap::class
    ),
    LogoutRestrictionPlugin,
    PrayerPlugin by prayersDisabled(*preset.disabledPrayers),
    TempPlayerStatePlugin,
    TradePlugin by tradingDisabled(message = true),
    CycleProcessPlugin
{
    private var destroying = false

    abstract val tournament: Tournament

    override fun process() = Unit

    override fun postProcess() {
        if (destroying)
            return
        val state = tournament.state as? TournamentState.Finished ?:return
        val autoDestroyRegionTimer = state.autoDestroyRegionTimer
        if (autoDestroyRegionTimer.elapsed()) {
            players.toSet().forEach { it.moveToTournamentPortal() }
            destroying = true
            destroyRegion()
        } else {
            autoDestroyRegionTimer.every(10.seconds) {
                players.forEach { it.sendTournamentMessage("You'll be moved back home in ${autoDestroyRegionTimer.durationRemaining().formattedString}.") }
            }
        }
    }

    override fun restrictMemberPerk(): Boolean = true

}

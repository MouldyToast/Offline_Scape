package org.jesse.game.content.theatreofblood

import org.jesse.game.util.invoke
import org.jesse.game.util.Colour.MAROON
import org.jesse.game.util.Colour.RED
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.attribute
import org.jesse.game.world.entity.persistentAttribute
import org.jesse.game.world.entity.player.Player

var Player.tobPreBarrierSpot by attribute<Player, Location?>("TOB_preBarrierSpot")
var Player.tobDeathSpot by attribute<Player, Location?>("TOB_deathSpot")
var Player.theatreDeathCount by attribute("TOB_deathCount", 0)
var Player.theatreDamageDealt by attribute("TOB_damageDealt", 0)
var Player.tobStats by persistentAttribute("TOB_stats", TheatreOfBloodScores())
var Player.tobStatsHard by persistentAttribute("TOB_stats_hard", TheatreOfBloodScores())
var Player.insideTob by attribute("TOB_inside", false)
var Player.tobPlayerCount by attribute("TOB_playerCount", 1)
var Player.scytheEquipped by attribute("TOB_scythe_equipped", false)
var Player.tobBypassHook by attribute("TOB_bypass_hook", false)
var Player.pendingTOBBypass by attribute("TOB_pending_bypass", false)

/**
 * These points are hidden from the player and influence the rewards the player gets.
 *
 * A TOB team can achieve a maximum of 68 points, the percentage of points the player earns,
 * influences their chance of getting a unique rewards.
 */
var Player.theatreContributionPoints
    get() = getNumericTemporaryAttribute("TOB_ContributionPoints").toInt()
    set(value) {
        val previousPoints = theatreContributionPoints
        val newPoints = value.coerceAtLeast(0)
        temporaryAttributes["TOB_ContributionPoints"] = newPoints
        sendDeveloperMessage("Your TOB contribution points are now ${MAROON(newPoints)} (was ${RED(previousPoints)})")
    }


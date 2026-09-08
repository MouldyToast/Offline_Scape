package org.jesse.game.content.scoreboard

import org.jesse.game.world.entity.persistentAttribute
import org.jesse.game.world.entity.player.Player

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-02-18
 */

var Player.deathsToAraxxor by persistentAttribute("deaths_to_araxxor", 0)

var Player.deathsToPhantomMuspah by persistentAttribute("deaths_to_phantom_muspah", 0)

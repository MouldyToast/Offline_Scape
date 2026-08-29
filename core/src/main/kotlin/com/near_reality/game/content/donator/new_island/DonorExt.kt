package com.near_reality.game.content.donator.new_island

import com.zenyte.game.world.entity.persistentAttribute
import com.zenyte.game.world.entity.player.Player

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-03-23
 */

var Player.isInWesternQuadrant by persistentAttribute("isInWesternQuadrant", false)
var Player.isInEasternQuadrant by persistentAttribute("isInEasternQuadrant", false)
var Player.isInNorthernQuadrant by persistentAttribute("isInNorthernQuadrant", false)
var Player.isInSouthernQuadrant by persistentAttribute("isInSouthernQuadrant", false)

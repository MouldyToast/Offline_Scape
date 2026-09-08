package org.jesse.plugins.events

import org.jesse.game.world.entity.player.Player
import org.jesse.plugins.Event

/**
 * @author Jire
 * @author Kris
 */
data class PostWindowStatusEvent(val player: Player) : Event
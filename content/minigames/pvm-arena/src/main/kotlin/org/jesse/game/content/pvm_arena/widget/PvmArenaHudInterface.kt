package org.jesse.game.content.pvm_arena.widget

import org.jesse.game.GameInterface
import org.jesse.game.model.ui.Interface

/**
 * Represents the PvM Arena HUD widget, displays the time left in minutes and the number of kills for each team.
 *
 * @author Stan van der Bend
 */
@Suppress("unused")
class PvmArenaHudInterface : Interface() {

    override fun attach() = Unit

    override fun build() = Unit

    override fun getInterface(): GameInterface =
        GameInterface.PVM_ARENA_HUD
}

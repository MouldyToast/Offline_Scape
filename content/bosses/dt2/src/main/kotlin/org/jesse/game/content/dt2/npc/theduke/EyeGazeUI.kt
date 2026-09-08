package org.jesse.game.content.dt2.npc.theduke

import org.jesse.game.GameInterface
import org.jesse.game.model.ui.Interface
import org.jesse.game.model.ui.InterfacePosition
import org.jesse.game.world.entity.player.Player

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-01-18
 */
class EyeGazeUI : Interface() {

    override fun attach() {
    }

    override fun build() {
    }

    override fun open(player: Player) {
        super.open(player)
    }

    override fun getInterface(): GameInterface =
        GameInterface.DUKE_GAZE
}
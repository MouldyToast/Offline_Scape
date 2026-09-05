package com.near_reality.game.content.dt2.npc.theduke

import com.zenyte.game.GameInterface
import com.zenyte.game.model.ui.Interface
import com.zenyte.game.model.ui.InterfacePosition
import com.zenyte.game.world.entity.player.Player

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
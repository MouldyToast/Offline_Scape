package org.jesse.scripts.interfaces

import org.jesse.game.model.ui.Interface.Handler
import org.jesse.game.world.entity.player.Player

/**
 * @author Jire
 */
interface InterfaceHandler : Handler {

    val script: InterfaceScript

    override fun handle(player: Player, slotId: Int, itemId: Int, option: Int) {
        val context = InterfaceHandlerContext(script, player, slotId, itemId, option)
        context.handle()
    }

    fun InterfaceHandlerContext.handle()

}
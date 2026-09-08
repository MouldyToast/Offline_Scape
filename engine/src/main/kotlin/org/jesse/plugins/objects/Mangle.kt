package org.jesse.plugins.objects

import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.obj.ids.*
import org.jesse.game.world.`object`.WorldObject
import org.jesse.plugins.interfaces.TanningInterface

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-02-22
 */
class Mangle: ObjectAction {
    override fun handleObjectAction(
        player: Player?,
        `object`: WorldObject?,
        name: String?,
        optionId: Int,
        option: String?
    ) {
        player ?: return; `object` ?: return
        TanningInterface.sendTanningInterface(player)
    }

    override fun getObjects(): Array<Any> =
        arrayOf(MANGLE)

}
package com.zenyte.plugins.objects

import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.obj.ids.*
import com.zenyte.game.world.`object`.WorldObject
import com.zenyte.plugins.interfaces.TanningInterface

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
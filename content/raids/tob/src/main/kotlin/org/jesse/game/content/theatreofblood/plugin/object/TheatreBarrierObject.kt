package org.jesse.game.content.theatreofblood.plugin.`object`

import org.jesse.game.content.theatreofblood.VerSinhazaArea
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.obj.ids.*
import org.jesse.game.world.`object`.WorldObject

/**
 * @author Tommeh
 * @author Jire
 */
class TheatreBarrierObject : ObjectAction {

    override fun handleObjectAction(
        player: Player,
        obj: WorldObject,
        name: String,
        optionId: Int,
        option: String
    ) {
        VerSinhazaArea.getArea(player)?.handleBarrier(obj, player)
    }

    override fun getObjects() = TheatreBarrierObject.objects

    private companion object {

        val objects = arrayOf(BARRIER_32755)

    }

}

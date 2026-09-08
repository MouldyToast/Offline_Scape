package org.jesse.game.content.theatreofblood.plugin.`object`

import org.jesse.game.GameInterface
import org.jesse.game.content.theatreofblood.VerSinhazaArea
import org.jesse.game.content.theatreofblood.room.ChestInfo
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.world.`object`.WorldObject

/**
 * @author Jire
 */
class OpenTheatreChestObject : ObjectAction {

    override fun handleObjectAction(
        player: Player,
        obj: WorldObject,
        name: String,
        optionId: Int,
        option: String
    ) {
        VerSinhazaArea.getArea(player) ?: return

        GameInterface.TOB_CHEST_SUPPLIES.open(player)
    }

    override fun getObjects() = OpenTheatreChestObject.objects

    private companion object {

        val objects = arrayOf(ChestInfo.OPEN_CHEST_OBJECT_ID)

    }

}
package org.jesse.game.content.theatreofblood.plugin.`object`

import org.jesse.game.content.theatreofblood.VerSinhazaArea
import org.jesse.game.content.theatreofblood.room.ChestInfo
import org.jesse.game.world.World
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.world.`object`.WorldObject

/**
 * @author Jire
 */
class ClosedTheatreChestObject : ObjectAction {

    override fun handleObjectAction(
        player: Player,
        obj: WorldObject,
        name: String,
        optionId: Int,
        option: String
    ) {
        VerSinhazaArea.getArea(player) ?: return

        World.replaceObject(obj, obj.transform(ChestInfo.OPEN_CHEST_OBJECT_ID))

        player.sendMessage("You open the chest. It glimmers with rewards inside.")
    }

    override fun getObjects() = ClosedTheatreChestObject.objects

    private companion object {

        val objects = arrayOf(ChestInfo.CLOSED_CHEST_OBJECT_ID)

    }

}
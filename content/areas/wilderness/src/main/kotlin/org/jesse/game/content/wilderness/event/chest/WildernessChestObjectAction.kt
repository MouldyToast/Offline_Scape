package org.jesse.game.content.wilderness.event.chest

import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.world.`object`.WorldObject

@Suppress("unused")
class WildernessChestObjectAction: ObjectAction {

    override fun handleObjectAction(
        player: Player?,
        `object`: WorldObject?,
        name: String?,
        optionId: Int,
        option: String?,
    ) {
        player?: return
        /*
         * This should always be the case, but making sure in case someone spawns a different object with the same id
         */
        if (`object`is WildernessChestObject)
            WildernessChestEvent.pickupChest(player)
    }

    override fun getObjects(): Array<Any> =
        arrayOf(WildernessChestObject.OBJECT_ID)
}

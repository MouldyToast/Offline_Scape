package org.jesse.game.content.theatreofblood.plugin.`object`

import org.jesse.game.content.theatreofblood.VerSinhazaArea
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.dialogue.options
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.obj.ids.*
import org.jesse.game.world.`object`.WorldObject

/**
 * @author Tommeh
 * @author Jire
 */
class FormidablePassageObject : ObjectAction {

    override fun handleObjectAction(
        player: Player,
        obj: WorldObject,
        name: String,
        optionId: Int,
        option: String
    ) {
        if (obj.id != TREASURE_ROOM && option == "Enter")
            player.options("Are you ready to proceed?") {
                "Yes." { enter(player) }
                "No - Stay here."()
            }
        else enter(player)
    }

    override fun getStrategyDistance(obj: WorldObject) =
        if (obj.id == TREASURE_ROOM) 1
        else super.getStrategyDistance(obj)

    override fun getObjects() = FormidablePassageObject.objects

    private companion object {

        val objects = arrayOf(FORMIDABLE_PASSAGE, DOOR_32751, TREASURE_ROOM)

        fun enter(player: Player) = VerSinhazaArea.getArea(player)?.handlePassage(player)

    }

}
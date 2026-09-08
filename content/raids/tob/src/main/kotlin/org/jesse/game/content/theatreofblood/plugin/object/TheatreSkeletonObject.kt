package org.jesse.game.content.theatreofblood.plugin.`object`

import org.jesse.game.content.theatreofblood.VerSinhazaArea
import org.jesse.game.item.Item
import org.jesse.game.item.ids.*
import org.jesse.game.world.World
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.dialogue.start
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.obj.ids.*
import org.jesse.game.world.`object`.WorldObject

/**
 * @author Jire
 */
class TheatreSkeletonObject : ObjectAction {

    override fun handleObjectAction(
        player: Player,
        obj: WorldObject,
        name: String,
        optionId: Int,
        option: String
    ) {
        val area = VerSinhazaArea.getArea(player) ?: return
        if (!area.completed) {
            player.sendMessage("You can't search this until the challenge is complete.")
            return
        }

        val dawnbringer = Item(DAWNBRINGER, 1)
        if (player.inventory.addItem(dawnbringer).isFailure)
            player.sendMessage("You don't have enough inventory space to take the Dawnbringer.")
        else {
            World.replaceObject(obj, obj.transform(SKELETON_32742))
            player.dialogueManager.start {
                item(dawnbringer, "You find the Dawnbringer; you feel a pulse of energy<br>burst through it.")
            }
        }
    }

    override fun getObjects() = TheatreSkeletonObject.objects

    private companion object {

        val objects = arrayOf(SKELETON_32741)

    }

}
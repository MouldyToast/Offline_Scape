package org.jesse.game.content.elven.obj

import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.world.`object`.WorldObject

/**
 * @author John J. Woloszyk / Kryeus
 * @date 8.1.2025
 */
class IorwerthDungeonEntrance: ObjectAction {
    override fun handleObjectAction(
        player: Player,
        `object`: WorldObject,
        name: String,
        optionId: Int,
        option: String
    ) {
        if(option.equals("Enter", true)) {
            player.lock(2)
            player.animation = Animation.LADDER_DOWN
            WorldTasksManager.schedule { player.setLocation(Location(3225, 12445, 0)) }
            return
        }
    }

    override fun getObjects(): Array<Any> = arrayOf(36690)
}
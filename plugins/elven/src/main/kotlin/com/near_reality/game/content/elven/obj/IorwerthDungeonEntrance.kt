package com.near_reality.game.content.elven.obj

import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.WorldObject

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
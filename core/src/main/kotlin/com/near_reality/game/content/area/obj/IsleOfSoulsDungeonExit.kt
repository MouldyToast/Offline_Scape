package com.near_reality.game.content.area.obj

import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.fadeRelocate
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.WorldObject

/**
 * @author John J. Woloszyk / Kryeus
 * @date 8.6.2025
 */
class IsleOfSoulsDungeonExit : ObjectAction {
    override fun handleObjectAction(
        player: Player,
        `object`: WorldObject,
        name: String,
        optionId: Int,
        option: String
    ) {
        player.sendFilteredMessage("You crawl through the opening.")
        player.fadeRelocate(Animation.CRAWL, Location(2310, 2919), 2)
    }

    override fun getObjects(): Array<Any> = arrayOf(40737)
}
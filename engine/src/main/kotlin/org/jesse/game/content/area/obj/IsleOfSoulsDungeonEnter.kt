package org.jesse.game.content.area.obj

import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.fadeRelocate
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.world.`object`.WorldObject

/**
 * @author John J. Woloszyk / Kryeus
 * @date 8.6.2025
 */
class IsleOfSoulsDungeonEnter : ObjectAction {
    override fun handleObjectAction(
        player: Player,
        `object`: WorldObject,
        name: String,
        optionId: Int,
        option: String
    ) {
        player.sendFilteredMessage("You crawl through the opening.")
        player.fadeRelocate(Animation.CRAWL, Location(2167, 9308, 0), 2)
    }

    override fun getObjects(): Array<Any> = arrayOf(40736)
}
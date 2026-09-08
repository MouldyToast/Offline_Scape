package org.jesse.game.content.wilderness.king_black_dragon

import org.jesse.game.content.skills.magic.spells.teleports.structures.LeverTeleport
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.obj.ids.*
import org.jesse.game.world.`object`.WorldObject

/**
 * @author Tommeh | 29 mei 2018 | 21:00:05
 * @see [Rune-Server profile](https://www.rune-server.ee/members/tommeh/)}
 */
@Suppress("unused")
class KingBlackDragonExitObject : ObjectAction {

    override fun handleObjectAction(
        player: Player,
        `object`: WorldObject,
        name: String,
        optionId: Int,
        option: String
    ) {
        LeverTeleport(
            KingBlackDragonInstance.outsideTile,
            `object`,
            "... and teleport out of the Dragon's lair.",
            null
        ).teleport(player)
    }

    override fun getObjects(): Array<Any> =
        arrayOf(LEVER_1817)
}

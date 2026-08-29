package com.near_reality.game.content.donator.new_island.loc

import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.WorldObject

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-03-26
 */
class ComingSoonDungeon: ObjectAction {
    override fun handleObjectAction(player: Player?, `object`: WorldObject?, name: String?, optionId: Int, option: String?) {
        player ?: return; `object` ?: return
        player.dialogue { plain("This dungeon is coming soon... ™") }
    }

    override fun getObjects(): Array<Any> =
        arrayOf(
            // outcrop cave
            30374,
            // Western Center
            28686,
            // Northern Hole
            2811,
            // Eastern Cave
            60471
        )
}
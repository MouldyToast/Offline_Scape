package com.near_reality.plugins.area.osnr_home.obj

import com.near_reality.game.item.CustomObjectId
import com.zenyte.game.GameInterface
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.WorldObject

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-03-14
 */
class Townboard: ObjectAction {
    override fun handleObjectAction(
        player: Player?,
        `object`: WorldObject?,
        name: String?,
        optionId: Int,
        option: String?
    ) {
        player ?: return; `object` ?: return
        if (option.equals("view", ignoreCase = true)) {
            player.addTemporaryAttribute("daily_challenge_claimable", 1)
            GameInterface.DAILY_CHALLENGES_OVERVIEW.open(player)
        }
        if (option.equals("Group Challenges", ignoreCase = true)) {
            GameInterface.CHALLENGES.open(player)
        }
        if (option.equals("Solo Challenges", ignoreCase = true)) {
            GameInterface.CHALLENGES_SOLO.open(player)
        }
    }

    override fun getObjects(): Array<Any> = arrayOf(CustomObjectId.NEAR_REALITY_TOWNBOARD)
}
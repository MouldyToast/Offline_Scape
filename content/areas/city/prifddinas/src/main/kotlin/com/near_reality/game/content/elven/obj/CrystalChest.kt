package com.near_reality.game.content.elven.obj

import com.zenyte.game.content.achievementdiary.diaries.FaladorDiary
import com.zenyte.game.item.ids.*
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.obj.ids.*
import com.zenyte.game.world.`object`.WorldObject

/**
 * Handles the interaction of a crystal chest object.
 *
 *  @author Stan van der Bend
 *
 *  @author Kris | 04/04/2019 13:17
 *  @see [Rune-Server profile](https://www.rune-server.ee/members/kris/)
 */
@Suppress("UNUSED")
class CrystalChest : ObjectAction{

    private val taverlyChest = Location(2914, 3452, 0)

    override fun handleObjectAction(player: Player, obj: WorldObject, name: String, optionId: Int, option: String) {

        if (player.inventory.containsItem(CRYSTAL_KEY)) {

            if (obj.positionHash == taverlyChest.positionHash)
                player.achievementDiaries.update(FaladorDiary.UNLOCK_CRYSTAL_CHEST)

            player.animation = Animation(832)
            player.lock(2)
            player.inventory.deleteItem(CRYSTAL_KEY, 1)

            NewCrystalChestLoot.rollTable(player, false).forEach(player.inventory::addOrDrop)
        } else
            player.sendMessage("This chest is securely locked shut.")
    }

    override fun getObjects() = arrayOf(CLOSED_CHEST_172)
}

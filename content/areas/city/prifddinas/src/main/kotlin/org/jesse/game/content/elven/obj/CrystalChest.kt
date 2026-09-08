package org.jesse.game.content.elven.obj

import org.jesse.game.content.achievementdiary.diaries.FaladorDiary
import org.jesse.game.item.ids.*
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.obj.ids.*
import org.jesse.game.world.`object`.WorldObject

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

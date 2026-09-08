package org.jesse.game.content.crystal

import org.jesse.game.item.ids.*
import org.jesse.game.util.Utils
import org.jesse.game.world.entity.player.Player

/**
 * The item id of the crystal shard used to craft and charge crystal items.
 */
const val CRYSTAL_SHARD = org.jesse.game.item.ids.CRYSTAL_SHARD

/**
 * The ratio of [CRYSTAL_SHARD] items converted to crystal item charges.
 */
const val CRYSTAL_SHARD_CHARGES_RATIO = 100


fun Player.tryFindRandom(chance: Int) {
    if (Utils.randomBoolean(chance)) {
        inventory.addOrDrop(CRYSTAL_SHARD, 1)
        sendFilteredMessage("You found a crystal shard!")
    }
}

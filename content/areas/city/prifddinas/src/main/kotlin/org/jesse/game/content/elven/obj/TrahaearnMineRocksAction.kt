package org.jesse.game.content.elven.obj

import org.jesse.game.content.crystal.TrahaearnMineRocks
import org.jesse.game.content.skills.mining.actions.Mining
import org.jesse.game.content.skills.mining.actions.Prospect
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.world.`object`.WorldObject

/**
 * Handles the mining of rocks in the Trahaearn mine.
 *
 * @author Stan van der Bend
 */
@Suppress("UNUSED")
class TrahaearnMineRocksAction : ObjectAction {

    override fun handleObjectAction(
        player: Player,
        `object`: WorldObject,
        name: String,
        optionId: Int,
        option: String,
    ) {
        val rocks = TrahaearnMineRocks.values().find { it.objectIds.contains(`object`.id) }?:return
        when(option) {
            "Mine" -> player.actionManager.action = Mining(`object`, rocks.ore)
            "Prospect" -> player.actionManager.action = Prospect(`object`, rocks.ore)
        }
    }

    override fun getObjects() = TrahaearnMineRocks.getAllRockObjectIds().toTypedArray()
}

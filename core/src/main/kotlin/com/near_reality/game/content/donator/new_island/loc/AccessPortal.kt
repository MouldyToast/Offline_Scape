package com.near_reality.game.content.donator.new_island.loc

import com.near_reality.api.service.user.storeTotalSpent
import com.near_reality.game.content.East
import com.near_reality.game.content.North
import com.near_reality.game.content.South
import com.near_reality.game.content.West
import com.zenyte.game.util.Direction
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.WorldObject

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-03-23
 */
class AccessPortal: ObjectAction {

    private fun northPortal(): WorldObject =
        World.getObjectWithId(Location(1663, 2631, 0), 41199)

    private fun southPortal(): WorldObject =
        World.getObjectWithId(Location(1663, 2617, 0), 41199)

    private fun westPortal(): WorldObject =
        World.getObjectWithId(Location(1656, 2624, 0), 41199)

    private fun eastPortal(): WorldObject =
        World.getObjectWithId(Location(1670, 2624, 0), 41199)


    override fun handleObjectAction(player: Player?, barrier: WorldObject?, name: String?, optionId: Int, option: String?) {
        player ?: return; barrier ?: return
        if (!player.canEnterBarrier(barrier)) {
            player.sendMessage("You do not have the required Donator Rank to pass this barrier.")
            return
        }
        val direction = player.getMovementDirection(barrier)
        when(direction) {
            North -> player.addWalkSteps(player.location.x, player.location.y + 1, -1, false)
            South -> player.addWalkSteps(player.location.x, player.location.y - 1, -1, false)
            East -> player.addWalkSteps(player.location.x + 1, player.location.y, -1, false)
            West -> player.addWalkSteps(player.location.x - 1, player.location.y, -1, false)
            else -> {}
        }
    }

    private fun Player.getMovementDirection(barrier: WorldObject): Direction =
        when(barrier) {
            westPortal() -> if (location.x < barrier.location.x) East else West
            eastPortal() -> if (location.x > barrier.location.x) West else East
            northPortal() -> if (location.y > barrier.location.y) South else North
            else -> if (location.y < barrier.location.y) North else South
        }

    private fun Player.canEnterBarrier(barrier: WorldObject): Boolean =
        when(barrier) {
            westPortal() -> hasMinimumAmountDonated(25)
            northPortal() -> hasMinimumAmountDonated(250)
            eastPortal() -> hasMinimumAmountDonated(1_000)
            southPortal() -> hasMinimumAmountDonated(5_000)
            else -> false
        }

    private fun Player.hasMinimumAmountDonated(amount: Int): Boolean =
        storeTotalSpent >= amount

    override fun getObjects(): Array<Any> =
        arrayOf(41199)
}
package com.near_reality.game.content.donator.new_island.loc

import com.zenyte.game.model.ui.PaneType
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.ObjectId.VIEWING_ORB_26747
import com.zenyte.game.world.`object`.WorldObject

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-03-31
 */
class ViewingOrb: ObjectAction {

    override fun handleObjectAction(player: Player?, orb: WorldObject?, name: String?, optionId: Int, option: String?) {
        player ?: return; orb ?: return
        player.packetDispatcher.freecam(true)
        player.packetDispatcher.ifOpenTop(PaneType.ORB_OF_OCULUS.id)
        player.interfaceHandler.visible.forcePut(PaneType.ORB_OF_OCULUS.id shl 16, PaneType.ORB_OF_OCULUS.id)
        player.temporaryAttributes["oculusStart"] = Location(player.location)
    }

    override fun getObjects(): Array<Any> =
        arrayOf(VIEWING_ORB_26747)
}
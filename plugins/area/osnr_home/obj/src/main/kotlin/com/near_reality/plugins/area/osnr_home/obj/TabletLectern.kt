package com.near_reality.plugins.area.osnr_home.obj

import com.zenyte.game.model.ui.InterfacePosition
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject

class TabletLectern : ObjectAction {

    private fun Player.openTabletCreator() {
        interfaceHandler.sendInterface(InterfacePosition.CENTRAL, 403)
        varManager.sendVar(4074, 5271)
        varManager.sendVar(2224, 1)
        packetDispatcher.sendClientScript(2524, -1, -1)
    }

    override fun handleObjectAction(player: Player, `object`: WorldObject, name: String, optionId: Int, option: String) {
        if (option == "Create-tablet")
            player.openTabletCreator()
    }

    override fun getObjects() = arrayOf(ObjectId.LECTERN_18245)
}
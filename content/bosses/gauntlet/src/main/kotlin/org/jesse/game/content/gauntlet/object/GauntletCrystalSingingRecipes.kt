package org.jesse.game.content.gauntlet.`object`

import org.jesse.game.model.ui.InterfacePosition
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.obj.ids.*
import org.jesse.game.world.`object`.WorldObject

@Suppress("UNUSED")
class GauntletCrystalSingingRecipes : ObjectAction {

    override fun handleObjectAction(
        player: Player,
        `object`: WorldObject,
        name: String,
        optionId: Int,
        option: String,
    ) {
        if (option == "Read") {
            player.interfaceHandler.sendInterface(
                InterfacePosition.CENTRAL, 640
            )
        }
    }

    override fun getObjects() = arrayOf(
        CRYSTAL_SINGING_RECIPES, // corrupted
        CRYSTAL_SINGING_RECIPES_36075
    )
}

package org.jesse.game.content.dt2.plugins.objects.duke

import org.jesse.game.content.dt2.area.DukeSucellusInstance
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.world.`object`.WorldObject

@Suppress("Unused")
class FermentationVatObject : ObjectAction {
    override fun handleObjectAction(
        player: Player,
        `object`: WorldObject,
        name: String,
        optionId: Int,
        option: String
    ) {
        if(optionId == 1) {
            if (player.mapInstance is DukeSucellusInstance) {
                val arena = player.mapInstance as DukeSucellusInstance
                arena.fillVat(player, `object`)
            }
            return
        }
        if(optionId == 3) {
            if (player.mapInstance is DukeSucellusInstance) {
                val arena = player.mapInstance as DukeSucellusInstance
                arena.checkVat(player, `object`)
            }
            return
        }
    }

    override fun getObjects(): Array<Any> {
        return arrayOf(47536)
    }
}
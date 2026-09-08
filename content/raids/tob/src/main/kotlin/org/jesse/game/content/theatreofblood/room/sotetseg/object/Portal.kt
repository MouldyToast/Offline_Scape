package org.jesse.game.content.theatreofblood.room.sotetseg.`object`

import org.jesse.game.content.theatreofblood.room.sotetseg.ShadowRealmRoom
import org.jesse.game.content.theatreofblood.room.sotetseg.npc.Sotetseg
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.obj.ids.*
import org.jesse.game.world.`object`.WorldObject
import org.jesse.game.world.region.RegionArea

/**
 * @author Tommeh
 * @author Jire
 */
internal class Portal : ObjectAction {

    override fun handleObjectAction(
        player: Player,
        `object`: WorldObject,
        name: String,
        optionId: Int,
        option: String
    ) {
        if (!option.equals("Enter", ignoreCase = true)) return
        val area: RegionArea = player.area as? ShadowRealmRoom ?: return
        val realm = area as ShadowRealmRoom
        if (realm.completed) return
        val sotetseg = realm.boss as Sotetseg
        realm.completed = true
        sotetseg.completeMaze(player)
    }

    override fun getObjects() = Portal.objects

    companion object {

        private val objects = arrayOf(PORTAL_33037)

    }

}
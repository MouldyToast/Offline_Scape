package org.jesse.game.content.dt2.npc.whisperer

import org.jesse.game.content.dt2.area.WhispererInstance
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.region.PolygonRegionArea
import org.jesse.game.world.region.RSPolygon

/**
 * The entry area for The Whisperer.
 *
 * Originally defined as: Bounds(2653, 6389, 2659, 6392, 0)
 */
class TheWhispererEntryArea : LassarMainUndergroundArea() {
    override fun polygons(): Array<RSPolygon> {
        return arrayOf(RSPolygon(2652, 6387, 2660, 6390))
//        return arrayOf(RSPolygon(2653, 6389, 2659, 6392))
    }

    override fun enter(player: Player) {
        player.sendDeveloperMessage("Whisperer: Entering auto-construct region")
        WhispererInstance(player).constructRegion()
    }

    override fun leave(player: Player, logout: Boolean) {

    }

    override fun name(): String {
        return "The Whisperer Entry Area"
    }
}

/**
 * The first fight area for The Whisperer.
 *
 * Originally defined as: Bounds(2643, 6353, 2670, 6384, 0)
 */
class TheWhispererFightArea : PolygonRegionArea() {
    override fun polygons(): Array<RSPolygon> {
        return arrayOf(
            RSPolygon(2643, 6353, 2670, 6384),
            RSPolygon(2387, 6353, 2414, 6384)
        )
    }

    override fun enter(player: Player) {

    }

    override fun leave(player: Player, logout: Boolean) {

    }

    override fun name(): String {
        return "The Whisperer Fight Area 1"
    }
}

/**
 * The main Lassar Underground area.
 *
 * Originally defined via:
 * Bounds.fromRegions(10340, 10596, 10852, 10339, 10338, 10595, 10594, 10851, 10850)
 *
 * Here we create one RSPolygon per region.
 */
open class LassarMainUndergroundArea
    internal constructor() : PolygonRegionArea() {

    override fun polygons(): Array<RSPolygon> {
        return arrayOf(
            RSPolygon(10340),
            RSPolygon(10596),
            RSPolygon(10852),
            RSPolygon(10339),
            RSPolygon(10338),
            RSPolygon(10595),
            RSPolygon(10594),
            RSPolygon(10851),
            RSPolygon(10850)
        )
    }

    override fun enter(player: Player) {

    }

    override fun leave(player: Player, logout: Boolean) {

    }

    override fun name(): String {
        return "Lassar Underground"
    }
}

/**
 * The Lassar Underground Shadow Realm area.
 *
 * Originally defined as: Bounds.fromRegion(9571)
 */
class LassarUndergroundShadowRealmArea : PolygonRegionArea() {
    override fun polygons(): Array<RSPolygon> {
        return arrayOf(RSPolygon(9571))
    }

    override fun enter(player: Player) {

    }

    override fun leave(player: Player, logout: Boolean) {

    }

    override fun name(): String {
        return "Lassar Underground Shadow Realm"
    }
}

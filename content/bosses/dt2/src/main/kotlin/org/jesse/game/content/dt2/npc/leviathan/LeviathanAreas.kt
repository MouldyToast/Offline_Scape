package org.jesse.game.content.dt2.npc.leviathan

import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.region.PolygonRegionArea
import org.jesse.game.world.region.RSPolygon

/**
 * @author Khaled Abdeljaber
 */


abstract class LeviathanArea : PolygonRegionArea() {
   /* override fun isWarningIntersection(): Boolean {
        return false
    }*/
}

/**
 * Leviathan Light Strike Borders Area A.
 */
class LeviathanLightStrikeBordersA : LeviathanArea() {

    override fun polygons(): Array<RSPolygon> {
        return arrayOf(
            RSPolygon(2073, 6378, 2089, 6381)
        )
    }

    override fun enter(player: Player) {
        // Logic when a player enters LeviathanArea A
    }

    override fun leave(player: Player, logout: Boolean) {
        // Logic when a player leaves LeviathanArea A
    }

    override fun name(): String {
        return "Leviathan Light Strike Borders A"
    }
}

/**
 * Leviathan Light Strike Borders LeviathanArea B.
 */
class LeviathanLightStrikeBordersB : LeviathanArea() {

    override fun polygons(): Array<RSPolygon> {
        return arrayOf(
            RSPolygon(2072, 6368, 2075, 6381)
        )
    }

    override fun enter(player: Player) {
        // Logic when a player enters LeviathanArea B
    }

    override fun leave(player: Player, logout: Boolean) {
        // Logic when a player leaves LeviathanArea B
    }

    override fun name(): String {
        return "Leviathan Light Strike Borders B"
    }
}


class LeviathanLightStrikeBordersC : LeviathanArea() {

    override fun polygons(): Array<RSPolygon> {
        return arrayOf(
            RSPolygon(2073, 6363, 2089, 6366)
        )
    }

    override fun enter(player: Player) {
        // Logic when a player enters LeviathanArea C
    }

    override fun leave(player: Player, logout: Boolean) {
        // Logic when a player leaves LeviathanArea C
    }

    override fun name(): String {
        return "Leviathan Light Strike Borders C"
    }
}

class LeviathanLightStrikeBordersD : LeviathanArea() {

    override fun polygons(): Array<RSPolygon> {
        return arrayOf(
            RSPolygon(2087, 6368, 2090, 6381)
        )
    }

    override fun enter(player: Player) {
        // Logic when a player enters LeviathanArea D
    }

    override fun leave(player: Player, logout: Boolean) {
        // Logic when a player leaves LeviathanArea D
    }

    override fun name(): String {
        return "Leviathan Light Strike Borders D"
    }
}

class LeviathanNorthBoulders : LeviathanArea() {

    override fun polygons(): Array<RSPolygon> {
        return arrayOf(
            RSPolygon(2080, 6378, 2081, 6380)
        )
    }

    override fun enter(player: Player) {
        // Logic when a player enters North Boulders
    }

    override fun leave(player: Player, logout: Boolean) {
        // Logic when a player leaves North Boulders
    }

    override fun name(): String {
        return "Leviathan North Boulders"
    }
}

class LeviathanSouthBoulders : LeviathanArea() {

    override fun polygons(): Array<RSPolygon> {
        return arrayOf(
            RSPolygon(2080, 6364, 2081, 6366)
        )
    }

    override fun enter(player: Player) {
        // Logic when a player enters South Boulders
    }

    override fun leave(player: Player, logout: Boolean) {
        // Logic when a player leaves South Boulders
    }

    override fun name(): String {
        return "Leviathan South Boulders"
    }
}

class LeviathanWestBoulders : LeviathanArea() {

    override fun polygons(): Array<RSPolygon> {
        return arrayOf(
            RSPolygon(2072, 6371, 2075, 6372)
        )
    }

    override fun enter(player: Player) {
        // Logic when a player enters West Boulders
    }

    override fun leave(player: Player, logout: Boolean) {
        // Logic when a player leaves West Boulders
    }

    override fun name(): String {
        return "Leviathan West Boulders"
    }
}

class LeviathanEastBoulders : LeviathanArea() {

    override fun polygons(): Array<RSPolygon> {
        return arrayOf(
            RSPolygon(2087, 6371, 2090, 6372)
        )
    }

    override fun enter(player: Player) {
        // Logic when a player enters East Boulders
    }

    override fun leave(player: Player, logout: Boolean) {
        // Logic when a player leaves East Boulders
    }

    override fun name(): String {
        return "Leviathan East Boulders"
    }
}

class LeviathanFightArea : LeviathanArea() {

    override fun polygons(): Array<RSPolygon> {
        return arrayOf(
            RSPolygon(
                arrayOf(
                    intArrayOf(2071, 6366),
                    intArrayOf(2071, 6363),
                    intArrayOf(2072, 6363),
                    intArrayOf(2072, 6362),
                    intArrayOf(2075, 6362),
                    intArrayOf(2075, 6361),
                    intArrayOf(2077, 6361),
                    intArrayOf(2077, 6360),
                    intArrayOf(2080, 6360),
                    intArrayOf(2080, 6361),
                    intArrayOf(2082, 6361),
                    intArrayOf(2082, 6362),
                    intArrayOf(2084, 6362),
                    intArrayOf(2084, 6363),
                    intArrayOf(2086, 6363),
                    intArrayOf(2086, 6362),
                    intArrayOf(2091, 6362),
                    intArrayOf(2091, 6363),
                    intArrayOf(2092, 6363),
                    intArrayOf(2092, 6368),
                    intArrayOf(2093, 6368),
                    intArrayOf(2093, 6369),
                    intArrayOf(2094, 6369),
                    intArrayOf(2094, 6371),
                    intArrayOf(2095, 6371),
                    intArrayOf(2095, 6374),
                    intArrayOf(2094, 6374),
                    intArrayOf(2094, 6375),
                    intArrayOf(2093, 6375),
                    intArrayOf(2093, 6378),
                    intArrayOf(2092, 6378),
                    intArrayOf(2092, 6381),
                    intArrayOf(2091, 6381),
                    intArrayOf(2091, 6383),
                    intArrayOf(2090, 6383),
                    intArrayOf(2090, 6384),
                    intArrayOf(2088, 6384),
                    intArrayOf(2088, 6385),
                    intArrayOf(2083, 6385),
                    intArrayOf(2083, 6384),
                    intArrayOf(2082, 6384),
                    intArrayOf(2082, 6383),
                    intArrayOf(2079, 6383),
                    intArrayOf(2079, 6384),
                    intArrayOf(2074, 6384),
                    intArrayOf(2074, 6383),
                    intArrayOf(2071, 6383),
                    intArrayOf(2071, 6382),
                    intArrayOf(2070, 6382),
                    intArrayOf(2070, 6374),
                    intArrayOf(2069, 6374),
                    intArrayOf(2069, 6370),
                    intArrayOf(2069, 6366)
                )
            )
        )
    }

    override fun enter(player: Player) {
        // Logic when a player enters the Leviathan fight area
    }

    override fun leave(player: Player, logout: Boolean) {
        // Logic when a player leaves the Leviathan fight area
    }

    override fun name(): String {
        return "Leviathan Fight Area"
    }
}
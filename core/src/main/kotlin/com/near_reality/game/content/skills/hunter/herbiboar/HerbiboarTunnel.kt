package com.near_reality.game.content.skills.hunter.herbiboar

import com.zenyte.game.world.entity.Location

/**
 * @author Andys1814
 * @since 1/26/2025
 */
enum class HerbiboarTunnel(val id: Int, val location: Location, val herbiboarSpawnLocation: Location) {
    TUNNEL_1(1, Location(3693, 3798), Location(3695, 3798, 0)),
    TUNNEL_2(2, Location(3702, 3808), Location(3704, 3808, 0)),
    TUNNEL_3(3, Location(3703, 3826), Location(3705, 3827, 0)),
    TUNNEL_4(4, Location(3710, 3881), Location(3708, 3879, 0)),
    TUNNEL_5(5, Location(3700, 3877), Location(3700, 3879, 0)),
    TUNNEL_6(6, Location(3715, 3840), Location(3716, 3841, 0)),
    TUNNEL_7(7, Location(3751, 3849), Location(3753, 3851, 0)),
    TUNNEL_8(8, Location(3685, 3869), Location(3685, 3867, 0)),
    TUNNEL_9(9, Location(3681, 3863), Location(3681, 3865, 0));

    companion object {

        fun forLocation(location: Location): HerbiboarTunnel? {
            return entries.toTypedArray().find {
                it.location == location
            }
        }

    }
}
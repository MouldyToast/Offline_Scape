package org.jesse.game.content.skills.hunter.herbiboar

/**
 * This isn't great, but I'm not convinced of any better way to do this and actually have it 1:1 with OSRS considering
 * all the nuances and rules etc.
 *
 * Knowing Jagex they likely also hardcoded the paths similarly.
 *
 * @author Andys1814
 * @since 1/26/2025
 */
object HerbiboarTrails {

    // DONE !
    private val FROM_LEPRECHAUN_TO_TUNNEL_1 = HerbiboarPathBuilder()
        .start(HerbiboarStart.LEPRECHAUN)
        .addTrail(HerbiboarTrail(HerbiboarSpot.G_PATCH, Herbiboar.HB_TRAIL_31354, 4, Herbiboar.HB_TRAIL_31327, 2))
        .addTrail(HerbiboarTrail(HerbiboarSpot.F_MUSHROOM, Herbiboar.HB_TRAIL_31327, 4, Herbiboar.HB_TRAIL_31342, 2))
        .addTrail(HerbiboarTrail(HerbiboarSpot.E_MUSHROOM, Herbiboar.HB_TRAIL_31342, 4, Herbiboar.HB_TRAIL_31345, 1))
        .addTrail(HerbiboarTrail(HerbiboarSpot.I_MUSHROOM, Herbiboar.HB_TRAIL_31345, 3, Herbiboar.HB_TRAIL_31351, 1)) //
        .tunnel(HerbiboarTunnel.TUNNEL_1, Herbiboar.HB_TRAIL_31351, 3)
        .build()

    // DONE !
    private val FROM_LEPRECHAUN_TO_TUNNEL_6 = HerbiboarPathBuilder()
        .start(HerbiboarStart.LEPRECHAUN)
        .addTrail(HerbiboarTrail(HerbiboarSpot.G_MUSHROOM, Herbiboar.HB_TRAIL_31354, 4, Herbiboar.HB_TRAIL_31333, 2))
        .addTrail(HerbiboarTrail(HerbiboarSpot.D_PATCH, Herbiboar.HB_TRAIL_31333, 4, Herbiboar.HB_TRAIL_31330, 1))
        .addTrail(HerbiboarTrail(HerbiboarSpot.H_SEAWEED_EAST, Herbiboar.HB_TRAIL_31330, 3, Herbiboar.HB_TRAIL_31357, 1))
        .tunnel(HerbiboarTunnel.TUNNEL_6, Herbiboar.HB_TRAIL_31357, 3)
        .build()


    // DONE !
    private val FROM_LEPRECHAUN_TO_TUNNEL_9 = HerbiboarPathBuilder()
        .start(HerbiboarStart.LEPRECHAUN)
        .addTrail(HerbiboarTrail(HerbiboarSpot.G_PATCH, Herbiboar.HB_TRAIL_31354, 4, Herbiboar.HB_TRAIL_31327, 2))
        .addTrail(HerbiboarTrail(HerbiboarSpot.F_MUSHROOM, Herbiboar.HB_TRAIL_31327, 4, Herbiboar.HB_TRAIL_31324, 1))
        .addTrail(HerbiboarTrail(HerbiboarSpot.I_PATCH, Herbiboar.HB_TRAIL_31324, 3, Herbiboar.HB_TRAIL_31345, 2))
        .addTrail(HerbiboarTrail(HerbiboarSpot.E_MUSHROOM, Herbiboar.HB_TRAIL_31345, 4, Herbiboar.HB_TRAIL_31342, 1))
        .tunnel(HerbiboarTunnel.TUNNEL_9, Herbiboar.HB_TRAIL_31342, 3)
        .build()

    // DONE !
    private val FROM_MIDDLE_TO_TUNNEL_1 = HerbiboarPathBuilder()
        .start(HerbiboarStart.MIDDLE)
        .addTrail(HerbiboarTrail(HerbiboarSpot.C_PATCH, Herbiboar.HB_TRAIL_31303, 3, Herbiboar.HB_TRAIL_31315, 1))
        .addTrail(HerbiboarTrail(HerbiboarSpot.B_SEAWEED, Herbiboar.HB_TRAIL_31315, 3, Herbiboar.HB_TRAIL_31318, 2))
        .addTrail(HerbiboarTrail(HerbiboarSpot.A_MUSHROOM, Herbiboar.HB_TRAIL_31318, 4, Herbiboar.HB_TRAIL_31321, 1))
        .addTrail(HerbiboarTrail(HerbiboarSpot.E_MUSHROOM, Herbiboar.HB_TRAIL_31321, 3, Herbiboar.HB_TRAIL_31345, 1))
        .addTrail(HerbiboarTrail(HerbiboarSpot.I_MUSHROOM, Herbiboar.HB_TRAIL_31345, 3, Herbiboar.HB_TRAIL_31351, 1))
        .tunnel(HerbiboarTunnel.TUNNEL_1, Herbiboar.HB_TRAIL_31351, 3)
        .build()

    // DONE !
    private val FROM_MIDDLE_TO_TUNNEL_4 = HerbiboarPathBuilder()
        .start(HerbiboarStart.MIDDLE)
        .addTrail(HerbiboarTrail(HerbiboarSpot.F_MUSHROOM, Herbiboar.HB_TRAIL_31309, 3, Herbiboar.HB_TRAIL_31324, 1))
        .addTrail(HerbiboarTrail(HerbiboarSpot.I_PATCH, Herbiboar.HB_TRAIL_31324, 3, Herbiboar.HB_TRAIL_31345, 2))
        .addTrail(HerbiboarTrail(HerbiboarSpot.E_PATCH, Herbiboar.HB_TRAIL_31345, 4, Herbiboar.HB_TRAIL_31321, 2))
        .addTrail(HerbiboarTrail(HerbiboarSpot.A_MUSHROOM, Herbiboar.HB_TRAIL_31321, 4, Herbiboar.HB_TRAIL_31318, 1))
        .addTrail(HerbiboarTrail(HerbiboarSpot.B_SEAWEED, Herbiboar.HB_TRAIL_31318, 3, Herbiboar.HB_TRAIL_31339, 1))
        .tunnel(HerbiboarTunnel.TUNNEL_4, Herbiboar.HB_TRAIL_31339, 3)
        .build()

    // DONE !
    private val FROM_MIDDLE_TO_TUNNEL_5 = HerbiboarPathBuilder()
        .start(HerbiboarStart.MIDDLE)
        .addTrail(HerbiboarTrail(HerbiboarSpot.C_PATCH, Herbiboar.HB_TRAIL_31303, 3, Herbiboar.HB_TRAIL_31315, 1))
        .addTrail(HerbiboarTrail(HerbiboarSpot.B_SEAWEED, Herbiboar.HB_TRAIL_31315, 3, Herbiboar.HB_TRAIL_31336, 1))
        .tunnel(HerbiboarTunnel.TUNNEL_5, Herbiboar.HB_TRAIL_31336, 3)
        .build()

    // DONE !
    private val FROM_MIDDLE_TO_TUNNEL_8 = HerbiboarPathBuilder()
        .start(HerbiboarStart.MIDDLE)
        .addTrail(HerbiboarTrail(HerbiboarSpot.A_MUSHROOM, Herbiboar.HB_TRAIL_31306, 3, Herbiboar.HB_TRAIL_31318, 1))
        .addTrail(HerbiboarTrail(HerbiboarSpot.B_SEAWEED, Herbiboar.HB_TRAIL_31318, 3, Herbiboar.HB_TRAIL_31315, 2))
        .addTrail(HerbiboarTrail(HerbiboarSpot.C_MUSHROOM, Herbiboar.HB_TRAIL_31315, 4, Herbiboar.HB_TRAIL_31303, 2))
        .tunnel(HerbiboarTunnel.TUNNEL_8, Herbiboar.HB_TRAIL_31303, 4)
        .build()

    // DONE !
    private val FROM_MIDDLE_TO_TUNNEL_9 = HerbiboarPathBuilder()
        .start(HerbiboarStart.MIDDLE)
        .addTrail(HerbiboarTrail(HerbiboarSpot.F_MUSHROOM, Herbiboar.HB_TRAIL_31309, 3, Herbiboar.HB_TRAIL_31324, 1))
        .addTrail(HerbiboarTrail(HerbiboarSpot.I_PATCH, Herbiboar.HB_TRAIL_31324, 3, Herbiboar.HB_TRAIL_31345, 2))
        .addTrail(HerbiboarTrail(HerbiboarSpot.E_MUSHROOM, Herbiboar.HB_TRAIL_31345, 4, Herbiboar.HB_TRAIL_31342, 1))
        .tunnel(HerbiboarTunnel.TUNNEL_9, Herbiboar.HB_TRAIL_31342, 4)
        .build()

    // DONE !
    private val FROM_DRIFTWOOD_TO_TUNNEL_8 = HerbiboarPathBuilder()
        .start(HerbiboarStart.DRIFTWOOD)
        .addTrail(HerbiboarTrail(HerbiboarSpot.H_SEAWEED_WEST, Herbiboar.HB_TRAIL_31360, 4, Herbiboar.HB_TRAIL_31330, 2))
        .addTrail(HerbiboarTrail(HerbiboarSpot.D_SEAWEED, Herbiboar.HB_TRAIL_31330, 4, Herbiboar.HB_TRAIL_31339, 2))
        .addTrail(HerbiboarTrail(HerbiboarSpot.B_SEAWEED, Herbiboar.HB_TRAIL_31339, 4, Herbiboar.HB_TRAIL_31318, 2))
        .addTrail(HerbiboarTrail(HerbiboarSpot.A_PATCH, Herbiboar.HB_TRAIL_31318, 4, Herbiboar.HB_TRAIL_31306, 2))
        .tunnel(HerbiboarTunnel.TUNNEL_8, Herbiboar.HB_TRAIL_31306, 4)
        .build()

    // DONE !
    private val FROM_DRIFTWOOD_TO_TUNNEL_3 = HerbiboarPathBuilder()
        .start(HerbiboarStart.DRIFTWOOD)
        .addTrail(HerbiboarTrail(HerbiboarSpot.H_SEAWEED_WEST, Herbiboar.HB_TRAIL_31360, 4, Herbiboar.HB_TRAIL_31363, 1))
        .addTrail(HerbiboarTrail(HerbiboarSpot.G_MUSHROOM, Herbiboar.HB_TRAIL_31363, 3, Herbiboar.HB_TRAIL_31354, 1))
        .tunnel(HerbiboarTunnel.TUNNEL_3, Herbiboar.HB_TRAIL_31354, 3)
        .build()

    // DONE !
    private val FROM_CAMP_ENTRANCE_TO_TUNNEL_7 = HerbiboarPathBuilder()
        .start(HerbiboarStart.CAMP_ENTRANCE)
        .addTrail(HerbiboarTrail(HerbiboarSpot.J_PATCH, Herbiboar.HB_TRAIL_31369, 3, Herbiboar.HB_TRAIL_31357, 2))
        .addTrail(HerbiboarTrail(HerbiboarSpot.H_SEAWEED_EAST, Herbiboar.HB_TRAIL_31357, 4, Herbiboar.HB_TRAIL_31360, 1))
        .tunnel(HerbiboarTunnel.TUNNEL_7, Herbiboar.HB_TRAIL_31360, 3)
        .build()

    // DONE !
    private val FROM_CAMP_ENTRANCE_TO_TUNNEL_9 = HerbiboarPathBuilder()
        .start(HerbiboarStart.CAMP_ENTRANCE)
        .addTrail(HerbiboarTrail(HerbiboarSpot.I_PATCH, Herbiboar.HB_TRAIL_31348, 4, Herbiboar.HB_TRAIL_31345, 2))
        .addTrail(HerbiboarTrail(HerbiboarSpot.E_MUSHROOM, Herbiboar.HB_TRAIL_31345, 4, Herbiboar.HB_TRAIL_31342, 1))
        .tunnel(HerbiboarTunnel.TUNNEL_9, Herbiboar.HB_TRAIL_31342, 3)
        .build()

    // DONE !
    private val FROM_GHOST_MUSHROOM_TO_TUNNEL_3 = HerbiboarPathBuilder()
        .start(HerbiboarStart.GHOST_MUSHROOM)
        .addTrail(HerbiboarTrail(HerbiboarSpot.K_PATCH, Herbiboar.HB_TRAIL_31366, 3, Herbiboar.HB_TRAIL_31348, 2))
        .addTrail(HerbiboarTrail(HerbiboarSpot.I_PATCH, Herbiboar.HB_TRAIL_31348, 4, Herbiboar.HB_TRAIL_31345, 2))
        .addTrail(HerbiboarTrail(HerbiboarSpot.E_PATCH, Herbiboar.HB_TRAIL_31345, 4, Herbiboar.HB_TRAIL_31321, 2))
        .addTrail(HerbiboarTrail(HerbiboarSpot.A_MUSHROOM, Herbiboar.HB_TRAIL_31321, 4, Herbiboar.HB_TRAIL_31318, 1))
        .addTrail(HerbiboarTrail(HerbiboarSpot.B_SEAWEED, Herbiboar.HB_TRAIL_31318, 3, Herbiboar.HB_TRAIL_31315, 2))
        .addTrail(HerbiboarTrail(HerbiboarSpot.C_PATCH, Herbiboar.HB_TRAIL_31315, 4, Herbiboar.HB_TRAIL_31312, 1))
        .addTrail(HerbiboarTrail(HerbiboarSpot.D_PATCH, Herbiboar.HB_TRAIL_31312, 3, Herbiboar.HB_TRAIL_31333, 1))
        .addTrail(HerbiboarTrail(HerbiboarSpot.G_MUSHROOM, Herbiboar.HB_TRAIL_31333, 3, Herbiboar.HB_TRAIL_31354, 1))
        .tunnel(HerbiboarTunnel.TUNNEL_3, Herbiboar.HB_TRAIL_31354, 3)
        .build()

    // DONE !
    private val FROM_GHOST_MUSHROOM_TO_TUNNEL_3_V2 = HerbiboarPathBuilder()
        .start(HerbiboarStart.GHOST_MUSHROOM)
        .addTrail(HerbiboarTrail(HerbiboarSpot.I_PATCH, Herbiboar.HB_TRAIL_31351, 4, Herbiboar.HB_TRAIL_31324, 2))
        .addTrail(HerbiboarTrail(HerbiboarSpot.F_MUSHROOM, Herbiboar.HB_TRAIL_31324, 4, Herbiboar.HB_TRAIL_31327, 1))
        .addTrail(HerbiboarTrail(HerbiboarSpot.G_MUSHROOM, Herbiboar.HB_TRAIL_31327, 3, Herbiboar.HB_TRAIL_31354, 1))
        .tunnel(HerbiboarTunnel.TUNNEL_3, Herbiboar.HB_TRAIL_31354, 3)
        .build()

    val POSSIBLE_PATHS = mapOf(
        HerbiboarStart.MIDDLE to listOf(
            FROM_MIDDLE_TO_TUNNEL_1,
            FROM_MIDDLE_TO_TUNNEL_4,
            FROM_MIDDLE_TO_TUNNEL_5,
            FROM_MIDDLE_TO_TUNNEL_8,
            FROM_MIDDLE_TO_TUNNEL_9
        ),
        HerbiboarStart.LEPRECHAUN to listOf(
            FROM_LEPRECHAUN_TO_TUNNEL_1,
            FROM_LEPRECHAUN_TO_TUNNEL_6,
            FROM_LEPRECHAUN_TO_TUNNEL_9
        ),
        HerbiboarStart.CAMP_ENTRANCE to listOf (
            FROM_CAMP_ENTRANCE_TO_TUNNEL_7,
            FROM_CAMP_ENTRANCE_TO_TUNNEL_9
        ),
        HerbiboarStart.GHOST_MUSHROOM to listOf (
            FROM_GHOST_MUSHROOM_TO_TUNNEL_3,
            FROM_GHOST_MUSHROOM_TO_TUNNEL_3_V2
        ),
        HerbiboarStart.DRIFTWOOD to listOf(
            FROM_DRIFTWOOD_TO_TUNNEL_3,
            FROM_DRIFTWOOD_TO_TUNNEL_8
        )
    )

}
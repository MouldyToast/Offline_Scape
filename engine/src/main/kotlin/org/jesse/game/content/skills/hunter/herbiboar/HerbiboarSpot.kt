package org.jesse.game.content.skills.hunter.herbiboar

import org.jesse.game.world.entity.Location

/**
 * @author Andys1814
 * @since 1/26/2025
 */
enum class HerbiboarSpot(val objectId: Int, val location: Location) {
    A_MUSHROOM(30534, Location(3670, 3889, 0)),
    A_PATCH(30546, Location(3672, 3890, 0)),
    B_SEAWEED(30537, Location(3728, 3893, 0)),
    C_MUSHROOM(30545, Location(3697, 3875, 0)),
    C_PATCH(30533, Location(3699, 3875, 0)),
    D_PATCH(30536, Location(3708, 3876, 0)),
    D_SEAWEED(30551, Location(3710, 3877, 0)),
    E_MUSHROOM(30539, Location(3668, 3865, 0)),
    E_PATCH(30549, Location(3667, 3862, 0)),
    F_MUSHROOM(30535, Location(3681, 3860, 0)),
    F_PATCH(30544, Location(3681, 3859, 0)),
    G_MUSHROOM(30541, Location(3694, 3847, 0)),
    G_PATCH(30543, Location(3698, 3847, 0)),
    H_SEAWEED_EAST(30542, Location(3715, 3851, 0)),
    H_SEAWEED_WEST(30550, Location(3713, 3850, 0)),
    I_MUSHROOM(30540, Location(3680, 3838, 0)),
    I_PATCH(30547, Location(3680, 3836, 0)),
    J_PATCH(31480, Location(3713, 3840, 0)),
    K_PATCH(31479, Location(3706, 3811, 0));

    companion object {

        val OBJECT_IDS = entries.map { it.objectId }.toTypedArray()

        fun withObjectId(id: Int) = entries.firstOrNull { it.objectId == id }

        val MUSHROOMS = setOf(A_MUSHROOM, C_MUSHROOM, E_MUSHROOM, F_MUSHROOM, G_MUSHROOM, I_MUSHROOM)

        val PATCHES = setOf(A_PATCH, C_PATCH, D_PATCH, E_PATCH, F_PATCH, G_PATCH, I_PATCH, J_PATCH, K_PATCH)

        val SEAWEED = setOf(B_SEAWEED, D_SEAWEED, H_SEAWEED_WEST, H_SEAWEED_EAST)
    }

}
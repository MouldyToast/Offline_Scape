package org.jesse.game.model.item.submenu.impl.max_cape

import org.jesse.game.model.item.submenu.ISubMenuAction
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.player.Player

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-04-10
 */
class MaxCapeTeleportAction(
    val warriorGuild: Int = 0,
    val fishingGuild: Int = 1,
    val craftingGuild: Int = 2,
    val farmingGuild: Int = 3,
    val ottoGrotto: Int = 4,
    val feldipHills: Int = 5,
    val ablackChinchopas: Int = 6,
    val hunterGuild: Int = 7,
    val home: Int = 8,
    val rimmington: Int = 9,
    val taverlyey: Int = 10,
    val pollnivneach: Int = 11,
    val hosidius: Int = 12,
    val aldarin: Int = 13,
    val rellekka: Int = 14,
    val brinhaven: Int = 15,
    val yanille: Int = 16,
    val prifddinas: Int = 17,
): ISubMenuAction {

    override fun onAction(player: Player, selectedItemIndex: Int) {
        val destination = getSelectedTeleportLocation[selectedItemIndex]
        player.teleport(Location(destination))
    }

    private val getSelectedTeleportLocation = mapOf(
        warriorGuild to Location(2880, 3546, 0),
        fishingGuild to Location(2612, 3391, 0),
        craftingGuild to Location(2933, 3291, 0),
        farmingGuild to Location(1249, 3719, 0),
        ottoGrotto to Location(2505, 3487, 0),
        feldipHills to Location(2555, 2917, 0),
        ablackChinchopas to Location(3144, 3773, 0),
        hunterGuild to Location(1568, 3047, 0),
        home to Location(3087, 3491, 0),
        rimmington to Location(2955, 3225, 0),
        taverlyey to Location(2895, 3465, 0),
        pollnivneach to Location(3342, 3005, 0),
        hosidius to Location(1743, 3517, 0),
        aldarin to Location(3087, 3491, 0), // Home for now temp
        rellekka to Location(2670, 3633, 0),
        brinhaven to Location(2758, 3178, 0),
        yanille to Location(2544, 3095, 0),
        prifddinas to Location(3239, 6074, 0)
    )
}
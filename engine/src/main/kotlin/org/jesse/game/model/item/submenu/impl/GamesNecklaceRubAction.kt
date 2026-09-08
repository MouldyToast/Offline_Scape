package org.jesse.game.model.item.submenu.impl

import org.jesse.game.model.item.submenu.ISubMenuAction
import org.jesse.game.model.item.submenu.NecklaceTeleport
import org.jesse.game.content.achievementdiary.diaries.KourendDiary
import org.jesse.game.content.skills.magic.spells.teleports.Teleport
import org.jesse.game.content.skills.magic.spells.teleports.TeleportType
import org.jesse.game.item.Item
import org.jesse.game.item.ids.*
import org.jesse.game.util.Colour
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.container.Container
import org.jesse.game.world.entity.player.container.impl.equipment.EquipmentSlot

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-04-19
 */
class GamesNecklaceRubAction(
    private val burthrope: Int = 0,
    private val barbarianOutpost: Int = 1,
    private val corporealBeast: Int = 2,
    private val tearsOfGuthix: Int = 3,
    private val wintertodtCamp: Int = 4,
): ISubMenuAction {

    override fun onAction(player: Player, selectedItemIndex: Int) {
        val destination = getSelectedTeleportLocation[selectedItemIndex]!!
        val tallyTeleport = NecklaceTeleport(destination)
        tallyTeleport.teleport(player)
    }

    private val getSelectedTeleportLocation = mapOf(
        burthrope to Location(2899,3553, 0),
        barbarianOutpost to Location(2520,3571, 0),
        corporealBeast to Location(2967,4382, 2),
        tearsOfGuthix to Location(3246,9510, 2),
        wintertodtCamp to Location(1631,3940, 0),
    )
}
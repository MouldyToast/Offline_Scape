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
 * @since 2025-04-14
 */
class GloryAmuletRubAction(
    private val edgeville: Int = 0,
    private val karamja: Int = 1,
    private val draynorVillage: Int = 2,
    private val alKharid: Int = 3,
): ISubMenuAction {

    override fun onAction(player: Player, selectedItemIndex: Int) {
        val destination = getSelectedTeleportLocation[selectedItemIndex]!!
        val tallyTeleport = NecklaceTeleport(destination, wildernessTeleportRestriction = Teleport.WILDERNESS_LEVEL_DRAGONSTONE)
        tallyTeleport.teleport(player)
    }

    private val getSelectedTeleportLocation = mapOf(
        edgeville to Location(3087, 3489, 0),
        karamja to Location(2918, 3176, 0),
        draynorVillage to Location(3105, 3251, 0),
        alKharid to Location(3293, 3163, 0),
    )
}
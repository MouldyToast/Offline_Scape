package com.near_reality.game.model.item.submenu.impl

import com.near_reality.game.model.item.submenu.ISubMenuAction
import com.near_reality.game.model.item.submenu.NecklaceTeleport
import com.zenyte.game.content.achievementdiary.diaries.KourendDiary
import com.zenyte.game.content.skills.magic.spells.teleports.Teleport
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportType
import com.zenyte.game.item.Item
import com.zenyte.game.item.ids.*
import com.zenyte.game.util.Colour
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.Container
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot

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
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
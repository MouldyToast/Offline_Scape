package com.near_reality.game.model.item.submenu.impl

import com.near_reality.game.model.item.submenu.ISubMenuAction
import com.near_reality.game.model.item.submenu.NecklaceTeleport
import com.zenyte.game.content.skills.magic.spells.teleports.Teleport
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.player.Player

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-04-19
 */
class SkillsNecklaceRubAction(
    private val fishingGuild: Int = 0,
    private val miningGuild: Int = 1,
    private val craftingGuild: Int = 2,
    private val cookingGuild: Int = 3,
    private val woodcuttingGuild: Int = 4,
    private val farmingGuild: Int = 5,
): ISubMenuAction {

    override fun onAction(player: Player, selectedItemIndex: Int) {
        val destination = getSelectedTeleportLocation[selectedItemIndex]!!
        val tallyTeleport = NecklaceTeleport(destination, wildernessTeleportRestriction = Teleport.WILDERNESS_LEVEL_DRAGONSTONE)
        tallyTeleport.teleport(player)
    }

    private val getSelectedTeleportLocation = mapOf(
        fishingGuild to Location(2611,3390, 0),
        miningGuild to Location(3049,9763, 0),
        craftingGuild to Location(2933,3295, 0),
        cookingGuild to Location(3144,3438, 0),
        woodcuttingGuild to Location(1662,3505, 0),
        farmingGuild to Location(1248,3719, 0),
    )
}
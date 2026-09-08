package com.near_reality.game.model.item.submenu

import com.zenyte.game.content.skills.magic.spells.teleports.Teleport
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportType
import com.zenyte.game.item.Item
import com.zenyte.game.world.entity.Location

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-04-19
 */

data class NecklaceTeleport(
    val destination: Location,
    val wildernessTeleportRestriction: Int = Teleport.WILDERNESS_LEVEL
): Teleport {
    override fun getType(): TeleportType = TeleportType.HOME_TELEPORT
    override fun destination(): Location = destination
    override fun getLevel(): Int = 0
    override fun getExperience(): Double = 0.0
    override fun getRandomizationDistance(): Int = 0
    override fun getRunes(): Array<Item>? = null
    override fun getWildernessLevel(): Int = wildernessTeleportRestriction
    override fun isCombatRestricted(): Boolean = Teleport.UNRESTRICTED
}
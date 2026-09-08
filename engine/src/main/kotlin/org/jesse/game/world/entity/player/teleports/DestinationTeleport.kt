package org.jesse.game.world.entity.player.teleports

import org.jesse.cache.interfaces.teleports.Destination
import org.jesse.game.content.skills.magic.spells.teleports.Teleport
import org.jesse.game.content.skills.magic.spells.teleports.TeleportType
import org.jesse.game.world.entity.Location

/**
 * @author Jire
 */
data class DestinationTeleport(
    val destination: Destination
) : Teleport {

    override fun getType() = TeleportType.NEAR_REALITY_PORTAL_TELEPORT
    override fun destination(): Location = destination.location.copy()
    override fun getLevel() = 0
    override fun getExperience() = 0.0
    override fun getRandomizationDistance() = 2
    override fun getRunes() = null
    override fun getWildernessLevel() = Teleport.WILDERNESS_LEVEL
    override fun isCombatRestricted() = false

}

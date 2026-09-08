package org.jesse.game.content.gauntlet.item.actions

import org.jesse.game.content.skills.magic.spells.teleports.Teleport
import org.jesse.game.content.skills.magic.spells.teleports.TeleportType
import org.jesse.game.item.Item
import org.jesse.game.world.entity.Location

class GauntletCrystalTeleport(private val destination: Location) : Teleport {
    override fun getType(): TeleportType = TeleportType.REGULAR_TELEPORT
    override fun destination(): Location = destination
    override fun getLevel(): Int = 0
    override fun getExperience(): Double = 0.0
    override fun getRandomizationDistance(): Int = 0
    override fun getRunes(): Array<Item> = emptyArray()
    override fun getWildernessLevel(): Int = 30
    override fun isCombatRestricted(): Boolean = Teleport.UNRESTRICTED
}
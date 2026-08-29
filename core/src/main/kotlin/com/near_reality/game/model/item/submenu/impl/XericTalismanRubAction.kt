package com.near_reality.game.model.item.submenu.impl

import com.near_reality.game.model.item.submenu.ISubMenuAction
import com.zenyte.game.content.achievementdiary.diaries.KourendDiary
import com.zenyte.game.content.skills.magic.spells.teleports.Teleport
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportType
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.util.Colour
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.Container
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot
import com.zenyte.game.world.entity.player.dialogue.dialogue

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-04-14
 */
class XericTalismanRubAction(
    val lookout: Int = 0,
    val glade: Int = 1,
    val inferno: Int = 2,
    val heart: Int = 3,
    val honour: Int = 4,
): ISubMenuAction {

    override fun onAction(player: Player, selectedItemIndex: Int) {
        val destination = getSelectedTeleportLocation[selectedItemIndex]!!
        val talisman: Item = player.inventory.getItemById(ItemId.XERICS_TALISMAN)
        val tallyTeleport = TalismanTeleport(talisman, player.inventory.container, destination)
        if (selectedItemIndex == honour) {
            if (!player.attributes.containsKey("xeric's honour")) {
                player.dialogue { plain("The talisman does not have the power to take you there.") }
                return
            }
        }
        tallyTeleport.teleport(player)
    }

    private val getSelectedTeleportLocation = mapOf(
        lookout to Location(1579, 3531, 0),
        glade to Location(1776, 3504, 0),
        inferno to Location(1503, 3815, 0),
        heart to Location(1643, 3671, 0),
        honour to Location(1254, 3559, 0),
    )
}


data class TalismanTeleport(
    val item: Item,
    val container: Container,
    val destination: Location
): Teleport {
    override fun teleport(player: Player) {
        if (item.charges <= 0) {
            player.sendMessage("Your Xeric's talisman needs to be recharged before it can be used again.")
            return
        }
        super.teleport(player)
    }

    override fun getType(): TeleportType = TeleportType.XERICS_TELEPORT
    override fun destination(): Location = destination
    override fun getLevel(): Int = 0
    override fun getExperience(): Double = 0.0
    override fun getRandomizationDistance(): Int = 0
    override fun getRunes(): Array<Item>? = null
    override fun getWildernessLevel(): Int = Teleport.WILDERNESS_LEVEL
    override fun isCombatRestricted(): Boolean = Teleport.UNRESTRICTED

    override fun onArrival(player: Player) {
        removeCharge(player, item)
        if (item.charges <= 0)
            player.sendMessage(Colour.RED.wrap("Your talisman has run out of charges."))
        if (destination.x == 1643 && destination.y == 3671)
            player.achievementDiaries.update(KourendDiary.TELEPORT_TO_XERICS_HEART)
    }

    private fun removeCharge(player: Player, item: Item) {
        val currentCharges = item.charges
        item.charges = currentCharges - 1
        if (item.charges <= 0) {
            val weaponName = item.name
            player.equipment.refresh(EquipmentSlot.WEAPON.slot)
            player.sendMessage("<col=ef1020>Your $weaponName has ran out of charges.</col>")
        }
    }
}
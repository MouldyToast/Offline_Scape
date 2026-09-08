package org.jesse.game.content.elven.item

import org.jesse.game.item.Item
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.ItemOnItemAction
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.container.RequestResult
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.game.world.entity.player.dialogue.options

/**
 * Handles the re-colouring of corrupted crystal weapons.
 *
 * @author Stan van der Bend
 */
@Suppress("UNUSED")
class CrystalOnCorruptedWeapon : ItemOnItemAction {

    override fun handleItemOnItemAction(player: Player, from: Item, to: Item, fromSlot: Int, toSlot: Int) {

        val target = CorruptedRecolourCrystal.values()
            .find { it.crystalItemId == from.id || it.crystalItemId == to.id }
            ?: return

        val source = CorruptedRecolourCrystal.values()
            .find {
                it.bladeItemId == from.id || it.bladeItemId == to.id ||
                        it.bowItemId == from.id || it.bowItemId == to.id
            } ?: return

        val crystalItem = when (target.crystalItemId) {
            from.id -> from
            to.id -> to
            else -> error("Did not find crystal item id for $target")
        }

        val oldWeaponItem = if (from.id == source.bowItemId || from.id == source.bladeItemId) from else to
        val newWeaponId = when (oldWeaponItem.id) {
            source.bladeItemId -> target.bladeItemId
            source.bowItemId -> target.bowItemId
            else -> error("Invalid crystal weapon item id for $source")
        }
        val weaponName = oldWeaponItem.name
        player.dialogue {
            options("Are you sure you wish to recolour your $weaponName?") {
                "Yes" {
                    val slot = if (oldWeaponItem == from) fromSlot else toSlot
                    val itemAtSlot = player.inventory.getItem(slot)
                    require(itemAtSlot == oldWeaponItem) {
                        "Expected $oldWeaponItem at slot[$slot] but got $itemAtSlot"
                    }
                    if (player.inventory.deleteItem(crystalItem).result == RequestResult.SUCCESS) {
                        player.inventory.replaceItem(newWeaponId, 1, slot)
                        player.dialogue {
                            doubleItem(
                                newWeaponId,
                                target.crystalItemId,
                                "You use the crystal to colour your $weaponName."
                            )
                        }
                    }
                }
                "No" {}
            }
        }
    }

    override fun getItems() = CorruptedRecolourCrystal.values()
        .flatMap { listOf(it.crystalItemId, it.bladeItemId, it.bowItemId) }
        .toSet()
        .toIntArray()

    private enum class CorruptedRecolourCrystal(
        val crystalItemId: Int,
        val bladeItemId: Int,
        val bowItemId: Int,
    ) {
        ITHELL(CRYSTAL_OF_ITHELL, BLADE_OF_SAELDOR_C_25870, BOW_OF_FAERDHINEN_C_25884),
        IORWERTH(CRYSTAL_OF_IORWERTH, BLADE_OF_SAELDOR_C_25872, BOW_OF_FAERDHINEN_C_25886),
        TRAHAEARN(CRYSTAL_OF_TRAHAEARN, BLADE_OF_SAELDOR_C_25874, BOW_OF_FAERDHINEN_C_25888),
        CADARN(CRYSTAL_OF_CADARN, BLADE_OF_SAELDOR_C_25876, BOW_OF_FAERDHINEN_C_25890),
        CRWYS(CRYSTAL_OF_CRWYS, BLADE_OF_SAELDOR_C_25878, BOW_OF_FAERDHINEN_C_25892),
        MEILYR(CRYSTAL_OF_MEILYR, BLADE_OF_SAELDOR_C_25880, BOW_OF_FAERDHINEN_C_25894),
        HEFIN(CRYSTAL_OF_HEFIN, BLADE_OF_SAELDOR_C, BOW_OF_FAERDHINEN_C),
        AMLODD(CRYSTAL_OF_AMLODD, BLADE_OF_SAELDOR_C_25882, BOW_OF_FAERDHINEN_C_25896),
    }

}


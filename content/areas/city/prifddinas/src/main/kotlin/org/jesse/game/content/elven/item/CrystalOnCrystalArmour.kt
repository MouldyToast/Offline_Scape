package org.jesse.game.content.elven.item

import org.jesse.game.item.Item
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.ItemOnItemAction
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.container.RequestResult
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.game.world.entity.player.dialogue.options

/**
 * @author Glabay | Glabay-Studios
 * @project Near-Reality
 * @social Discord: Glabay
 * @since 2024-09-17
 */
@Suppress("UNUSED")
class CrystalOnCrystalArmour : ItemOnItemAction {

    override fun handleItemOnItemAction(player: Player, from: Item, to: Item, fromSlot: Int, toSlot: Int) {

        val target = CorruptedRecolourCrystal.entries
            .find { it.crystalItemId == from.id || it.crystalItemId == to.id }
            ?: return

        val source = CorruptedRecolourCrystal.entries
            .find {
                it.helmId == from.id || it.helmId == to.id ||
                        it.chestId == from.id || it.chestId == to.id ||
                        it.legId == from.id || it.legId == to.id
            } ?: return

        val crystalItem = when (target.crystalItemId) {
            from.id -> from
            to.id -> to
            else -> error("Did not find crystal item id for $target")
        }

        val oldArmourItem = if (from.id == source.helmId || from.id == source.chestId || from.id == source.legId) from else to
        val newArmourItem = when (oldArmourItem.id) {
            source.helmId -> target.helmId
            source.chestId -> target.chestId
            source.legId -> target.legId
            else -> error("Invalid crystal weapon item id for $source")
        }
        val armourName = oldArmourItem.name
        val crystalItemId = target.crystalItemId
        player.dialogue {
            options("Are you sure you wish to recolour your $armourName?") {
                "Yes" {
                    val slot = if (oldArmourItem == from) fromSlot else toSlot
                    val itemAtSlot = player.inventory.getItem(slot)
                    require(itemAtSlot == oldArmourItem) {
                        "Expected $oldArmourItem at slot[$slot] but got $itemAtSlot"
                    }
                    if (player.inventory.deleteItem(crystalItem).result == RequestResult.SUCCESS) {
                        player.inventory.replaceItem(newArmourItem, 1, slot)
                        player.dialogue {
                            doubleItem(
                                newArmourItem,
                                crystalItemId,
                                "You use the crystal to colour your $armourName."
                            )
                        }
                    }
                }
                "No" {}
            }
        }
    }

    override fun getItems() = CorruptedRecolourCrystal.entries
        .flatMap { listOf(it.crystalItemId, it.helmId, it.chestId, it.legId) }
        .toSet()
        .toIntArray()

    private enum class CorruptedRecolourCrystal(
        val crystalItemId: Int,
        val helmId: Int,
        val chestId: Int,
        val legId: Int,
    ) {
        ITHELL(     CRYSTAL_OF_ITHELL,       CRYSTAL_HELM_27717,  CRYSTAL_BODY_27709,  CRYSTAL_LEGS_27713),
        IORWERTH(   CRYSTAL_OF_IORWERTH,     CRYSTAL_HELM_27729,  CRYSTAL_BODY_27721,  CRYSTAL_LEGS_27725),
        TRAHAEARN(  CRYSTAL_OF_TRAHAEARN,    CRYSTAL_HELM_27741,  CRYSTAL_BODY_27733,  CRYSTAL_LEGS_27737),
        CADARN(     CRYSTAL_OF_CADARN,       CRYSTAL_HELM_27753,  CRYSTAL_BODY_27745,  CRYSTAL_LEGS_27749),
        CRWYS(      CRYSTAL_OF_CRWYS,        CRYSTAL_HELM_27765,  CRYSTAL_BODY_27757,  CRYSTAL_LEGS_27761),
        MEILYR(     CRYSTAL_OF_MEILYR,       CRYSTAL_HELM,        CRYSTAL_BODY,        CRYSTAL_LEGS),
        HEFIN(      CRYSTAL_OF_HEFIN,        CRYSTAL_HELM_27705,  CRYSTAL_BODY_27697,  CRYSTAL_LEGS_27701),
        AMLODD(     CRYSTAL_OF_AMLODD,       CRYSTAL_HELM_27777,  CRYSTAL_BODY_27769,  CRYSTAL_LEGS_27773),
    }

}
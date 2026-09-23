package org.jesse.game.content.wilderness.revenant.item

import com.google.common.collect.HashBiMap
import org.jesse.game.item.Item
import org.jesse.game.item.ids.*


/**
 * Handles the item container actions of the accursed sceptre.
 *
 * @author Stan van der Bend
 */
@Suppress("unused")
class AccursedSceptrePlugin : AbstractRevenantWeaponPlugin(
    chargedToUnchargedIdMap = HashBiMap.create<Int, Int>().apply {
        put(ACCURSED_SCEPTRE, ACCURSED_SCEPTRE_U)
        put(ACCURSED_SCEPTRE_A, ACCURSED_SCEPTRE_AU)
    },
    dismantleIngredientsByUnchargedIdMap = mapOf(
        ACCURSED_SCEPTRE_U to arrayOf(
            Item(SKULL_OF_VETION),
            Item(THAMMARONS_SCEPTRE_U)
        ),
        ACCURSED_SCEPTRE_AU to arrayOf(
            Item(SKULL_OF_VETION),
            Item(THAMMARONS_SCEPTRE_AU)
        )
    )
){

    override fun handle() {
        super.handle()
        bind("Swap") { player, item, container, slotId ->
            val otherId = when(item.id) {
                ACCURSED_SCEPTRE -> ACCURSED_SCEPTRE_A
                ACCURSED_SCEPTRE_A -> ACCURSED_SCEPTRE
                ACCURSED_SCEPTRE_U -> ACCURSED_SCEPTRE_AU
                ACCURSED_SCEPTRE_AU -> ACCURSED_SCEPTRE_U
                else -> null
            }
            if (otherId != null) {
                if (player.inventory.container == container) {
                    container[slotId].id = otherId
                    player.inventory.refresh(slotId)
                } else {
                    player.equipment.set(slotId, Item(otherId).apply { charges = item.charges })
                    player.equipment.refresh(slotId)
                }
            }
        }
    }
}

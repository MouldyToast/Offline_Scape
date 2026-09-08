package org.jesse.game.content.wilderness.revenant.item

import com.google.common.collect.HashBiMap
import org.jesse.game.item.Item
import org.jesse.game.item.ids.*

/**
 * Handles the item container actions of the Viggora's chainmace.
 *
 * @author Stan van der Bend
 */
@Suppress("unused")
class ViggorasChainMacePlugin : AbstractRevenantWeaponPlugin(
    chargedToUnchargedIdMap =  HashBiMap.create<Int, Int>().apply {
        put(VIGGORAS_CHAINMACE, VIGGORAS_CHAINMACE_U)
    },
    dismantleIngredientsByUnchargedIdMap = mapOf(
        VIGGORAS_CHAINMACE_U to arrayOf(Item(REVENANT_ETHER, 7_500))
    )
)

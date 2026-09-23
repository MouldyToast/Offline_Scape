package org.jesse.game.content.wilderness.revenant.item

import com.google.common.collect.HashBiMap
import org.jesse.game.item.Item
import org.jesse.game.item.ids.*

/**
 * Handles the item container actions of the Ursine chainmace.
 *
 * @author Stan van der Bend
 */
@Suppress("unused")
class UrsineChainMacePlugin : AbstractRevenantWeaponPlugin(
    chargedToUnchargedIdMap =  HashBiMap.create<Int, Int>().apply {
        put(URSINE_CHAINMACE, URSINE_CHAINMACE_U)
    },
    dismantleIngredientsByUnchargedIdMap = mapOf(
        URSINE_CHAINMACE_U to arrayOf(
            Item(CLAWS_OF_CALLISTO),
            Item(VIGGORAS_CHAINMACE_U)
        )
    )
)

package com.near_reality.game.content.wilderness.revenant.item

import com.google.common.collect.HashBiMap
import com.zenyte.game.item.Item
import com.zenyte.game.item.ids.*

/**
 * Handles the item container actions of the Viggora's chainmace.
 *
 * @author Stan van der Bend
 */
@Suppress("unused")
class WebWeaverBowPlugin : AbstractRevenantWeaponPlugin(
    chargedToUnchargedIdMap =  HashBiMap.create<Int, Int>().apply {
        put(WEBWEAVER_BOW_27655, WEBWEAVER_BOW_U_27652)
    },
    dismantleIngredientsByUnchargedIdMap = mapOf(
        WEBWEAVER_BOW_U_27652 to arrayOf(
            Item(FANGS_OF_VENENATIS),
            Item(CRAWS_BOW_U)
        )
    )
)

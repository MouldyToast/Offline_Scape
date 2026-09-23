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
class WebWeaverBowPlugin : AbstractRevenantWeaponPlugin(
    chargedToUnchargedIdMap =  HashBiMap.create<Int, Int>().apply {
        put(WEBWEAVER_BOW, WEBWEAVER_BOW_U)
    },
    dismantleIngredientsByUnchargedIdMap = mapOf(
        WEBWEAVER_BOW_U to arrayOf(
            Item(FANGS_OF_VENENATIS),
            Item(CRAWS_BOW_U)
        )
    )
)

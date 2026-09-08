package org.jesse.game.content.wilderness.revenant.item

import com.google.common.collect.HashBiMap
import org.jesse.game.item.Item
import org.jesse.game.item.ids.*

/**
 * Handles the item container actions of the craw's bow.
 *
 * @author Stan van der Bend
 */
@Suppress("unused")
class CrawsBowPlugin : AbstractRevenantWeaponPlugin(
    chargedToUnchargedIdMap = HashBiMap.create<Int, Int>().apply {
        put(CRAWS_BOW, CRAWS_BOW_U)
    },
    dismantleIngredientsByUnchargedIdMap = mapOf(
        CRAWS_BOW_U to arrayOf(Item(REVENANT_ETHER, 7_500))
    )
)

package com.near_reality.game.content.storebundle

import com.near_reality.game.item.CustomItemId

/**
 * @author John J. Woloszyk / Kryeus
 * @date 8.6.2025
 */
enum class BundleChest(
    val itemId: Int
){
    BUNDLE_1(CustomItemId.DONATOR_PROMO_BUNDLE_1),
    BUNDLE_2(CustomItemId.DONATOR_PROMO_BUNDLE_2),
    BUNDLE_3(CustomItemId.DONATOR_PROMO_BUNDLE_3),
    BUNDLE_4(CustomItemId.DONATOR_PROMO_BUNDLE_4),
    BUNDLE_5(CustomItemId.DONATOR_PROMO_BUNDLE_5),
    BUNDLE_6(CustomItemId.DONATOR_PROMO_BUNDLE_6),
    BUNDLE_7(CustomItemId.DONATOR_PROMO_BUNDLE_7),
    BUNDLE_8(CustomItemId.DONATOR_PROMO_BUNDLE_8),
    BUNDLE_9(CustomItemId.DONATOR_PROMO_BUNDLE_9),
    BUNDLE_10(CustomItemId.DONATOR_PROMO_BUNDLE_10);

    companion object {
        @JvmStatic
        fun getForItemOrNull(item: Int) = entries.firstOrNull { it.itemId == item }
    }
}

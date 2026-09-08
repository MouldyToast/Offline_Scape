package com.near_reality.game.content.storebundle

import com.zenyte.game.item.ids.*

/**
 * @author John J. Woloszyk / Kryeus
 * @date 8.6.2025
 */
enum class BundleChest(
    val itemId: Int
){
    BUNDLE_1(DONATOR_PROMO_BUNDLE_1),
    BUNDLE_2(DONATOR_PROMO_BUNDLE_2),
    BUNDLE_3(DONATOR_PROMO_BUNDLE_3),
    BUNDLE_4(DONATOR_PROMO_BUNDLE_4),
    BUNDLE_5(DONATOR_PROMO_BUNDLE_5),
    BUNDLE_6(DONATOR_PROMO_BUNDLE_6),
    BUNDLE_7(DONATOR_PROMO_BUNDLE_7),
    BUNDLE_8(DONATOR_PROMO_BUNDLE_8),
    BUNDLE_9(DONATOR_PROMO_BUNDLE_9),
    BUNDLE_10(DONATOR_PROMO_BUNDLE_10);

    companion object {
        @JvmStatic
        fun getForItemOrNull(item: Int) = entries.firstOrNull { it.itemId == item }
    }
}

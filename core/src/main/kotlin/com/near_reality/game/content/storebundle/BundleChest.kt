package com.near_reality.game.content.storebundle

import com.zenyte.game.item.ItemId

/**
 * @author John J. Woloszyk / Kryeus
 * @date 8.6.2025
 */
enum class BundleChest(
    val itemId: Int
){
    BUNDLE_1(ItemId.DONATOR_PROMO_BUNDLE_1),
    BUNDLE_2(ItemId.DONATOR_PROMO_BUNDLE_2),
    BUNDLE_3(ItemId.DONATOR_PROMO_BUNDLE_3),
    BUNDLE_4(ItemId.DONATOR_PROMO_BUNDLE_4),
    BUNDLE_5(ItemId.DONATOR_PROMO_BUNDLE_5),
    BUNDLE_6(ItemId.DONATOR_PROMO_BUNDLE_6),
    BUNDLE_7(ItemId.DONATOR_PROMO_BUNDLE_7),
    BUNDLE_8(ItemId.DONATOR_PROMO_BUNDLE_8),
    BUNDLE_9(ItemId.DONATOR_PROMO_BUNDLE_9),
    BUNDLE_10(ItemId.DONATOR_PROMO_BUNDLE_10);

    companion object {
        @JvmStatic
        fun getForItemOrNull(item: Int) = entries.firstOrNull { it.itemId == item }
    }
}

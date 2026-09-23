package org.jesse.game.content.araxxor.rewards

import org.jesse.game.item.Item
import org.jesse.game.item.ids.*
import java.util.concurrent.ThreadLocalRandom

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-29
 */
enum class AraxxorSuppliesDropTable(
    val id: Int,
    val minAmount: Int,
    val maxAmount: Int,
    val weight: Double
) {
    VENOM_SACK(ARAXYTE_VENOM_SAC, 1, 1, 16.0),
    SUPER_COMBAT_1(SUPER_COMBAT_POTION1, 1, 1, 16.0),
    PRAYER_3(PRAYER_POTION3, 1, 2, 16.0),
    PRAYER_4(PRAYER_POTION4, 1, 1, 16.0),
    SHARK(org.jesse.game.item.ids.SHARK, 2, 3, 16.0),
    PIE(WILD_PIE, 2, 3, 16.0)
    ;

    companion object {
        fun rollForItem(): Item? {
            val diceRole = ThreadLocalRandom.current().nextDouble()
            val sortedDrops = AraxxorNormalDropTable.entries.toTypedArray()
                .sortedByDescending { it.weight }
                .shuffled()

            sortedDrops.forEach { drop ->
                val required = 1 / drop.weight
                if (diceRole < required)
                    return Item(drop.id, ThreadLocalRandom.current().nextInt(drop.minAmount, drop.maxAmount + 1))
            }
            return null
        }
    }
}
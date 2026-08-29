package com.near_reality.game.model.item.submenu.items.rings

import com.near_reality.game.model.item.submenu.impl.ring_of_wealth.RingOfWealthFeatureAction
import com.near_reality.game.model.item.submenu.impl.ring_of_wealth.RingOfWealthRubAction
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.pluginextensions.ItemSubMenuPlugin
import com.zenyte.game.world.entity.player.Player
import com.zenyte.logger.NearRealityLogger
import org.slf4j.Logger

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-04-19
 */
class RingOfWealthSubmenu : ItemSubMenuPlugin() {
    private val log: Logger = NearRealityLogger.getLogger(RingOfWealthSubmenu::class.java)

    private val featuresIndex = 4
    private val rubIndex = 6

    override fun handle(
        player: Player,
        ring: Item,
        optionId: Int,
        subOptionId: Int
    ) {
        when (optionId) {
            rubIndex -> {
                RingOfWealthRubAction().onAction(player, subOptionId)
                val isImbued = ring.name.contains("ring of wealth (i", ignoreCase = true)
                val nextRing = if (isImbued) nextImbuedRing else nextRing
                ring.id = nextRing[ring.id]!!
                player.inventory.refreshAll()
            }

            featuresIndex -> RingOfWealthFeatureAction().onAction(player, subOptionId)
            else -> log.warn(
                "${this.javaClass.simpleName} submenu option {} subOption {} handled",
                optionId,
                subOptionId
            )
        }
    }

    private val nextImbuedRing = mapOf(
        ItemId.RING_OF_WEALTH_I5 to ItemId.RING_OF_WEALTH_I4,
        ItemId.RING_OF_WEALTH_I4 to ItemId.RING_OF_WEALTH_I3,
        ItemId.RING_OF_WEALTH_I3 to ItemId.RING_OF_WEALTH_I2,
        ItemId.RING_OF_WEALTH_I2 to ItemId.RING_OF_WEALTH_I1,
        ItemId.RING_OF_WEALTH_I1 to ItemId.RING_OF_WEALTH_I,
    )
    private val nextRing = mapOf(
        ItemId.RING_OF_WEALTH_5 to ItemId.RING_OF_WEALTH_4,
        ItemId.RING_OF_WEALTH_4 to ItemId.RING_OF_WEALTH_3,
        ItemId.RING_OF_WEALTH_3 to ItemId.RING_OF_WEALTH_2,
        ItemId.RING_OF_WEALTH_2 to ItemId.RING_OF_WEALTH_1,
        ItemId.RING_OF_WEALTH_1 to ItemId.RING_OF_WEALTH,
    )

    override fun getItems(): IntArray =
        intArrayOf(
            ItemId.RING_OF_WEALTH_I5,
            ItemId.RING_OF_WEALTH_I4,
            ItemId.RING_OF_WEALTH_I3,
            ItemId.RING_OF_WEALTH_I2,
            ItemId.RING_OF_WEALTH_I1,

            ItemId.RING_OF_WEALTH_5,
            ItemId.RING_OF_WEALTH_4,
            ItemId.RING_OF_WEALTH_3,
            ItemId.RING_OF_WEALTH_2,
            ItemId.RING_OF_WEALTH_1
        )

}
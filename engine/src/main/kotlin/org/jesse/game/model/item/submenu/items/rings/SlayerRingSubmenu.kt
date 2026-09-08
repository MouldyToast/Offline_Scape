package org.jesse.game.model.item.submenu.items.rings

import org.jesse.game.model.item.submenu.impl.ring_of_wealth.RingOfWealthFeatureAction
import org.jesse.game.model.item.submenu.impl.ring_of_wealth.RingOfWealthRubAction
import org.jesse.game.model.item.submenu.impl.slayer_ring.SlayerRingRubAction
import org.jesse.game.model.item.submenu.impl.slayer_ring.SlayerRingTeleportAction
import org.jesse.game.item.Item
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.pluginextensions.ItemSubMenuPlugin
import org.jesse.game.world.entity.player.Player
import org.jesse.logger.NearRealityLogger
import org.slf4j.Logger

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-04-19
 */
class SlayerRingSubmenu(
    private val rubIndex: Int = 3,
    private val teleportIndex: Int = 4
) : ItemSubMenuPlugin() {
    private val log: Logger = NearRealityLogger.getLogger(SlayerRingSubmenu::class.java)

    override fun handle(player: Player, ring: Item, optionId: Int, subOptionId: Int) {
        when (optionId) {
            teleportIndex -> {
                SlayerRingTeleportAction().onAction(player, subOptionId)
                ring.id = nexRingMap[ring.id]!!
                player.inventory.refreshAll()
            }
            rubIndex -> SlayerRingRubAction().onAction(player, subOptionId)
            else -> log.warn("${this.javaClass.simpleName} submenu option {} subOption {} handled", optionId, subOptionId)
        }
    }

    private val nexRingMap = mapOf(
        SLAYER_RING_ETERNAL to SLAYER_RING_ETERNAL,
        SLAYER_RING_8 to SLAYER_RING_7,
        SLAYER_RING_7 to SLAYER_RING_6,
        SLAYER_RING_6 to SLAYER_RING_5,
        SLAYER_RING_5 to SLAYER_RING_4,
        SLAYER_RING_4 to SLAYER_RING_3,
        SLAYER_RING_3 to SLAYER_RING_2,
        SLAYER_RING_2 to SLAYER_RING_1,
        SLAYER_RING_1 to -1,
    )

    override fun getItems(): IntArray =
        intArrayOf(
            SLAYER_RING_ETERNAL,

            SLAYER_RING_8,
            SLAYER_RING_7,
            SLAYER_RING_6,
            SLAYER_RING_5,
            SLAYER_RING_4,
            SLAYER_RING_3,
            SLAYER_RING_2,
            SLAYER_RING_1
        )

}
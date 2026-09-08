package org.jesse.game.model.item.submenu.items.amulets

import org.jesse.game.model.item.submenu.impl.GloryAmuletRubAction
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
 * @since 2025-04-09
 */
class GloryAmuletSub(
    private val rubAction: Int = 6
) : ItemSubMenuPlugin() {
    private val log: Logger = NearRealityLogger.getLogger(GloryAmuletSub::class.java)

    override fun handle(
        player: Player,
        amulet: Item,
        optionId: Int,
        subOptionId: Int
    ) {
        if (optionId == rubAction) {
            GloryAmuletRubAction().onAction(player, subOptionId)
            val isTrimmed = amulet.name.contains("amulet of glory (t", ignoreCase = true)
            val nextAmulet = if (isTrimmed) toNextAmuletT else toNextAmulet
            amulet.id = nextAmulet[amulet.id]!!
            player.inventory.refreshAll()
            return
        }
        log.info("${this.javaClass.simpleName} submenu option {} subOption {} handled", optionId, subOptionId)
    }

    private val toNextAmulet = mapOf(
        AMULET_OF_GLORY6 to AMULET_OF_GLORY5,
        AMULET_OF_GLORY5 to AMULET_OF_GLORY4,
        AMULET_OF_GLORY4 to AMULET_OF_GLORY3,
        AMULET_OF_GLORY3 to AMULET_OF_GLORY2,
        AMULET_OF_GLORY2 to AMULET_OF_GLORY1,
        AMULET_OF_GLORY1 to AMULET_OF_GLORY,
    )

    private val toNextAmuletT = mapOf(
        AMULET_OF_GLORY_T6 to AMULET_OF_GLORY_T5,
        AMULET_OF_GLORY_T5 to AMULET_OF_GLORY_T4,
        AMULET_OF_GLORY_T4 to AMULET_OF_GLORY_T3,
        AMULET_OF_GLORY_T3 to AMULET_OF_GLORY_T2,
        AMULET_OF_GLORY_T2 to AMULET_OF_GLORY_T1,
        AMULET_OF_GLORY_T1 to AMULET_OF_GLORY_T,
    )

    override fun getItems(): IntArray =
        intArrayOf(
            AMULET_OF_ETERNAL_GLORY,

            AMULET_OF_GLORY,
            AMULET_OF_GLORY1,
            AMULET_OF_GLORY2,
            AMULET_OF_GLORY3,
            AMULET_OF_GLORY4,
            AMULET_OF_GLORY5,
            AMULET_OF_GLORY6,

            AMULET_OF_GLORY_T,
            AMULET_OF_GLORY_T1,
            AMULET_OF_GLORY_T2,
            AMULET_OF_GLORY_T3,
            AMULET_OF_GLORY_T4,
            AMULET_OF_GLORY_T5,
            AMULET_OF_GLORY_T6,
        )

}
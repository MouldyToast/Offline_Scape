package org.jesse.game.model.item.submenu.items.amulets

import org.jesse.game.model.item.submenu.impl.DigsitePendantRubAction
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
class DigsitePendantSub(
    private val rubAction: Int = 6
) : ItemSubMenuPlugin() {
    private val log: Logger = NearRealityLogger.getLogger(DigsitePendantSub::class.java)

    override fun handle(
        player: Player,
        amulet: Item,
        optionId: Int,
        subOptionId: Int
    ) {
        if (optionId == rubAction) {
            DigsitePendantRubAction().onAction(player, subOptionId)
            val nextAmulet = toNextAmulet
            amulet.id = nextAmulet[amulet.id]!!
            player.inventory.refreshAll()
            return
        }
        log.info("${this.javaClass.simpleName} submenu option {} subOption {} handled", optionId, subOptionId)
    }

    private val toNextAmulet = mapOf(
        DIGSITE_PENDANT_5 to DIGSITE_PENDANT_4,
        DIGSITE_PENDANT_4 to DIGSITE_PENDANT_3,
        DIGSITE_PENDANT_3 to DIGSITE_PENDANT_2,
        DIGSITE_PENDANT_2 to DIGSITE_PENDANT_1,
        DIGSITE_PENDANT_1 to -1,
    )

    override fun getItems(): IntArray =
        intArrayOf(
            DIGSITE_PENDANT_5,
            DIGSITE_PENDANT_4,
            DIGSITE_PENDANT_3,
            DIGSITE_PENDANT_2,
            DIGSITE_PENDANT_1
        )
}
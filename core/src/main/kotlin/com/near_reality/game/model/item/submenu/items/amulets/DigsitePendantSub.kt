package com.near_reality.game.model.item.submenu.items.amulets

import com.near_reality.game.model.item.submenu.impl.DigsitePendantRubAction
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
        ItemId.DIGSITE_PENDANT_5 to ItemId.DIGSITE_PENDANT_4,
        ItemId.DIGSITE_PENDANT_4 to ItemId.DIGSITE_PENDANT_3,
        ItemId.DIGSITE_PENDANT_3 to ItemId.DIGSITE_PENDANT_2,
        ItemId.DIGSITE_PENDANT_2 to ItemId.DIGSITE_PENDANT_1,
        ItemId.DIGSITE_PENDANT_1 to -1,
    )

    override fun getItems(): IntArray =
        intArrayOf(
            ItemId.DIGSITE_PENDANT_5,
            ItemId.DIGSITE_PENDANT_4,
            ItemId.DIGSITE_PENDANT_3,
            ItemId.DIGSITE_PENDANT_2,
            ItemId.DIGSITE_PENDANT_1
        )
}
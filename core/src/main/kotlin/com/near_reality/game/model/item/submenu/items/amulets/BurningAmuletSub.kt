package com.near_reality.game.model.item.submenu.items.amulets

import com.near_reality.game.model.item.submenu.impl.BurningAmuletRubAction
import com.zenyte.game.item.Item
import com.zenyte.game.item.ids.*
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
class BurningAmuletSub(
    private val rubAction: Int = 6
) : ItemSubMenuPlugin() {
    private val log: Logger = NearRealityLogger.getLogger(BurningAmuletSub::class.java)

    override fun handle(
        player: Player,
        amulet: Item,
        optionId: Int,
        subOptionId: Int
    ) {
        if (optionId == rubAction) {
            BurningAmuletRubAction().onAction(player, subOptionId)
            val nextAmulet = toNextAmulet
            amulet.id = nextAmulet[amulet.id]!!
            player.inventory.refreshAll()
            return
        }
        log.info("${this.javaClass.simpleName} submenu option {} subOption {} handled", optionId, subOptionId)
    }

    private val toNextAmulet = mapOf(
        BURNING_AMULET5 to BURNING_AMULET4,
        BURNING_AMULET4 to BURNING_AMULET3,
        BURNING_AMULET3 to BURNING_AMULET2,
        BURNING_AMULET2 to BURNING_AMULET1,
        BURNING_AMULET1 to -1,
    )

    override fun getItems(): IntArray =
        intArrayOf(
            BURNING_AMULET5,
            BURNING_AMULET4,
            BURNING_AMULET3,
            BURNING_AMULET2,
            BURNING_AMULET1
        )
}
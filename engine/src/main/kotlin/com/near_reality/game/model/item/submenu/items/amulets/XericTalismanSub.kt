package com.near_reality.game.model.item.submenu.items.amulets

import com.near_reality.game.model.item.submenu.impl.XericTalismanRubAction
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
 * @since 2025-04-09
 */
class XericTalismanSub(
    val rubAction: Int = 4
) : ItemSubMenuPlugin() {
    private val log: Logger = NearRealityLogger.getLogger(XericTalismanSub::class.java)

    override fun handle(
        player: Player,
        amulet: Item,
        optionId: Int,
        subOptionId: Int
    ) {
        if (optionId == rubAction) {
            XericTalismanRubAction().onAction(player, subOptionId)
            return
        }
        log.info("${this.javaClass.simpleName} submenu option {} subOption {} handled", optionId, subOptionId)
    }

    override fun getItems(): IntArray = intArrayOf(XERICS_TALISMAN)

}
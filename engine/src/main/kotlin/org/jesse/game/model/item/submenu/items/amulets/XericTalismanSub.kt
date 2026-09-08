package org.jesse.game.model.item.submenu.items.amulets

import org.jesse.game.model.item.submenu.impl.XericTalismanRubAction
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
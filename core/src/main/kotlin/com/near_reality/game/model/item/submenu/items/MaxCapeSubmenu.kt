package com.near_reality.game.model.item.submenu.items

import com.near_reality.game.model.item.submenu.impl.max_cape.MaxCapeFeatureAction
import com.near_reality.game.model.item.submenu.impl.max_cape.MaxCapeSpellbookAction
import com.near_reality.game.model.item.submenu.impl.max_cape.MaxCapeTeleportAction
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
 * @since 2025-04-09
 */
class MaxCapeSubmenu : ItemSubMenuPlugin() {
    private val log: Logger = NearRealityLogger.getLogger(MaxCapeSubmenu::class.java)

    private val teleportIndex = 3
    private val spellbookIndex = 4
    private val featuresIndex = 6

    override fun handle(
        player: Player,
        cape: Item,
        optionId: Int,
        subOptionId: Int
    ) {
        log.info("Max cape submenu option {} subOption {} handled", optionId, subOptionId)
        when (optionId) {
            teleportIndex -> MaxCapeTeleportAction().onAction(player, subOptionId)
            spellbookIndex -> MaxCapeSpellbookAction().onAction(player, subOptionId)
            featuresIndex -> MaxCapeFeatureAction().onAction(player, subOptionId)
            else -> log.warn("Invalid Max cape submenu option $optionId")
        }
    }

    override fun getItems(): IntArray = intArrayOf(ItemId.MAX_CAPE)

}
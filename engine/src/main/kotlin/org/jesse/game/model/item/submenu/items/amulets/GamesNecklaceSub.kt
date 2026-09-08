package org.jesse.game.model.item.submenu.items.amulets

import org.jesse.game.model.item.submenu.impl.GamesNecklaceRubAction
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
class GamesNecklaceSub(
    private val rubAction: Int = 6
) : ItemSubMenuPlugin() {
    private val log: Logger = NearRealityLogger.getLogger(GamesNecklaceSub::class.java)

    override fun handle(
        player: Player,
        amulet: Item,
        optionId: Int,
        subOptionId: Int
    ) {
        if (optionId == rubAction) {
            GamesNecklaceRubAction().onAction(player, subOptionId)
            val nextAmulet = toNextAmulet
            amulet.id = nextAmulet[amulet.id]!!
            player.inventory.refreshAll()
            return
        }
        log.info("${this.javaClass.simpleName} submenu option {} subOption {} handled", optionId, subOptionId)
    }

    private val toNextAmulet = mapOf(
        GAMES_NECKLACE8 to GAMES_NECKLACE7,
        GAMES_NECKLACE7 to GAMES_NECKLACE5,
        GAMES_NECKLACE6 to GAMES_NECKLACE5,
        GAMES_NECKLACE5 to GAMES_NECKLACE4,
        GAMES_NECKLACE4 to GAMES_NECKLACE3,
        GAMES_NECKLACE3 to GAMES_NECKLACE2,
        GAMES_NECKLACE2 to GAMES_NECKLACE1,
        GAMES_NECKLACE1 to -1,
    )

    override fun getItems(): IntArray =
        intArrayOf(
            GAMES_NECKLACE8,
            GAMES_NECKLACE7,
            GAMES_NECKLACE6,
            GAMES_NECKLACE5,
            GAMES_NECKLACE4,
            GAMES_NECKLACE3,
            GAMES_NECKLACE2,
            GAMES_NECKLACE1
        )
}
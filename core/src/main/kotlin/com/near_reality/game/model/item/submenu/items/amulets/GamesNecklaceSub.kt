package com.near_reality.game.model.item.submenu.items.amulets

import com.near_reality.game.model.item.submenu.impl.GamesNecklaceRubAction
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
        ItemId.GAMES_NECKLACE8 to ItemId.GAMES_NECKLACE7,
        ItemId.GAMES_NECKLACE7 to ItemId.GAMES_NECKLACE5,
        ItemId.GAMES_NECKLACE6 to ItemId.GAMES_NECKLACE5,
        ItemId.GAMES_NECKLACE5 to ItemId.GAMES_NECKLACE4,
        ItemId.GAMES_NECKLACE4 to ItemId.GAMES_NECKLACE3,
        ItemId.GAMES_NECKLACE3 to ItemId.GAMES_NECKLACE2,
        ItemId.GAMES_NECKLACE2 to ItemId.GAMES_NECKLACE1,
        ItemId.GAMES_NECKLACE1 to -1,
    )

    override fun getItems(): IntArray =
        intArrayOf(
            ItemId.GAMES_NECKLACE8,
            ItemId.GAMES_NECKLACE7,
            ItemId.GAMES_NECKLACE6,
            ItemId.GAMES_NECKLACE5,
            ItemId.GAMES_NECKLACE4,
            ItemId.GAMES_NECKLACE3,
            ItemId.GAMES_NECKLACE2,
            ItemId.GAMES_NECKLACE1
        )
}
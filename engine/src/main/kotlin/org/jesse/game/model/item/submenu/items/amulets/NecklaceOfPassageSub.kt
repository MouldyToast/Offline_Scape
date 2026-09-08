package org.jesse.game.model.item.submenu.items.amulets

import org.jesse.game.model.item.submenu.impl.NecklaceOfPassageRubAction
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
class NecklaceOfPassageSub(
    private val rubAction: Int = 6
) : ItemSubMenuPlugin() {
    private val log: Logger = NearRealityLogger.getLogger(NecklaceOfPassageSub::class.java)

    override fun handle(
        player: Player,
        amulet: Item,
        optionId: Int,
        subOptionId: Int
    ) {
        if (optionId == rubAction) {
            NecklaceOfPassageRubAction().onAction(player, subOptionId)
            val nextAmulet = toNextAmulet
            amulet.id = nextAmulet[amulet.id]!!
            player.inventory.refreshAll()
            return
        }
        log.info("${this.javaClass.simpleName} submenu option {} subOption {} handled", optionId, subOptionId)
    }

    private val toNextAmulet = mapOf(
        NECKLACE_OF_PASSAGE5 to NECKLACE_OF_PASSAGE4,
        NECKLACE_OF_PASSAGE4 to NECKLACE_OF_PASSAGE3,
        NECKLACE_OF_PASSAGE3 to NECKLACE_OF_PASSAGE2,
        NECKLACE_OF_PASSAGE2 to NECKLACE_OF_PASSAGE1,
        NECKLACE_OF_PASSAGE1 to -1,
    )

    override fun getItems(): IntArray =
        intArrayOf(
            NECKLACE_OF_PASSAGE5,
            NECKLACE_OF_PASSAGE4,
            NECKLACE_OF_PASSAGE3,
            NECKLACE_OF_PASSAGE2,
            NECKLACE_OF_PASSAGE1
        )
}
package com.near_reality.game.model.item.submenu.items.amulets

import com.near_reality.game.model.item.submenu.impl.SkillsNecklaceRubAction
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
class SkillsNecklaceSub(
    private val rubAction: Int = 6
) : ItemSubMenuPlugin() {
    private val log: Logger = NearRealityLogger.getLogger(SkillsNecklaceSub::class.java)

    override fun handle(
        player: Player,
        amulet: Item,
        optionId: Int,
        subOptionId: Int
    ) {
        if (optionId == rubAction) {
            SkillsNecklaceRubAction().onAction(player, subOptionId)
            val nextAmulet = toNextAmulet
            amulet.id = nextAmulet[amulet.id]!!
            player.inventory.refreshAll()
            return
        }
        log.info("${this.javaClass.simpleName} submenu option {} subOption {} handled", optionId, subOptionId)
    }

    private val toNextAmulet = mapOf(
        ItemId.SKILLS_NECKLACE6 to ItemId.SKILLS_NECKLACE5,
        ItemId.SKILLS_NECKLACE5 to ItemId.SKILLS_NECKLACE4,
        ItemId.SKILLS_NECKLACE4 to ItemId.SKILLS_NECKLACE3,
        ItemId.SKILLS_NECKLACE3 to ItemId.SKILLS_NECKLACE2,
        ItemId.SKILLS_NECKLACE2 to ItemId.SKILLS_NECKLACE1,
        ItemId.SKILLS_NECKLACE1 to ItemId.SKILLS_NECKLACE,
    )

    override fun getItems(): IntArray =
        intArrayOf(
            ItemId.SKILLS_NECKLACE6,
            ItemId.SKILLS_NECKLACE5,
            ItemId.SKILLS_NECKLACE4,
            ItemId.SKILLS_NECKLACE3,
            ItemId.SKILLS_NECKLACE2,
            ItemId.SKILLS_NECKLACE1
        )
}
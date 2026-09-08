package com.near_reality.game.model.item.submenu.items.amulets

import com.near_reality.game.model.item.submenu.impl.SkillsNecklaceRubAction
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
        SKILLS_NECKLACE6 to SKILLS_NECKLACE5,
        SKILLS_NECKLACE5 to SKILLS_NECKLACE4,
        SKILLS_NECKLACE4 to SKILLS_NECKLACE3,
        SKILLS_NECKLACE3 to SKILLS_NECKLACE2,
        SKILLS_NECKLACE2 to SKILLS_NECKLACE1,
        SKILLS_NECKLACE1 to SKILLS_NECKLACE,
    )

    override fun getItems(): IntArray =
        intArrayOf(
            SKILLS_NECKLACE6,
            SKILLS_NECKLACE5,
            SKILLS_NECKLACE4,
            SKILLS_NECKLACE3,
            SKILLS_NECKLACE2,
            SKILLS_NECKLACE1
        )
}
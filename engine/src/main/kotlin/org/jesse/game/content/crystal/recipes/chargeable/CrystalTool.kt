package org.jesse.game.content.crystal.recipes.chargeable

import org.jesse.game.content.crystal.CrystalSeed
import org.jesse.game.content.crystal.recipes.CrystalChargeable
import org.jesse.game.content.skills.mining.PickAxeDefinition
import org.jesse.game.content.skills.woodcutting.AxeDefinition
import org.jesse.game.content.skills.fishing.FishingTool
import org.jesse.game.item.Item
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.degradableitems.DegradeType
import org.jesse.game.util.Utils
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.player.SkillConstants.*

/**
 *  Represents a crystal item that can be used as a skilling tool for.
 *
 * @author Stan van der Bend
 */
sealed class CrystalTool(
    override val productItemId: Int,
    override val inactiveId: Int,
    val skillRequirement: Pair<Int, Int>,
    vararg val additionalMaterial: Item,
) : CrystalChargeable() {

    companion object { val all by lazy { listOf(Axe, Harpoon, Pickaxe) } }

    override val crystalShardCost: Int = 120
    override val materials: List<Item> = CrystalSeed.TOOL * 1 + additionalMaterial
    override val startCharges = 20_000
    override val maximumCharges = 20_000
    override val requiredCrafting = 76
    override val requiredSmithing = 76
    override val craftingExperience = 6000
    override val smithingExperience = 6000
    override val type: DegradeType = DegradeType.CUSTOM

    object Axe : CrystalTool(
        productItemId = CRYSTAL_AXE,
        inactiveId = CRYSTAL_AXE_INACTIVE,
        skillRequirement = WOODCUTTING to 71,
        Item(DRAGON_AXE)
    ), AxeDefinition {
        override val cutTime: Int
            get() = if (Utils.random(0, 100) >= 45) 1 else 2
        override val levelRequired: Int = 71
        override val treeCutAnimation = Animation(8324)
        override val trunkCutAnimation = Animation(8326)
        override val canoeCutAnimation = Animation(8327)
    }

    /**
     * TODO: 35% increased catch rate compared to a regular harpoon and 12.5% increased compared to dragon harpoon
     * TODO: The Crystal harpoon also has a 1/3 chance of catching a Crystallised harpoonfish when fishing in Tempoross Cove.
     */
    object Harpoon : CrystalTool(
        productItemId = CRYSTAL_HARPOON,
        inactiveId = CRYSTAL_HARPOON_INACTIVE,
        skillRequirement = FISHING to 71,
        Item(DRAGON_HARPOON)
    ) {
        fun `is`(tool: FishingTool.Tool) =
            productItemId == tool.id || inactiveId == tool.id
    }

    /**
     * TODO: Excluding waiting for ores to respawn or having to move to a new resource,
     *       the crystal pickaxe is effectively a 3.03% increase in overall mining speed compared to a dragon pickaxe
     *       or 9.09% compared to a rune pickaxe.
     * TODO: A charged crystal pickaxe provides a chance to mine an ore every three ticks
     *       (with an additional 1/4 chance to reduce this to two ticks);
     *       an uncharged crystal pickaxe has the same mining capabilities as a dragon pickaxe.
     */
    object Pickaxe : CrystalTool(
        productItemId = CRYSTAL_PICKAXE,
        inactiveId = CRYSTAL_PICKAXE_INACTIVE,
        skillRequirement = MINING to 71,
        Item(DRAGON_PICKAXE)
    ), PickAxeDefinition {
        override val id: Int = productItemId
        override val level: Int = 71
        override val mineTime: Int
            get() = if (Utils.random(0, 100) >= 47) 1 else 2
        override val anim = Animation(8347)
        override val alternateAnimation = Animation(8345)
    }

    object CelestialSignet : CrystalTool(
        productItemId = CELESTIAL_SIGNET_UNCHARGED,
        inactiveId = CELESTIAL_RING_UNCHARGED,
        skillRequirement = MINING to 70,
        Item(CELESTIAL_RING_UNCHARGED),
        Item(STARDUST, 1_000),
    ) {
        override val crystalShardCost: Int = 100

        override val materials: List<Item> = additionalMaterial.toList()

        override val smithingExperience: Int = 5000

        override val craftingExperience: Int = 5000
    }
}

package com.near_reality.game.content.bountyhunter.crate

import com.zenyte.game.item.Item
import com.zenyte.game.model.item.pluginextensions.ItemPlugin
import com.zenyte.game.util.Utils
import com.zenyte.game.world.entity.player.container.RequestResult
import kotlin.random.Random
import com.zenyte.game.item.ids.*

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-02-07
 */
class BountyCrate: ItemPlugin() {
    override fun handle() {
        bind("Open") { player, crate, _ ->
            if (player.inventory.deleteItem(crate).result == RequestResult.SUCCESS) {
                // Chance for Artifact
                if (Utils.random(10) == 1) {
                    val reward = rollForArtifact()
                    if (reward != null)
                        player.inventory.addOrDrop(reward)
                }
                // 3 rolls on supply table
                repeat(3) {
                    val reward = rollForCrateSupplyLoot(crate)
                    if (reward != null)
                        player.inventory.addOrDrop(reward)
                }
            }
        }
    }

    private fun getCrateModifier(crate: Item) : Float =
        when(crate.id) {
            BOUNTY_CRATE_TIER_9 -> 1.9F
            BOUNTY_CRATE_TIER_8 -> 1.8F
            BOUNTY_CRATE_TIER_7 -> 1.7F
            BOUNTY_CRATE_TIER_6 -> 1.6F
            BOUNTY_CRATE_TIER_5 -> 1.5F
            BOUNTY_CRATE_TIER_4 -> 1.4F
            BOUNTY_CRATE_TIER_3 -> 1.3F
            BOUNTY_CRATE_TIER_2 -> 1.2F
            BOUNTY_CRATE_TIER_1 -> 1.1F
            else -> 1.0F
        }

    private fun rollForCrateSupplyLoot(crate: Item): Item? {
        return if (Random.nextInt(3) == 0) Item(COINS_995, ((Utils.random(75_000, 250_000) * getCrateModifier(crate)).toInt()))
          else if (Random.nextInt(3) == 0) Item(BLOOD_MONEY, Utils.random(1, 50))
          else if (Random.nextInt(3) == 0) Item(DRAGON_DART, ((Utils.random(50, 100) * getCrateModifier(crate)).toInt()))
          else if (Random.nextInt(3) == 0) Item(DRAGON_KNIFE, ((Utils.random(50, 100) * getCrateModifier(crate)).toInt()))
          else if (Random.nextInt(9) == 0) Item(BASTION_POTION4, ((Utils.random(10, 25) * getCrateModifier(crate)).toInt())).toNote()
          else if (Random.nextInt(9) == 0) Item(SARADOMIN_BREW4, ((Utils.random(10, 25) * getCrateModifier(crate)).toInt())).toNote()
          else if (Random.nextInt(9) == 0) Item(SANFEW_SERUM4, ((Utils.random(10, 25) * getCrateModifier(crate)).toInt())).toNote()
          else if (Random.nextInt(9) == 0) Item(SUPER_COMBAT_POTION4, ((Utils.random(10, 25) * getCrateModifier(crate)).toInt())).toNote()
          else if (Random.nextInt(9) == 0) Item(ANTIVENOM4, ((Utils.random(10, 25) * getCrateModifier(crate)).toInt())).toNote()
          else if (Random.nextInt(9) == 0) Item(ANGLERFISH, ((Utils.random(15, 45) * getCrateModifier(crate)).toInt())).toNote()
          else if (Random.nextInt(16) == 0) Item(32149) // Larran's Key Booster
          else if (Random.nextInt(16) == 0) Item(32150) // Gano Booster
          else if (Random.nextInt(16) == 0) Item(32154) // Blood Money Booster
          else if (Random.nextInt(16) == 0) Item(32166) // Revenant Booster
        else null
    }

    private fun rollForArtifact(): Item? {
        return if (Random.nextInt(25) == 0) Item(ANCIENT_EMBLEM)
          else if (Random.nextInt(33) == 0) Item(ANCIENT_TOTEM)
          else if (Random.nextInt(67) == 0) Item(ANCIENT_STATUETTE)
          else if (Random.nextInt(111) == 0) Item(ANCIENT_MEDALLION)
          else if (Random.nextInt(250) == 0) Item(ANCIENT_EFFIGY)
          else if (Random.nextInt(500) == 0) Item(ANCIENT_RELIC)
        else null
    }

    override fun getItems(): IntArray =
        intArrayOf(
            BOUNTY_CRATE_TIER_9,
            BOUNTY_CRATE_TIER_8,
            BOUNTY_CRATE_TIER_7,
            BOUNTY_CRATE_TIER_6,
            BOUNTY_CRATE_TIER_5,
            BOUNTY_CRATE_TIER_4,
            BOUNTY_CRATE_TIER_3,
            BOUNTY_CRATE_TIER_2,
            BOUNTY_CRATE_TIER_1
        )
}
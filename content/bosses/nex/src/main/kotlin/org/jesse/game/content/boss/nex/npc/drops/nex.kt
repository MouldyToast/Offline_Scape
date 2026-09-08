package org.jesse.game.content.boss.nex.npc.drops

import org.jesse.game.content.boss.nex.NexNPC
import org.jesse.scripts.npc.drops.table.DropTableContext
import org.jesse.scripts.npc.drops.table.always
import org.jesse.scripts.npc.drops.table.chance.immutable.StaticRollChance
import org.jesse.scripts.npc.drops.table.chance.immutable.StaticRollItemChance
import org.jesse.scripts.npc.drops.table.chance.immutable.StaticRollItemOneIn
import org.jesse.scripts.npc.drops.table.dsl.StandaloneDropTableBuilder
import org.jesse.scripts.npc.drops.table.noted
import org.jesse.game.item.Item
import org.jesse.game.util.Utils
import org.jesse.game.world.World
import kotlin.math.roundToInt
import org.jesse.scripts.npc.drops.NPCDropTableScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.item.ids.*
import org.jesse.game.item.ids.COAL
import org.jesse.game.item.ids.NEXLING
import org.jesse.game.item.ids.RUNITE_ORE
import org.jesse.game.item.ids.SHARK
import org.jesse.scripts.npc.drops.table.DropTableType.*
import org.jesse.game.world.entity.npc.drop.matrix.Drop
import org.jesse.game.world.entity.npc.drop.matrix.Drop.GUARANTEED_RATE
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor.PredicatedDrop
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor.DisplayedDrop

class NexDroptable : NPCDropTableScript() {

    object NexUniques : StandaloneDropTableBuilder({
        limit = 126
        static {
            ZARYTE_VAMBRACES quantity 1 rarity 30  announce everywhere
            NIHIL_HORN quantity 1 rarity 20  announce everywhere
            TORVA_FULLHELM_DAMAGED quantity 1 rarity 20  announce everywhere
            TORVA_PLATEBODY_DAMAGED quantity 1 rarity 20  announce everywhere
            TORVA_PLATELEGS_DAMAGED quantity 1 rarity 20  announce everywhere
            ANCIENT_HILT quantity 1 rarity 16  announce everywhere
        }
    })

    val common = 300

    val uncommon = 150

    val rare = 20

    fun scaleDropAmountOrNull(it: Item, contribution: Double, mvp: Boolean): Item? {
        var newAmount = it.amount * contribution
        if (mvp)
            newAmount *= 1.10
        return if (newAmount >= 1)
            Item(it.id, newAmount.roundToInt())
        else
            null
    }

    fun scaleRarity(dropChance: StaticRollChance, rarity: Double, damageContribution: Double): Double =
        if (dropChance is StaticRollItemOneIn)
            rarity / damageContribution
        else
            rarity * damageContribution

    fun stringify(itemList: List<Item>) =
        itemList
            .groupBy(Item::getName)
            .mapValues { items -> items.value.sumOf(Item::getAmount) }
            .entries
            .joinToString { (name, amount) -> "${amount}x $name" }

    init {
        npcs(NEX)

        onDeath {

            if (npc is NexNPC) {

                if (playerDamageContributions.isEmpty()) {
                    killer.sendDeveloperMessage("Did not find any damage dealers, ignoring drops.")
                    return@onDeath
                }

                val mvp = playerDamageContributions.maxBy { it.value }.key
                mvp.sendDeveloperMessage("You are the MVP with a damage percentage of ${playerDamageContributions[mvp]}")

                modifyDropRarity { dropChance ->

                    // unique drops are scaled in rarity for all players based on their total damage done
                    var rarity = dropChance.rarity.toDouble()

                    if (this is DropTableContext.ForPlayer) {
                        val unique = type == Unique
                        // player drop chance is determined by percentage of damage dealt to nex
                        var rarityScale = if (unique)
                            playerDamageContributions[player] ?: 0.0
                        else
                            1.0

                        // mvp has 10% increase of common and unique drops
                        if ((type == Main || unique) && mvp == player)
                            rarityScale += 0.10

                        if ((type == Unique || type == Tertiary) && player.variables.nexBoosterleft > 0)
                            rarityScale += 0.15

                        rarity = scaleRarity(dropChance, rarity, rarityScale)
                    }

                    rarity.toInt()
                }

                val playerCount = playerDamageContributions.size
                val commonDrops = getStaticTable(Main).staticRolls
                    .filterIsInstance<StaticRollItemChance>()
                    .filter { it.rarity == common }
                val uncommonDrops = getStaticTable(Main).staticRolls
                    .filterIsInstance<StaticRollItemChance>()
                    .filter { it.rarity != common }

                for ((player, contributionPercentage) in playerDamageContributions) {
                    if (player.variables.nexBoosterleft > 0) {
                        player.variables.nexBoosterleft--
                    }

                    val isMvp = player == mvp
                    if (isMvp)
                        rollStaticTableAndDrop(player, type = Always)
                    commonDrops.shuffled().take(2).forEach { it.rollAndDrop(npc, player, amountMultiplier = playerCount * contributionPercentage) }
                    uncommonDrops.shuffled().take(1).forEach { it.rollAndDrop(npc, player, amountMultiplier = playerCount * contributionPercentage) }
                    rollStaticTableAndDrop(player, type = Tertiary)
                    rollStaticTableAndDrop(player, type = Unique)
                    if (Utils.randomBoolean(4)) {
                        npc.dropItem(player, Item(995, Utils.random(50_000, 150_000)))
                    }
                }
            }
        }

        provideInfo<StaticRollItemChance> { table ->
            if(table == Unique)
                "This rate is scaled down based on player contributions & DR boost"
            else if (rarity == common)
                "The MVP has a 10% increased chance of getting this drop."
            else
                null
        }

        buildTable(5000) {
            Always {
                BIG_BONES quantity 1 rarity always
            }
            Unique {
                chance(117) roll NexUniques
            }
            Main {
                // Runes and ammunition
                BLOOD_RUNE quantity 84..325 rarity common
                DEATH_RUNE quantity 85..170 rarity common
                SOUL_RUNE quantity 86..227 rarity common
                DRAGON_BOLTS_UNF quantity 12..90 rarity common
                CANNONBALL quantity 42..298 rarity common
                AIR_RUNE quantity 123..1365 rarity uncommon
                FIRE_RUNE quantity 210..1655 rarity uncommon
                WATER_RUNE quantity 193..1599 rarity uncommon
                ONYX_BOLTS_E quantity 11..29 rarity uncommon
                // Resources
                AIR_ORB quantity (6..20).noted rarity common
                UNCUT_RUBY quantity (3..26).noted rarity common
                UNCUT_DIAMOND quantity (3..17).noted rarity common
                WINE_OF_ZAMORAK quantity (4..14).noted rarity common
                COAL quantity (23..95).noted rarity uncommon
                RUNITE_ORE quantity (2..28).noted rarity uncommon
                // Consumables
                SHARK quantity 3 rarity common
                PRAYER_POTION4 quantity 1 rarity common
                SUPER_RESTORE4 quantity 1 rarity common
                // Other
                ECUMENICAL_KEY_SHARD quantity 6..39 rarity common
                NIHIL_SHARD quantity 1..20 rarity common
                BLOOD_ESSENCE quantity 1..2 rarity uncommon
                COINS_995 quantity 8539..26738 rarity uncommon
                //        RUNE_SWORD quantity 1 rarity rare
            }
            Tertiary {
                SCROLL_BOX_ELITE quantity 1 oneIn 48
                NEXLING quantity 1 oneIn 500 announce everywhere
            }
        }
    }
}
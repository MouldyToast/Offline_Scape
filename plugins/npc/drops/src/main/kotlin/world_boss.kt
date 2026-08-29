package com.zenyte.game.content

import com.near_reality.game.content.commands.DeveloperCommands
import com.near_reality.game.content.donator.new_island.npc.AvatarOfCreation
import com.near_reality.scripts.npc.drops.table.DropTableContext
import com.near_reality.scripts.npc.drops.table.always
import com.near_reality.scripts.npc.drops.table.chance.immutable.StaticRollChance
import com.near_reality.scripts.npc.drops.table.chance.immutable.StaticRollItemOneIn
import com.near_reality.scripts.npc.drops.table.dsl.StandaloneDropTableBuilder
import com.near_reality.scripts.npc.drops.table.noted
import com.zenyte.game.item.ItemId.*
import com.near_reality.scripts.npc.drops.NPCDropTableScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.near_reality.game.util.invoke
import com.zenyte.game.item.ItemId
import com.near_reality.scripts.npc.drops.table.DropTableType.*
import com.zenyte.game.world.entity.npc.drop.matrix.Drop
import com.zenyte.game.world.entity.npc.drop.matrix.Drop.GUARANTEED_RATE
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor.PredicatedDrop
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor.DisplayedDrop

class WorldBossDroptable : NPCDropTableScript() {

    object Commons : StandaloneDropTableBuilder({
        limit = 52
        static {
            ABYSSAL_WHIP quantity (1..3).noted rarity 1
            AMULET_OF_FURY quantity (1..3).noted rarity 1
            BABYDRAGON_BONES quantity (50..100).noted rarity 2
            BLOOD_RUNE quantity 750 rarity 2
            WRATH_RUNE quantity 500 rarity 2
            DRAGON_BONES quantity (25..50).noted rarity 2
            SUPERIOR_DRAGON_BONES quantity (10..20).noted rarity 2
            SHERLOCKS_NOTES quantity (3..5) rarity 2
            PRAYER_POTION4 quantity (25..35).noted rarity 2
            SUPER_COMBAT_POTION4 quantity (25..35).noted rarity 2
            SARADOMIN_BREW4 quantity (25..35).noted rarity 2
            SUPER_RESTORE4 quantity (25..35).noted rarity 2
            SANFEW_SERUM4 quantity (15..20).noted rarity 2
            CRUSHED_NEST quantity 100.noted rarity 2
            SNAPE_GRASS quantity 75.noted rarity 2
            RED_SPIDERS_EGGS quantity 75.noted rarity 2
            TORSTOL_SEED quantity 10 rarity 2
            RUNITE_BAR quantity 150.noted rarity 2
            ANGLERFISH quantity 250.noted rarity 2
            COOKED_KARAMBWAN quantity 250.noted rarity 2
            PURE_ESSENCE quantity (1000..2500).noted rarity 2
            CANNONBALL quantity (500..750) rarity 2
            RUBY_BOLTS_E quantity 250 rarity 2
            DRAGONSTONE_BOLTS_E quantity 250 rarity 2
            DRAGON_BOLTS quantity 150 rarity 2
            DRAGON_ARROW quantity 150 rarity 2
            DRAGON_DART quantity 200 rarity 2
        }
    })

    object Uncommons : StandaloneDropTableBuilder({
        limit = 1600
        static {
            PET_MYSTERY_BOX quantity 1 rarity 100
            NR_VOTE_SHARD quantity 5 rarity 100
            AMULET_OF_ETERNAL_GLORY quantity 1 rarity 100
            BOOK_OF_THE_DEAD quantity 1 rarity 100
            LARRANS_KEY_BOOSTER quantity 1 rarity 100
            GANODERMIC_BOOSTER quantity 1 rarity 100
            SLAYER_BOOSTER quantity 1 rarity 100
            PET_BOOSTER quantity 1 rarity 100
            TOB_BOOSTER quantity 1 rarity 100
            NEX_BOOSTER quantity 1 rarity 100
            GAUNTLET_BOOSTER quantity 1 rarity 100
            32166 quantity 1 rarity 100
            32155 quantity 1 rarity 100
            32154 quantity 1 rarity 100
            SLAYER_TASK_RESET_SCROLL quantity 1 rarity 100
            SLAYER_TASK_PICKER_SCROLL quantity 1 rarity 100
        }
    })

    object Rares : StandaloneDropTableBuilder({
        limit = 3700
        static {
            ORB_OF_BLOOD quantity 1 rarity 100
            ORB_OF_XERIC quantity 1 rarity 100
            DRAGON_CROSSBOW quantity 1 rarity 250
            STAFF_OF_THE_DEAD quantity 1 rarity 250
            ENHANCED_CRYSTAL_KEY quantity 3.noted rarity 100
            MYSTERY_BOX quantity 1 rarity 100
            CRYSTAL_KEY quantity 10.noted rarity 100
            BURNING_CLAW quantity 1 rarity 500
            TORMENTED_SYNAPSE quantity 1 rarity 500
            DONATOR_PIN_10 quantity 1 rarity 500 announce everywhere
            ZAMORAK_BOW quantity 1 rarity 300
            BANDOS_BOW quantity 1 rarity 300
            SARADOMIN_BOW quantity 1 rarity 300
            ARMADYL_BOW quantity 1 rarity 300
        }
    })

    object SuperRares : StandaloneDropTableBuilder({
        limit = 55
        static {
            LIME_WHIP quantity 1 rarity 8 announce everywhere
            LAVA_WHIP quantity 1 rarity 8 announce everywhere
            SATURATED_HEART quantity 1 rarity 8 announce everywhere
            DEATH_CAPE quantity 1 rarity 8 announce everywhere
            DRAGON_KITE quantity 1 rarity 8 announce everywhere
            WORLD_BOOST_TOKEN quantity 1 rarity 4 announce everywhere
            REMNANT_POINT_VOUCHER_1 quantity (500..1000) rarity 4
            PRIMAL_FULL_HELM quantity 1 rarity 1 announce everywhere
            PRIMAL_PLATEBODY quantity 1 rarity 1 announce everywhere
            PRIMAL_PLATELEGS quantity 1 rarity 1 announce everywhere
            PRIMAL_PLATESKIRT quantity 1 rarity 1 announce everywhere
            PRIMAL_BOOTS quantity 1 rarity 1 announce everywhere
            PRIMAL_GAUNTLETS quantity 1 rarity 1 announce everywhere
            PRIMAL_CHAINBODY quantity 1 rarity 1 announce everywhere
        }
    })

    fun scaleRarity(dropChance: StaticRollChance, rarity: Double, damageContribution: Double): Double =
        if (dropChance is StaticRollItemOneIn)
            rarity / damageContribution
        else
            rarity * damageContribution

    init {
        npcs(AVATAR_OF_CREATION_10531)

        onDeath {
            if (npc is AvatarOfCreation) {
                if (playerDamageContributions.isEmpty()) {
                    killer.sendDeveloperMessage("Did not find any damage dealers, ignoring drops.")
                    return@onDeath
                }

                val mvp = playerDamageContributions.maxBy { it.value }.key
                mvp.sendDeveloperMessage("You are the MVP with a damage percentage of ${playerDamageContributions[mvp]}")

                modifyDropRarity { dropChance ->
                    var rarity = dropChance.rarity.toDouble()

                    if (this is DropTableContext.ForPlayer) {
                        if ((type == Main || type == Unique) && mvp == player)
                            rarity = scaleRarity(dropChance, rarity, 1.25)
                    }
                    rarity.toInt().coerceAtLeast(1)
                }

                val damageContributionByMvp = playerDamageContributions[mvp]?:0.0
                val damageDealtByMvp = (damageContributionByMvp * npc.maxHitpoints).toInt()
                for ((player, damageContribution) in playerDamageContributions) {
                    player.sendMessage("The MVP for this fight was: ${mvp?.name} dealing $damageDealtByMvp damage!")
                    if (damageContribution >= 0.001) {
                        rollTablesAndAward(player)
                        rollStaticTableAndAward(player, Main)
                    }
                    else
                        player.sendMessage("You did not contribute enough damage to receive any drops.")
                }
            }
        }

        buildTable {
            Always {
                TOME_OF_EXPERIENCE_30215 quantity 1 rarity always
                DRAGON_BONES quantity (5).noted rarity always
            }
            Main(100_000) {
                chance(98_000) roll Commons
                chance(2_000) roll Uncommons
            }
            Unique(12_000) {
                chance(360) roll Rares
                chance(30) roll SuperRares
            }
            Tertiary {
                ItemId.LIL_CREATOR quantity 1 oneIn 3000 announce everywhere
            }
        }
    }
}

package com.zenyte.game.content

import com.near_reality.game.world.entity.player.slayerLeftBoneDryStreak
import com.near_reality.scripts.npc.drops.table.always
import com.near_reality.scripts.npc.drops.table.noted
import com.near_reality.scripts.npc.drops.table.tables.rare.RareDropTable
import com.zenyte.game.item.Item
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

class CerberusDroptable : NPCDropTableScript() {

    init {
        npcs(CERBERUS, CERBERUS_5863)

        buildTable {
            Always {
                INFERNAL_ASHES quantity 1 rarity always
            }
            Unique(320) {
                PRIMORDIAL_CRYSTAL quantity 1 rarity 1
                PEGASIAN_CRYSTAL quantity 1 rarity 1
                ETERNAL_CRYSTAL quantity 1 rarity 1
                SMOULDERING_STONE quantity 1 rarity 1
            }
            Main(156) {
                // Weapons and armour
                RUNE_PLATEBODY quantity 1 rarity 5
                RUNE_CHAINBODY quantity 1 rarity 4
                RUNE_2H_SWORD quantity 1 rarity 4
                BLACK_DHIDE_BODY quantity 1 rarity 3
                RUNE_AXE quantity 1 rarity 3
                RUNE_PICKAXE quantity 1 rarity 3
                BATTLESTAFF quantity 6.noted rarity 3
                RUNE_FULL_HELM quantity 1 rarity 3
                LAVA_BATTLESTAFF quantity 1 rarity 2
                RUNE_HALBERD quantity 1 rarity 2
                // Runes and ammunition
                FIRE_RUNE quantity 300 rarity 6
                SOUL_RUNE quantity 100 rarity 6
                PURE_ESSENCE quantity 300.noted rarity 5
                BLOOD_RUNE quantity 60 rarity 4
                CANNONBALL quantity 50 rarity 4
                RUNITE_BOLTS_UNF quantity 40 rarity 4
                DEATH_RUNE quantity 100 rarity 3
                // Other
                ItemId.COAL quantity 120.noted rarity 6
                SUPER_RESTORE4 quantity 2 rarity 6
                SUMMER_PIE quantity 3 rarity 6
                COINS_995 quantity (10_000..20_000) rarity 5
                DRAGON_BONES quantity 20.noted rarity 5
                UNHOLY_SYMBOL quantity 1 rarity 5
                WINE_OF_ZAMORAK quantity 15.noted rarity 5
                ASHES quantity 50.noted rarity 4
                FIRE_ORB quantity 20.noted rarity 4
                GRIMY_TORSTOL quantity 6.noted rarity 4
                ItemId.RUNITE_ORE quantity 5.noted rarity 3
                UNCUT_DIAMOND quantity 5.noted rarity 3
                KEY_MASTER_TELEPORT quantity 3 rarity 2
                TORSTOL_SEED quantity 3 rarity 2
                // Rare drop table
                chance(31) roll RareDropTable
            }
            Tertiary {
                ENSOULED_HELLHOUND_HEAD quantity 1 oneIn 15
                JAR_OF_SOULS quantity 1 oneIn 1000 announce everywhere
                SLAYER_LEFT_BONE quantity 1 oneIn 500 announce everywhere
            }
        }

        onDeath {
            val drops = rollTables().values.flatten()
            for (drop in drops) {
                val item = drop.rollItem(killer, 1.0)
                if (item != null) {
                    if(item.id != SLAYER_LEFT_BONE) {
                        killer.slayerLeftBoneDryStreak++
                        if(killer.slayerLeftBoneDryStreak == 500) {
                            killer.sendMessage("You have received a slayer left bone for hitting a 500 kill drystreak.")
                            npc.dropItem(killer, Item(SLAYER_LEFT_BONE))
                        }
                    }
                    npc.dropItem(killer, item, npc.middleLocation, false)
                }
            }
        }
    }
}

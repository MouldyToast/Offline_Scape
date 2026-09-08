package com.near_reality.game.content.drops

import com.near_reality.scripts.npc.drops.table.noted
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
import com.zenyte.game.item.ItemId.BIG_BONES
import com.zenyte.game.item.ItemId.BLIGHTED_ANGLERFISH
import com.zenyte.game.item.ItemId.BLIGHTED_KARAMBWAN
import com.zenyte.game.item.ItemId.BLIGHTED_SUPER_RESTORE3
import com.zenyte.game.item.ItemId.BLIGHTED_SUPER_RESTORE4
import com.zenyte.game.item.ItemId.BLOOD_RUNE
import com.zenyte.game.item.ItemId.CANNONBALL
import com.zenyte.game.item.ItemId.CHAOS_RUNE
import com.zenyte.game.item.ItemId.CLAWS_OF_CALLISTO
import com.zenyte.game.item.ItemId.CLUE_SCROLL_ELITE
import com.zenyte.game.item.ItemId.COCONUT
import com.zenyte.game.item.ItemId.COINS_995
import com.zenyte.game.item.ItemId.CURVED_BONE
import com.zenyte.game.item.ItemId.DARK_CRAB
import com.zenyte.game.item.ItemId.DARK_FISHING_BAIT
import com.zenyte.game.item.ItemId.DEATH_RUNE
import com.zenyte.game.item.ItemId.DRAGON_2H_SWORD
import com.zenyte.game.item.ItemId.DRAGON_BONES
import com.zenyte.game.item.ItemId.DRAGON_PICKAXE
import com.zenyte.game.item.ItemId.GRIMY_TOADFLAX
import com.zenyte.game.item.ItemId.LIMPWURT_ROOT
import com.zenyte.game.item.ItemId.LONG_BONE
import com.zenyte.game.item.ItemId.LOOTING_BAG
import com.zenyte.game.item.ItemId.MAGIC_LOGS
import com.zenyte.game.item.ItemId.MAGIC_SEED
import com.zenyte.game.item.ItemId.MAHOGANY_LOGS
import com.zenyte.game.item.ItemId.PALM_TREE_SEED
import com.zenyte.game.item.ItemId.RANARR_SEED
import com.zenyte.game.item.ItemId.RANGING_POTION2
import com.zenyte.game.item.ItemId.RED_DRAGONHIDE
import com.zenyte.game.item.ItemId.RUNE_2H_SWORD
import com.zenyte.game.item.ItemId.RUNE_PICKAXE
import com.zenyte.game.item.ItemId.SNAPDRAGON_SEED
import com.zenyte.game.item.ItemId.SOUL_RUNE
import com.zenyte.game.item.ItemId.SUPERCOMPOST
import com.zenyte.game.item.ItemId.SUPER_COMBAT_POTION2
import com.zenyte.game.item.ItemId.SUPER_RESTORE4
import com.zenyte.game.item.ItemId.TYRANNICAL_RING
import com.zenyte.game.item.ItemId.UNCUT_DIAMOND
import com.zenyte.game.item.ItemId.UNCUT_DRAGONSTONE
import com.zenyte.game.item.ItemId.UNCUT_RUBY
import com.zenyte.game.item.ItemId.VOIDWAKER_HILT
import com.zenyte.game.item.ItemId.YEW_SEED

class ArtioDroptable : NPCDropTableScript() {

    init {
        /**
         * @author John J. Woloszyk / Kryeus
         * @date 8.7.2025
         */

        npcs(ARTIO)

        buildTable {
            Always {
                BIG_BONES quantity 1
            }
            Main(500) {

                DRAGON_2H_SWORD      quantity 1    rarity 2
                DRAGON_PICKAXE       quantity 1    rarity 2
                CLAWS_OF_CALLISTO    quantity 1    rarity 2
                TYRANNICAL_RING      quantity 1    rarity 1
                VOIDWAKER_HILT       quantity 1    rarity 1

                RANARR_SEED          quantity 3          rarity 24
                SNAPDRAGON_SEED      quantity 1          rarity 20
                SUPERCOMPOST         quantity 60.noted   rarity 10
                DARK_CRAB            quantity 9    rarity 19
                SUPER_RESTORE4       quantity 3    rarity 19

                // Weapons & Armour - 100
                RUNE_PICKAXE         quantity 1    rarity 50
                RUNE_2H_SWORD        quantity 1    rarity 50

                // Runes & Ammunition - 100
                CHAOS_RUNE           quantity 300  rarity 20
                DEATH_RUNE           quantity 220  rarity 20
                BLOOD_RUNE           quantity 140  rarity 20
                SOUL_RUNE            quantity 150  rarity 20
                CANNONBALL           quantity 190  rarity 20

                // Materials - 100
                MAHOGANY_LOGS        quantity 200.noted   rarity 20
                LIMPWURT_ROOT        quantity 20.noted    rarity 10
                MAGIC_LOGS           quantity 60.noted    rarity 10
                UNCUT_RUBY           quantity 17.noted    rarity 10
                UNCUT_DIAMOND        quantity 7.noted     rarity 10
                DRAGON_BONES         quantity 25.noted    rarity 10
                RED_DRAGONHIDE       quantity 55.noted    rarity 10
                UNCUT_DRAGONSTONE    quantity 1          rarity 8
                COCONUT              quantity 30.noted   rarity 8
                GRIMY_TOADFLAX       quantity 40.noted   rarity 4

                // Other - 100
                COINS_995            quantity 12000       rarity 84
                DARK_FISHING_BAIT    quantity 300        rarity 4
                YEW_SEED             quantity 1          rarity 4
                MAGIC_SEED           quantity 1          rarity 4
                PALM_TREE_SEED       quantity 1          rarity 4


            }
            Tertiary {
                LOOTING_BAG             quantity 1    oneIn 3
                CLUE_SCROLL_ELITE       quantity 1    oneIn 100
                LONG_BONE               quantity 1    oneIn 400
                CURVED_BONE             quantity 1    oneIn 4000
                BLIGHTED_ANGLERFISH     quantity (5..6) oneIn 18
                BLIGHTED_KARAMBWAN      quantity (5..6) oneIn 18
                BLIGHTED_SUPER_RESTORE3 quantity (3..4)  oneIn 18
                BLIGHTED_SUPER_RESTORE4 quantity (3..4)  oneIn 18
                RANGING_POTION2         quantity (2..3)  oneIn 18
                SUPER_COMBAT_POTION2    quantity (2..3)  oneIn 18
            }
        }
    }
}

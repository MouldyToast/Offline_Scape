package com.near_reality.game.content.drops

import com.near_reality.scripts.npc.drops.table.noted
import com.near_reality.scripts.npc.drops.NPCDropTableScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.near_reality.game.util.invoke
import com.zenyte.game.item.ids.*
import com.near_reality.scripts.npc.drops.table.DropTableType.*
import com.zenyte.game.world.entity.npc.drop.matrix.Drop
import com.zenyte.game.world.entity.npc.drop.matrix.Drop.GUARANTEED_RATE
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor.PredicatedDrop
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor.DisplayedDrop

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

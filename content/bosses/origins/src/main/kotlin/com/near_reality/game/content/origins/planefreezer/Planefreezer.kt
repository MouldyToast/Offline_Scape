package com.near_reality.game.content.origins.planefreezer

import com.near_reality.scripts.npc.drops.table.always
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
import com.zenyte.game.item.ItemId.COINS_995
import com.zenyte.game.item.ItemId.COOKED_KARAMBWAN
import com.zenyte.game.item.ItemId.CRYSTAL_KEY
import com.zenyte.game.item.ItemId.DEATH_RUNE
import com.zenyte.game.item.ItemId.EARTH_ORB
import com.zenyte.game.item.ItemId.FIRE_RUNE
import com.zenyte.game.item.ItemId.GRIMY_IRIT_LEAF
import com.zenyte.game.item.ItemId.GRIMY_SNAPDRAGON
import com.zenyte.game.item.ItemId.MYSTIC_GLOVES
import com.zenyte.game.item.ItemId.PRIMAL_BOOTS
import com.zenyte.game.item.ItemId.PRIMAL_GAUNTLETS
import com.zenyte.game.item.ItemId.PURE_ESSENCE
import com.zenyte.game.item.ItemId.RUNE_BOOTS
import com.zenyte.game.item.ItemId.RUNE_KITESHIELD
import com.zenyte.game.item.ItemId.RUNE_WARHAMMER
import com.zenyte.game.item.ItemId.STEEL_BAR
import com.zenyte.game.item.ItemId.TUNA_POTATO
import com.zenyte.game.item.ItemId.UNCUT_SAPPHIRE
import com.zenyte.game.item.ItemId.WATER_RUNE

class PlanefreezerDroptable : NPCDropTableScript() {

    init {
        /**
         * @author John J. Woloszyk / Kryeus
         * @date 8.6.2025
         */

        npcs(PLANE_FREEZER_LAKHRAHNAZ)

        buildTable {
            Always {
                ItemId.BONES quantity 1 rarity always
            }
            Main(1750) {
                // Common
                COINS_995 quantity (1500..3000) rarity 250
                WATER_RUNE quantity 300 rarity 175
                COOKED_KARAMBWAN quantity 4 rarity 125
                FIRE_RUNE quantity 200 rarity 125
                GRIMY_IRIT_LEAF quantity 4.noted rarity 100
                STEEL_BAR quantity 10.noted rarity 100
                ItemId.COAL quantity 25.noted rarity 75
                PURE_ESSENCE quantity 125.noted rarity 75
                UNCUT_SAPPHIRE quantity 3.noted rarity 60
                TUNA_POTATO quantity 3 rarity 60

                // Uncommon
                RUNE_KITESHIELD quantity 1 rarity 20
                MYSTIC_GLOVES quantity 1 rarity 15
                GRIMY_SNAPDRAGON quantity 2 rarity 25
                RUNE_WARHAMMER quantity 1 rarity 15
                DEATH_RUNE quantity 75 rarity 25
                ItemId.ADAMANTITE_ORE quantity 6.noted rarity 22
                RUNE_BOOTS quantity 1 rarity 16
                EARTH_ORB quantity 1 rarity 20
                CRYSTAL_KEY quantity 1 rarity 12

                // Rare
                PRIMAL_GAUNTLETS quantity 1 rarity 1 announce everywhere
                PRIMAL_BOOTS quantity 1 rarity 1 announce everywhere
            }
        }
    }
}

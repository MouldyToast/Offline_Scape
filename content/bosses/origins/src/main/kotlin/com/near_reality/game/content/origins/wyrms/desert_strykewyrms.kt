package com.near_reality.game.content.origins.wyrms

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
import com.zenyte.game.item.ItemId.ADAMANTITE_BAR
import com.zenyte.game.item.ItemId.BATTLESTAFF
import com.zenyte.game.item.ItemId.CHAOTIC_KITESHIELD
import com.zenyte.game.item.ItemId.COINS_995
import com.zenyte.game.item.ItemId.DRAGON_SCIMITAR
import com.zenyte.game.item.ItemId.FIRE_RUNE
import com.zenyte.game.item.ItemId.GRIMY_IRIT_LEAF
import com.zenyte.game.item.ItemId.GRIMY_KWUARM
import com.zenyte.game.item.ItemId.LANTADYME
import com.zenyte.game.item.ItemId.LOBSTER
import com.zenyte.game.item.ItemId.NATURE_RUNE
import com.zenyte.game.item.ItemId.PET_DESERT_STRYKEWYRM
import com.zenyte.game.item.ItemId.PURE_ESSENCE
import com.zenyte.game.item.ItemId.RUNE_BOOTS
import com.zenyte.game.item.ItemId.RUNE_LONGSWORD
import com.zenyte.game.item.ItemId.UNCUT_EMERALD

class DesertStrykewyrmsDroptable : NPCDropTableScript() {

    init {
        /**
         * @author John J. Woloszyk / Kryeus
         * @date 7.10.2025
         */


        npcs(DESERT_STRYKEWYRM)

        buildTable {
            Always {
                ItemId.BONES quantity 1 rarity always
            }
            Main(1250) {
                COINS_995 quantity (1000..2500) rarity 278
                FIRE_RUNE quantity 250 rarity 186
                LOBSTER quantity 8.noted rarity 139
                PURE_ESSENCE quantity 100.noted rarity 93
                NATURE_RUNE quantity 60 rarity 93
                GRIMY_IRIT_LEAF quantity 4.noted rarity 74
                ItemId.COAL quantity 25.noted rarity 74
                ADAMANTITE_BAR quantity 3.noted rarity 56
                ItemId.MITHRIL_ORE quantity 10.noted rarity 56
                UNCUT_EMERALD quantity 3.noted rarity 56

                RUNE_LONGSWORD quantity 1 rarity 20
                BATTLESTAFF quantity 1 rarity 25
                DRAGON_SCIMITAR quantity 1 rarity 17
                GRIMY_KWUARM quantity 2.noted rarity 25
                LANTADYME quantity 2.noted rarity 25
                RUNE_BOOTS quantity 1 rarity 15
                ItemId.RUNITE_ORE quantity 3.noted rarity 17

                CHAOTIC_KITESHIELD quantity 1 rarity 1 announce everywhere
            }
            Tertiary {
                PET_DESERT_STRYKEWYRM quantity 1 oneIn 5000
            }
        }
    }
}

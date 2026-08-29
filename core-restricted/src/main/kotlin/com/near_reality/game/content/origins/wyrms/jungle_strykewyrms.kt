package com.near_reality.game.content.origins.wyrms

import com.near_reality.scripts.npc.drops.table.always
import com.near_reality.scripts.npc.drops.table.noted
import com.near_reality.scripts.npc.drops.NPCDropTableScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.near_reality.game.util.invoke
import com.zenyte.game.item.ItemId
import com.zenyte.game.item.ItemId.*
import com.near_reality.scripts.npc.drops.table.DropTableType.*
import com.zenyte.game.world.entity.npc.drop.matrix.Drop
import com.zenyte.game.world.entity.npc.drop.matrix.Drop.GUARANTEED_RATE
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor.PredicatedDrop
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor.DisplayedDrop

class JungleStrykewyrmsDroptable : NPCDropTableScript() {

    init {
        /**
         * @author John J. Woloszyk / Kryeus
         * @date 7.10.2025
         */

        npcs(JUNGLE_STRYKEWYRM)

        buildTable {
            Always {
                ItemId.BONES quantity 1 rarity always
            }
            Main(1250) {
                COINS_995 quantity (1000..2500) rarity 285
                FIRE_RUNE quantity 250 rarity 188
                SWORDFISH quantity 6.noted rarity 118
                PURE_ESSENCE quantity 100.noted rarity 94
                EARTH_RUNE quantity 200 rarity 94
                NATURE_RUNE quantity 50 rarity 75
                ItemId.MITHRIL_ORE quantity 10.noted rarity 56
                GRIMY_HARRALANDER quantity 4.noted rarity 56
                UNCUT_SAPPHIRE quantity 3.noted rarity 56
                STEEL_BAR quantity 6.noted rarity 56

                GRIMY_AVANTOE quantity 3.noted rarity 25
                RUNE_MED_HELM quantity 1 rarity 20
                BATTLESTAFF quantity 1 rarity 20
                LAW_RUNE quantity 25 rarity 20
                GRIMY_DWARF_WEED quantity 2.noted rarity 20
                ItemId.ADAMANTITE_ORE quantity 5.noted rarity 20

                RUNE_SPEAR quantity 1 rarity 15
                MYSTIC_HAT quantity 1 rarity 10
                RUNITE_BOLTS quantity 15 rarity 10
                DRAGON_DAGGER quantity 1 rarity 10

                CHAOTIC_CROSSBOW quantity 1 rarity 1 announce everywhere
                EAGLE_EYE_KITESHIELD quantity 1 rarity 1 announce everywhere
            }
            Tertiary {
                PET_JUNGLE_STRYKEWYRM quantity 1 oneIn 5000
            }
        }
    }
}

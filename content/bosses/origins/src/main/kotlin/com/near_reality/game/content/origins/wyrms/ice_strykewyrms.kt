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

class IceStrykewyrmsDroptable : NPCDropTableScript() {

    init {
        /**
         * @author John J. Woloszyk / Kryeus
         * @date 7.10.2025
         */

        npcs(ICE_STRYKEWYRM)

        buildTable {
            Always {
                ItemId.BONES quantity 1 rarity always
            }
            Main(2500) {
                COINS_995 quantity (1000..2500) rarity 600
                WATER_RUNE quantity 300 rarity 400
                ItemId.SHARK quantity 4.noted rarity 250
                FIRE_RUNE quantity 400 rarity 175
                GRIMY_CADANTINE quantity 3.noted rarity 175
                NATURE_RUNE quantity 60 rarity 150
                STEEL_BAR quantity 10.noted rarity 150
                UNCUT_RUBY quantity 3.noted rarity 150
                MITHRIL_BAR quantity 4.noted rarity 150

                STAFF_OF_FIRE quantity 1 rarity 50
                DRAGON_DAGGERP quantity 1 rarity 50
                MYSTIC_GLOVES quantity 1 rarity 40
                GRIMY_TORSTOL quantity 2.noted rarity 40
                FARSEER_HELM quantity 1 rarity 40
                GRIMY_DWARF_WEED quantity 3.noted rarity 40
                RUNE_KITESHIELD quantity 1.noted rarity 28

                STAFF_OF_LIGHT quantity 1 rarity 8
                FARSEER_KITESHIELD quantity 1 rarity 2 announce everywhere
            }
            Tertiary {
                PET_ICE_STRYKEWYRM quantity 1 oneIn 5000
            }
        }
    }
}

package org.jesse.game.content.origins.wyrms

import org.jesse.scripts.npc.drops.table.always
import org.jesse.scripts.npc.drops.table.noted
import org.jesse.scripts.npc.drops.NPCDropTableScript
import org.jesse.game.npc.ids.*
import org.jesse.game.util.invoke
import org.jesse.game.item.ids.*
import org.jesse.game.item.ids.ADAMANTITE_ORE
import org.jesse.game.item.ids.BONES
import org.jesse.game.item.ids.MITHRIL_ORE
import org.jesse.scripts.npc.drops.table.DropTableType.*
import org.jesse.game.world.entity.npc.drop.matrix.Drop
import org.jesse.game.world.entity.npc.drop.matrix.Drop.GUARANTEED_RATE
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor.PredicatedDrop
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor.DisplayedDrop

class JungleStrykewyrmsDroptable : NPCDropTableScript() {

    init {
        /**
         * @author John J. Woloszyk / Kryeus
         * @date 7.10.2025
         */

        npcs(JUNGLE_STRYKEWYRM)

        buildTable {
            Always {
                BONES quantity 1 rarity always
            }
            Main(1250) {
                COINS_995 quantity (1000..2500) rarity 285
                FIRE_RUNE quantity 250 rarity 188
                SWORDFISH quantity 6.noted rarity 118
                PURE_ESSENCE quantity 100.noted rarity 94
                EARTH_RUNE quantity 200 rarity 94
                NATURE_RUNE quantity 50 rarity 75
                MITHRIL_ORE quantity 10.noted rarity 56
                GRIMY_HARRALANDER quantity 4.noted rarity 56
                UNCUT_SAPPHIRE quantity 3.noted rarity 56
                STEEL_BAR quantity 6.noted rarity 56

                GRIMY_AVANTOE quantity 3.noted rarity 25
                RUNE_MED_HELM quantity 1 rarity 20
                BATTLESTAFF quantity 1 rarity 20
                LAW_RUNE quantity 25 rarity 20
                GRIMY_DWARF_WEED quantity 2.noted rarity 20
                ADAMANTITE_ORE quantity 5.noted rarity 20

                RUNE_SPEAR quantity 1 rarity 15
                MYSTIC_HAT quantity 1 rarity 10
                RUNITE_BOLTS quantity 15 rarity 10
                DRAGON_DAGGER quantity 1 rarity 10

                EAGLE_EYE_KITESHIELD quantity 1 rarity 1 announce everywhere
            }
            Tertiary {
                PET_JUNGLE_STRYKEWYRM quantity 1 oneIn 5000
            }
        }
    }
}

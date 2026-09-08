package com.zenyte.game.content

import com.near_reality.scripts.npc.drops.table.always
import com.near_reality.scripts.npc.drops.table.noted
import com.near_reality.scripts.npc.drops.table.tables.misc.TalismanDropTable
import com.near_reality.scripts.npc.drops.table.tables.seed.RareSeedDropTable
import com.zenyte.game.content.achievementdiary.DiaryComplexity
import com.zenyte.game.content.achievementdiary.diaries.FremennikDiary
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

class DagannothSupremeDroptable : NPCDropTableScript() {

    init {
        npcs(DAGANNOTH_SUPREME, DAGANNOTH_SUPREME_6496)

        buildTable {
            Always {
                DAGANNOTH_BONES quantity 1 rarity always transformItem {item ->
                    if(this.achievementDiaries.isAllSetCompleted(DiaryComplexity.ELITE, FremennikDiary.VALUES))
                        item.toNote()
                    item
                }
                DAGANNOTH_HIDE quantity 1 rarity always
            }
            Main(85) {
                // Weapons and armour
                MITHRIL_KNIFE quantity (25..50) rarity 5
                RED_DHIDE_VAMB quantity 1 rarity 3
                RUNE_THROWNAXE quantity (5..10) rarity 2
                ADAMANT_DART quantity (10..25) rarity 2
                IRON_KNIFE quantity (200..500) rarity 2
                STEEL_KNIFE quantity (50..150) rarity 2
                FREMENNIK_BLADE quantity 1 rarity 1
                FREMENNIK_SHIELD quantity 1 rarity 1
                FREMENNIK_HELM quantity 1 rarity 1
                SEERCULL quantity 1 rarity 1
                DRAGON_AXE quantity 1 rarity 1
                ARCHER_HELM quantity 1 rarity 1
                SPINED_BODY quantity 1 rarity 1
                SPINED_CHAPS quantity 1 rarity 1
                ARCHERS_RING quantity 1 rarity 1
                // Ammunition
                STEEL_ARROW quantity (50..250) rarity 3
                RUNITE_BOLTS quantity (2..12) rarity 3
                IRON_ARROW quantity (200..700) rarity 4
                // Seeds
                chance(7) roll RareSeedDropTable
                // Talismans
                chance(1) roll TalismanDropTable
                // Other
                COINS_995 quantity (500..1110) rarity 5
                OYSTER_PEARLS quantity 1 rarity 4
                OPAL_BOLT_TIPS quantity (10..30) rarity 2
                SHARK quantity 5 rarity 3
                YEW_LOGS quantity (50..150).noted rarity 2
                GRIMY_RANARR_WEED quantity 1 rarity 3
                MAPLE_LOGS quantity (15..65).noted rarity 2
                RUNITE_LIMBS quantity 1 rarity 2
                FEATHER quantity (250..500) rarity 1
            }
            Tertiary {
                ENSOULED_DAGANNOTH_HEAD quantity 1 oneIn 20
            }
        }
    }
}

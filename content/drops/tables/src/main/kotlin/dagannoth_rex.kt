package com.zenyte.game.content

import com.near_reality.scripts.npc.drops.table.always
import com.near_reality.scripts.npc.drops.table.noted
import com.near_reality.scripts.npc.drops.table.tables.gem.GemDropTable
import com.near_reality.scripts.npc.drops.table.tables.misc.TalismanDropTable
import com.near_reality.scripts.npc.drops.table.tables.rare.RareDropTable
import com.zenyte.game.content.achievementdiary.DiaryComplexity
import com.zenyte.game.content.achievementdiary.diaries.FremennikDiary
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

class DagannothRexDroptable : NPCDropTableScript() {

    init {
        npcs(DAGANNOTH_REX, DAGANNOTH_REX_6498)

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
                STEEL_KITESHIELD quantity 1 rarity 5
                MITHRIL_WARHAMMER quantity 1 rarity 3
                ADAMANT_AXE quantity 1 rarity 4
                STEEL_PLATEBODY quantity 1 rarity 2
                MITHRIL_PICKAXE quantity 1 rarity 2
                ADAMANT_PLATEBODY quantity 1 rarity 2
                FREMENNIK_BLADE quantity 1 rarity 2
                DRAGON_AXE quantity 1 rarity 1
                RUNE_AXE quantity 1 rarity 1
                FREMENNIK_SHIELD quantity 1 rarity 1
                FREMENNIK_HELM quantity 1 rarity 1
                MITHRIL_2H_SWORD quantity 1 rarity 1
                RING_OF_LIFE quantity 1 rarity 1
                ROCKSHELL_PLATE quantity 1 rarity 1
                ROCKSHELL_LEGS quantity 1 rarity 1
                BERSERKER_RING quantity 1 rarity 1
                WARRIOR_RING quantity 1 rarity 1
                // Potions
                ANTIFIRE_POTION2 quantity 1 rarity 1
                PRAYER_POTION2 quantity 1 rarity 1
                RESTORE_POTION2 quantity 1 rarity 1
                SUPER_ATTACK2 quantity 1 rarity 1
                SUPER_STRENGTH2 quantity 1 rarity 1
                SUPER_DEFENCE2 quantity 1 rarity 1
                ZAMORAK_BREW2 quantity 1 rarity 1
                // Ores and bars
                ItemId.MITHRIL_ORE quantity 25.noted rarity 5
                ADAMANTITE_BAR quantity 1 rarity 3
                ItemId.COAL quantity 100.noted rarity 2
                ItemId.IRON_ORE quantity 150.noted rarity 1
                STEEL_BAR quantity (15..30).noted rarity 1
                chance(1) roll TalismanDropTable
                // Other
                COINS_995 quantity (100..1209) rarity 5
                GRIMY_RANARR_WEED quantity 1 rarity 3
                BASS quantity 5 rarity 5
                SWORDFISH quantity 5 rarity 4
                ItemId.SHARK quantity 5 rarity 1
                chance(8) roll RareDropTable
                chance(10) roll GemDropTable
            }
            Tertiary {
                ENSOULED_DAGANNOTH_HEAD quantity 1 oneIn 20
            }
        }
    }
}

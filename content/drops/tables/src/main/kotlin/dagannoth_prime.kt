package com.zenyte.game.content

import com.near_reality.scripts.npc.drops.table.always
import com.near_reality.scripts.npc.drops.table.noted
import com.near_reality.scripts.npc.drops.table.tables.gem.GemDropTable
import com.near_reality.scripts.npc.drops.table.tables.misc.TalismanDropTable
import com.near_reality.scripts.npc.drops.table.tables.rare.RareDropTable
import com.near_reality.scripts.npc.drops.table.tables.seed.RareSeedDropTable
import com.zenyte.game.content.achievementdiary.DiaryComplexity
import com.zenyte.game.content.achievementdiary.diaries.FremennikDiary
import com.near_reality.scripts.npc.drops.NPCDropTableScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.near_reality.game.util.invoke
import com.zenyte.game.item.ids.*
import com.zenyte.game.item.ids.SHARK
import com.near_reality.scripts.npc.drops.table.DropTableType.*
import com.zenyte.game.world.entity.npc.drop.matrix.Drop
import com.zenyte.game.world.entity.npc.drop.matrix.Drop.GUARANTEED_RATE
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor.PredicatedDrop
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor.DisplayedDrop

class DagannothPrimeDroptable : NPCDropTableScript() {

    init {
        npcs(DAGANNOTH_PRIME, DAGANNOTH_PRIME_6497)

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
                EARTH_BATTLESTAFF quantity 1 rarity 3
                WATER_BATTLESTAFF quantity 1 rarity 2
                AIR_BATTLESTAFF quantity 1 rarity 2
                BATTLESTAFF quantity (1..10).noted rarity 1
                FREMENNIK_BLADE quantity 1 rarity 1
                FREMENNIK_SHIELD quantity 1 rarity 1
                FREMENNIK_HELM quantity 1 rarity 1
                MUD_BATTLESTAFF quantity 1 rarity 1
                DRAGON_AXE quantity 1 rarity 1
                FARSEER_HELM quantity 1 rarity 1
                SKELETAL_TOP quantity 1 rarity 1
                SKELETAL_BOTTOMS quantity 1 rarity 1
                SEERS_RING quantity 1 rarity 1
                // Runes
                AIR_RUNE quantity 100..200 rarity 3
                EARTH_RUNE quantity 50..100 rarity 2
                BLOOD_RUNE quantity 25..75 rarity 2
                LAW_RUNE quantity 10..75 rarity 2
                NATURE_RUNE quantity 25..75 rarity 2
                MUD_RUNE quantity 25..75 rarity 2
                DEATH_RUNE quantity 25..85 rarity 2
                // Seeds
                chance(7) roll RareSeedDropTable
                // Talismans
                chance(1) roll TalismanDropTable
                // Talismans (noted)
                EARTH_TALISMAN quantity (25..75).noted rarity 5
                AIR_TALISMAN quantity (25..75).noted rarity 3
                WATER_TALISMAN quantity (1..76).noted rarity 3
                // Other
                SHARK quantity 5 rarity 4
                COINS_995 quantity (500..1109) rarity 3
                OYSTER_PEARLS quantity 1 rarity 3
                PURE_ESSENCE quantity 150.noted rarity 3
                GRIMY_RANARR_WEED quantity 1 rarity 3
                // Rare and Gem drop table
                chance(8) roll RareDropTable
                chance(10) roll GemDropTable
            }
            Tertiary {
                ENSOULED_DAGANNOTH_HEAD quantity 1 oneIn 20
            }
        }
    }
}

import com.near_reality.scripts.npc.drops.table.always
import com.near_reality.scripts.npc.drops.table.noted
import com.near_reality.scripts.npc.drops.NPCDropTableScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.near_reality.game.util.invoke
import com.zenyte.game.item.ids.*
import com.zenyte.game.item.ids.BONES
import com.near_reality.scripts.npc.drops.table.DropTableType.*
import com.zenyte.game.world.entity.npc.drop.matrix.Drop
import com.zenyte.game.world.entity.npc.drop.matrix.Drop.GUARANTEED_RATE
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor.PredicatedDrop
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor.DisplayedDrop

class UndeadDruidDroptable : NPCDropTableScript() {

    init {
        npcs(UNDEAD_DRUID)

        onDeath {
            rollStaticTableAndDrop(killer, Always)
            rollStaticTableAndDrop(killer, Main)
            rollStaticTableAndDrop(killer, Tertiary)
        }

        buildTable {
            Always {
                BONES quantity 1 rarity always
            }

            Main {
                // Weapons and Armor
                AIR_BATTLESTAFF quantity 1 oneIn 50
                EARTH_BATTLESTAFF quantity 1 oneIn 50
                MASK_OF_RANUL quantity 1 oneIn 1000
                // Runes
                AIR_RUNE quantity 200..300 oneIn 20
                EARTH_RUNE quantity 200..300 oneIn 33
                BLOOD_RUNE quantity 20..30 oneIn 33
                CHAOS_RUNE quantity 50..80 oneIn 33
                COSMIC_RUNE quantity 20..30 oneIn 33
                DEATH_RUNE quantity 20..30 oneIn 33
                NATURE_RUNE quantity 20..30 oneIn 33
                MUD_RUNE quantity 30..70 oneIn 33
                LAW_RUNE quantity 10..20 oneIn 50
                // Herbs
                GRIMY_GUAM_LEAF quantity 1..3 oneIn 18
                GRIMY_MARRENTILL quantity 1..3 oneIn 24
                GRIMY_TARROMIN quantity 1..3 oneIn 32
                GRIMY_HARRALANDER quantity 1..3 oneIn 41
                GRIMY_RANARR_WEED quantity 1..3 oneIn 53
                GRIMY_IRIT_LEAF quantity 1..3 oneIn 73
                GRIMY_AVANTOE quantity 1..3 oneIn 97
                GRIMY_KWUARM quantity 1..3 oneIn 116
                GRIMY_CADANTINE quantity 1..3 oneIn 146
                GRIMY_LANTADYME quantity 1..3 oneIn 194
                GRIMY_DWARF_WEED quantity 1..3 oneIn 194
                // Seeds
                LIMPWURT_SEED quantity 1 oneIn 64
                STRAWBERRY_SEED quantity 1 oneIn 67
                MARRENTILL_SEED quantity 1 oneIn 70
                JANGERBERRY_SEED quantity 1 oneIn 94
                TARROMIN_SEED quantity 1 oneIn 102
                WILDBLOOD_SEED quantity 1 oneIn 105
                WATERMELON_SEED quantity 1 oneIn 139
                HARRALANDER_SEED quantity 1 oneIn 156
                SNAPE_GRASS_SEED quantity 3 oneIn 218
                RANARR_SEED quantity 1 oneIn 224
                WHITEBERRY_SEED quantity 1 oneIn 257
                MUSHROOM_SPORE quantity 1 oneIn 301
                TOADFLAX_SEED quantity 1 oneIn 324
                BELLADONNA_SEED quantity 1 oneIn 485
                IRIT_SEED quantity 1 oneIn 485
                POISON_IVY_SEED quantity 1 oneIn 672
                AVANTOE_SEED quantity 1 oneIn 728
                CACTUS_SEED quantity 1 oneIn 728
                KWUARM_SEED quantity 1 oneIn 970
                POTATO_CACTUS_SEED quantity 1 oneIn 1092
                SNAPDRAGON_SEED quantity 1 oneIn 1747
                CADANTINE_SEED quantity 1 oneIn 2183
                LANTADYME_SEED quantity 1 oneIn 2911
                DWARF_WEED_SEED quantity 1 oneIn 4367
                TORSTOL_SEED quantity 1 oneIn 8733
                // Rare
                TOADFLAX_SEED quantity 1 oneIn 251
                IRIT_SEED quantity 1 oneIn 367
                BELLADONNA_SEED quantity 1 oneIn 380
                POISON_IVY_SEED quantity 1 oneIn 536
                AVANTOE_SEED quantity 1 oneIn 536
                CACTUS_SEED quantity 1 oneIn 561
                POTATO_CACTUS_SEED quantity 1 oneIn 787
                KWUARM_SEED quantity 1 oneIn 787
                SNAPDRAGON_SEED quantity 1 oneIn 1180
                CADANTINE_SEED quantity 1 oneIn 1686
                LANTADYME_SEED quantity 1 oneIn 2360
                SNAPE_GRASS_SEED quantity 3 oneIn 2950
                DWARF_WEED_SEED quantity 1 oneIn 3933
                TORSTOL_SEED quantity 1 oneIn 5900
                // Materials
                EYE_OF_NEWT quantity (25..30).noted oneIn 50
                POTATO_CACTUS quantity (10..15).noted oneIn 50
                WHITE_BERRIES quantity (10..15).noted oneIn 50
                WINE_OF_ZAMORAK quantity (5..8).noted oneIn 50
                // Other
                COINS_995 quantity 1000..5000 oneIn 17
                AMULET_OF_DEFENCE quantity 1 oneIn 20
                AMULET_OF_MAGIC quantity 1 oneIn 20
                AMULET_OF_STRENGTH quantity 1 oneIn 20
            }

            Tertiary {
                TATTERED_MOON_PAGE quantity 1 oneIn 20
                TATTERED_SUN_PAGE quantity 1 oneIn 20
                TATTERED_TEMPLE_PAGE quantity 1 oneIn 20
                GRUBBY_KEY quantity 1 oneIn 75
                SCROLL_BOX_ELITE quantity 1 oneIn 100
                ZOMBIE_CHAMPION_SCROLL quantity 1 oneIn 5000
            }
        }
    }
}

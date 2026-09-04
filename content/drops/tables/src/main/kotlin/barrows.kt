package com.zenyte.game.content.minigame.barrows

import com.near_reality.scripts.npc.drops.table.dsl.StandaloneDropTableBuilder
import com.near_reality.scripts.npc.drops.table.noted
import com.near_reality.scripts.npc.drops.table.tables.gem.GemDropTable
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

class BarrowsDroptable : NPCDropTableScript() {

    object BarrowsUniques : StandaloneDropTableBuilder({
        limit = 82
        static {
            AHRIMS_HOOD quantity 1 rarity 20 onlyDroppedBy AHRIM_THE_BLIGHTED
            AHRIMS_ROBESKIRT quantity 1 rarity 20 onlyDroppedBy AHRIM_THE_BLIGHTED
            AHRIMS_STAFF quantity 1 rarity 20 onlyDroppedBy AHRIM_THE_BLIGHTED
            AHRIMS_ROBETOP quantity 1 rarity 20 onlyDroppedBy AHRIM_THE_BLIGHTED

            GUTHANS_WARSPEAR quantity 1 rarity 20 onlyDroppedBy GUTHAN_THE_INFESTED
            GUTHANS_CHAINSKIRT quantity 1 rarity 20 onlyDroppedBy GUTHAN_THE_INFESTED
            GUTHANS_HELM quantity 1 rarity 20 onlyDroppedBy GUTHAN_THE_INFESTED
            GUTHANS_PLATEBODY quantity 1 rarity 20 onlyDroppedBy GUTHAN_THE_INFESTED

            KARILS_COIF quantity 1 rarity 20 onlyDroppedBy KARIL_THE_TAINTED
            KARILS_CROSSBOW quantity 1 rarity 20 onlyDroppedBy KARIL_THE_TAINTED
            KARILS_LEATHERTOP quantity 1 rarity 20 onlyDroppedBy KARIL_THE_TAINTED
            KARILS_LEATHERSKIRT quantity 1 rarity 20 onlyDroppedBy KARIL_THE_TAINTED

            VERACS_BRASSARD quantity 1 rarity 20 onlyDroppedBy VERAC_THE_DEFILED
            VERACS_FLAIL quantity 1 rarity 20 onlyDroppedBy VERAC_THE_DEFILED
            VERACS_HELM quantity 1 rarity 20 onlyDroppedBy VERAC_THE_DEFILED
            VERACS_PLATESKIRT quantity 1 rarity 20 onlyDroppedBy VERAC_THE_DEFILED

            DHAROKS_GREATAXE quantity 1 rarity 20 onlyDroppedBy DHAROK_THE_WRETCHED
            DHAROKS_HELM quantity 1 rarity 20 onlyDroppedBy DHAROK_THE_WRETCHED
            DHAROKS_PLATEBODY quantity 1 rarity 20 onlyDroppedBy DHAROK_THE_WRETCHED
            DHAROKS_PLATELEGS quantity 1 rarity 20 onlyDroppedBy DHAROK_THE_WRETCHED

            TORAGS_HAMMERS quantity 1 rarity 20 onlyDroppedBy TORAG_THE_CORRUPTED
            TORAGS_HELM quantity 1 rarity 20 onlyDroppedBy TORAG_THE_CORRUPTED
            TORAGS_PLATELEGS quantity 1 rarity 20 onlyDroppedBy TORAG_THE_CORRUPTED
            TORAGS_PLATEBODY quantity 1 rarity 20 onlyDroppedBy TORAG_THE_CORRUPTED

        }
    })

    init {
        npcs(GUTHAN_THE_INFESTED, AHRIM_THE_BLIGHTED, KARIL_THE_TAINTED, VERAC_THE_DEFILED, DHAROK_THE_WRETCHED, TORAG_THE_CORRUPTED)

        buildTable(650) {
            Main {
                // Runes
                AIR_RUNE quantity 40..70 rarity 35
                ASTRAL_RUNE quantity 38..98 rarity 75
                BLOOD_RUNE quantity 25 rarity 10
                LAVA_RUNE quantity 30..60 rarity 45
                DEATH_RUNE quantity 25 rarity 20
                MUD_RUNE quantity 40..70 rarity 35
                SMOKE_RUNE quantity 100..150 rarity 10
                SOUL_RUNE quantity 25 rarity 15
                // Herbs
                GRIMY_AVANTOE quantity 1 rarity 15
                GRIMY_RANARR_WEED quantity 1 rarity 13
                GRIMY_SNAPDRAGON quantity 1 rarity 13
                GRIMY_TORSTOL quantity 1 rarity 45
                // Coins
                COINS_995 quantity 1300..1337 rarity 30
                COINS_995 quantity 6900..6942 rarity 10
                // Potions
                SUPER_DEFENCE2 quantity 1 rarity 10
                SUPER_RESTORE1 quantity 1 rarity 45
                SUPER_ATTACK2 quantity 1 rarity 55
                SUPER_DEFENCE3 quantity 1 rarity 20
                // Other
                ADAMANTITE_BAR quantity (1..4).noted rarity 40
                BLOOD_ESSENCE quantity 1 rarity 5
                ItemId.COAL quantity (1..10).noted rarity 40
                PURE_ESSENCE quantity 46.noted rarity 40
                BOLT_RACK quantity 30..50 rarity 75
                // Gem
                chance(5) roll GemDropTable
            }
            Unique {
                chance(20) roll BarrowsUniques
            }
            Tertiary {
                SCROLL_BOX_HARD quantity 1 rarity 30
            }
        }
    }
}

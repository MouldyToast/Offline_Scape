package com.near_reality.game.content.boss.nex.npc.drops

import com.near_reality.scripts.npc.drops.table.noted
import com.near_reality.scripts.npc.drops.table.tables.gem.GemDropTable
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
import com.zenyte.game.item.ItemId.ADAMANT_BOLTS
import com.zenyte.game.item.ItemId.ANCIENT_CEREMONIAL_BOOTS
import com.zenyte.game.item.ItemId.ANCIENT_CEREMONIAL_GLOVES
import com.zenyte.game.item.ItemId.ANCIENT_CEREMONIAL_LEGS
import com.zenyte.game.item.ItemId.ANCIENT_CEREMONIAL_MASK
import com.zenyte.game.item.ItemId.ANCIENT_CEREMONIAL_TOP
import com.zenyte.game.item.ItemId.BLUE_DRAGON_SCALE
import com.zenyte.game.item.ItemId.CHAOS_RUNE
import com.zenyte.game.item.ItemId.CLUE_SCROLL_HARD
import com.zenyte.game.item.ItemId.COINS_995
import com.zenyte.game.item.ItemId.GREEN_DHIDE_BODY
import com.zenyte.game.item.ItemId.GRIMY_AVANTOE
import com.zenyte.game.item.ItemId.GRIMY_RANARR_WEED
import com.zenyte.game.item.ItemId.GRIMY_SNAPDRAGON
import com.zenyte.game.item.ItemId.GRIMY_TORSTOL
import com.zenyte.game.item.ItemId.MITHRIL_LONGSWORD
import com.zenyte.game.item.ItemId.NATURE_RUNE
import com.zenyte.game.item.ItemId.NIHIL_SHARD
import com.zenyte.game.item.ItemId.PRAYER_POTION2
import com.zenyte.game.item.ItemId.PURE_ESSENCE
import com.zenyte.game.item.ItemId.RANGING_POTION2
import com.zenyte.game.item.ItemId.RUNE_ARROW
import com.zenyte.game.item.ItemId.SUPER_DEFENCE1
import com.zenyte.game.item.ItemId.UNCUT_DIAMOND

class ZarosSpiritualRangerDroptable : NPCDropTableScript() {

    init {
        npcs(SPIRITUAL_RANGER_11291)

        buildTable(640) {
            Main {
                // Ancient ceremonial robes
                ANCIENT_CEREMONIAL_MASK quantity 1 rarity 1
                ANCIENT_CEREMONIAL_TOP quantity 1 rarity 1
                ANCIENT_CEREMONIAL_LEGS quantity 1 rarity 1
                ANCIENT_CEREMONIAL_GLOVES quantity 1 rarity 1
                ANCIENT_CEREMONIAL_BOOTS quantity 1 rarity 1
                // Runes and ammunition
                ADAMANT_BOLTS quantity 15 rarity 35
                CHAOS_RUNE quantity 1 rarity 15
                NATURE_RUNE quantity 1 rarity 20
                RUNE_ARROW quantity 12 rarity 10
                RUNE_ARROW quantity 50 rarity 10
                // Herbs
                GRIMY_AVANTOE quantity 1 rarity 15
                GRIMY_RANARR_WEED quantity 1 rarity 13
                GRIMY_SNAPDRAGON quantity 1 rarity 13
                GRIMY_TORSTOL quantity 1 rarity 6
                // Coins
                COINS_995 quantity 400..500 rarity 45
                COINS_995 quantity 1300..1337 rarity 30
                // Potions
                PRAYER_POTION2 quantity 1 rarity 45
                RANGING_POTION2 quantity 1 rarity 55
                SUPER_DEFENCE1 quantity 1 rarity 10
                // Other
                ADAMANTITE_BAR quantity (1..4).noted rarity 40
                BLUE_DRAGON_SCALE quantity 2.noted rarity 35
                ItemId.COAL quantity (1..10).noted rarity 40
                GREEN_DHIDE_BODY quantity 1 rarity 35
                MITHRIL_LONGSWORD quantity 1 rarity 75
                NIHIL_SHARD quantity 2..5 rarity 5
                PURE_ESSENCE quantity 23.noted rarity 40
                UNCUT_DIAMOND quantity 1 rarity 20
                ItemId.SHARK quantity 1 rarity 5
                // Gem
                chance(5) roll GemDropTable
            }
            Tertiary {
                CLUE_SCROLL_HARD quantity 1 rarity 5
            }
        }
    }
}

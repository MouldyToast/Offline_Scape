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
import com.zenyte.game.item.ItemId.ADAMANT_CHAINBODY
import com.zenyte.game.item.ItemId.AIR_RUNE
import com.zenyte.game.item.ItemId.ANCIENT_CEREMONIAL_BOOTS
import com.zenyte.game.item.ItemId.ANCIENT_CEREMONIAL_GLOVES
import com.zenyte.game.item.ItemId.ANCIENT_CEREMONIAL_LEGS
import com.zenyte.game.item.ItemId.ANCIENT_CEREMONIAL_MASK
import com.zenyte.game.item.ItemId.ANCIENT_CEREMONIAL_TOP
import com.zenyte.game.item.ItemId.BLOOD_ESSENCE
import com.zenyte.game.item.ItemId.CHAOS_RUNE
import com.zenyte.game.item.ItemId.CLUE_SCROLL_HARD
import com.zenyte.game.item.ItemId.COINS_995
import com.zenyte.game.item.ItemId.GRIMY_AVANTOE
import com.zenyte.game.item.ItemId.GRIMY_RANARR_WEED
import com.zenyte.game.item.ItemId.GRIMY_SNAPDRAGON
import com.zenyte.game.item.ItemId.GRIMY_TORSTOL
import com.zenyte.game.item.ItemId.LOBSTER
import com.zenyte.game.item.ItemId.MIND_RUNE
import com.zenyte.game.item.ItemId.MUD_RUNE
import com.zenyte.game.item.ItemId.NATURE_RUNE
import com.zenyte.game.item.ItemId.NIHIL_SHARD
import com.zenyte.game.item.ItemId.POTATO_CACTUS
import com.zenyte.game.item.ItemId.PRAYER_POTION2
import com.zenyte.game.item.ItemId.PURE_ESSENCE
import com.zenyte.game.item.ItemId.SUPER_ATTACK1
import com.zenyte.game.item.ItemId.SUPER_DEFENCE1
import com.zenyte.game.item.ItemId.SUPER_STRENGTH1

class ZarosSpiritualWarriorDroptable : NPCDropTableScript() {

    init {
        npcs(SPIRITUAL_WARRIOR_11290)

        buildTable(640) {
            Main {
                // Ancient ceremonial robes
                ANCIENT_CEREMONIAL_MASK quantity 1 rarity  1
                ANCIENT_CEREMONIAL_TOP quantity 1 rarity  1
                ANCIENT_CEREMONIAL_LEGS quantity 1 rarity  1
                ANCIENT_CEREMONIAL_GLOVES quantity 1 rarity  1
                ANCIENT_CEREMONIAL_BOOTS quantity 1 rarity  1
                // Runes
                AIR_RUNE quantity 150 rarity 10
                CHAOS_RUNE quantity 1 rarity 15
                MIND_RUNE quantity 1 rarity 35
                MUD_RUNE quantity 15 rarity 35
                NATURE_RUNE quantity 1 rarity 20
                // Herbs
                GRIMY_AVANTOE quantity 1 rarity 15
                GRIMY_RANARR_WEED quantity 1 rarity 13
                GRIMY_SNAPDRAGON quantity 1 rarity 13
                GRIMY_TORSTOL quantity 1 rarity 6
                // Coins
                COINS_995 quantity 400..499 rarity 45
                COINS_995 quantity 1300..1337 rarity 30
                // Potions
                PRAYER_POTION2 quantity 1 rarity 45
                SUPER_ATTACK1 quantity  1 rarity 55
                SUPER_DEFENCE1 quantity 1 rarity 10
                SUPER_STRENGTH1 quantity 1 rarity 10
                // Other
                ADAMANT_CHAINBODY quantity 1 rarity 35
                ADAMANTITE_BAR quantity (1..4).noted rarity 40
                BLOOD_ESSENCE quantity 1 rarity 5
                ItemId.COAL quantity (1..10).noted rarity 40
                LOBSTER quantity 1 rarity 5
                NIHIL_SHARD quantity 2..5 rarity 5
                POTATO_CACTUS quantity 1 rarity 35
                PURE_ESSENCE quantity 23.noted rarity 40
                // Gem
                chance(5) roll GemDropTable
            }
            Tertiary {
                CLUE_SCROLL_HARD quantity 1 rarity 5
            }
        }
    }
}

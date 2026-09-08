package com.near_reality.game.content.boss.nex.npc.drops

import com.near_reality.scripts.npc.drops.table.DropTableType.*
import com.near_reality.scripts.npc.drops.table.noted
import com.near_reality.scripts.npc.drops.table.tables.gem.GemDropTable
import com.near_reality.scripts.npc.drops.NPCDropTableScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.near_reality.game.util.invoke
import com.zenyte.game.item.ItemId
import com.zenyte.game.world.entity.npc.drop.matrix.Drop
import com.zenyte.game.world.entity.npc.drop.matrix.Drop.GUARANTEED_RATE
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor.PredicatedDrop
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor.DisplayedDrop
import com.zenyte.game.item.ItemId.ADAMANTITE_BAR
import com.zenyte.game.item.ItemId.AIR_RUNE
import com.zenyte.game.item.ItemId.ANCIENT_BREW_3
import com.zenyte.game.item.ItemId.ANCIENT_CEREMONIAL_BOOTS
import com.zenyte.game.item.ItemId.ANCIENT_CEREMONIAL_GLOVES
import com.zenyte.game.item.ItemId.ANCIENT_CEREMONIAL_LEGS
import com.zenyte.game.item.ItemId.ANCIENT_CEREMONIAL_MASK
import com.zenyte.game.item.ItemId.ANCIENT_CEREMONIAL_TOP
import com.zenyte.game.item.ItemId.ASTRAL_RUNE
import com.zenyte.game.item.ItemId.BLOOD_ESSENCE
import com.zenyte.game.item.ItemId.BLOOD_RUNE
import com.zenyte.game.item.ItemId.CLUE_SCROLL_HARD
import com.zenyte.game.item.ItemId.COINS_995
import com.zenyte.game.item.ItemId.DEATH_RUNE
import com.zenyte.game.item.ItemId.DRAGON_BOOTS
import com.zenyte.game.item.ItemId.GRIMY_AVANTOE
import com.zenyte.game.item.ItemId.GRIMY_RANARR_WEED
import com.zenyte.game.item.ItemId.GRIMY_SNAPDRAGON
import com.zenyte.game.item.ItemId.GRIMY_TORSTOL
import com.zenyte.game.item.ItemId.LAVA_RUNE
import com.zenyte.game.item.ItemId.MUD_RUNE
import com.zenyte.game.item.ItemId.NIHIL_SHARD
import com.zenyte.game.item.ItemId.PURE_ESSENCE
import com.zenyte.game.item.ItemId.SMOKE_RUNE
import com.zenyte.game.item.ItemId.SOUL_RUNE
import com.zenyte.game.item.ItemId.SUPER_DEFENCE3
import com.zenyte.game.item.ItemId.SUPER_RESTORE3

class ZarosSpiritualMageDroptable : NPCDropTableScript() {

    init {
        npcs(SPIRITUAL_MAGE_11292)

        buildTable(640) {
            Main {
                // Ancient ceremonial robes
                ANCIENT_CEREMONIAL_MASK quantity 1 rarity 1
                ANCIENT_CEREMONIAL_TOP quantity 1 rarity 1
                ANCIENT_CEREMONIAL_LEGS quantity 1 rarity 1
                ANCIENT_CEREMONIAL_GLOVES quantity 1 rarity 1
                ANCIENT_CEREMONIAL_BOOTS quantity 1 rarity 1
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
                SUPER_DEFENCE3 quantity 1 rarity 10
                SUPER_RESTORE3 quantity 1 rarity 45
                ANCIENT_BREW_3 quantity 1 rarity 55
                ANCIENT_BREW_3 quantity 1 rarity 20
                // Other
                ADAMANTITE_BAR quantity (1..4).noted rarity 40
                BLOOD_ESSENCE quantity 1 rarity 5
                ItemId.COAL quantity (1..10).noted rarity 40
                NIHIL_SHARD quantity 5..9 rarity 5
                PURE_ESSENCE quantity 46.noted rarity 40
                // Gem
                chance(5) roll GemDropTable
            }
            Tertiary {
                CLUE_SCROLL_HARD quantity 1 rarity 7
                DRAGON_BOOTS quantity 1 rarity 5
            }
        }
    }
}

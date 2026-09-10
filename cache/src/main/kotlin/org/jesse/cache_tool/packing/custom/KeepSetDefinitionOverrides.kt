package org.jesse.cache_tool.packing.custom

import org.jesse.game.item.ids.ELDER_MAUL_OR
import org.jesse.game.obj.ids.DUKE_SCOREBOARD
import org.jesse.game.obj.ids.PHANTOM_MUSPAH_SCOREBOARD
import org.jesse.game.obj.ids.ZAMORAK_PORTAL
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.player.container.impl.ContainerType
import org.jesse.game.world.`object`.WorldObject
import org.jesse.game.world.region.Regions
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import mgi.tools.parser.TypeParser
import mgi.types.component.ComponentDefinitions
import mgi.types.config.InventoryDefinitions
import mgi.types.config.ObjectDefinitions
import mgi.types.config.enums.EnumDefinitions
import mgi.types.config.items.ItemDefinitions
import mgi.types.config.npcs.NPCDefinitions
import java.util.function.Predicate

/**
 * The minimal set of cache definition overrides that survive the removal of
 * the Near Reality custom packers (Stage 5c). Each entry is here because
 * kept server content depends on it; everything else was reverted to vanilla.
 */
object KeepSetDefinitionOverrides {

    /**
     * Item, NPC and inventory definition overrides. Runs where the custom
     * item packer used to run, before the main definition pack pass.
     */
    @JvmStatic
    fun pack() {
        // The collection log container (inv 620) is 500 slots in vanilla;
        // with all post-228 content loaded, players exceed that. Shrinking
        // it would silently truncate existing collection log saves.
        InventoryDefinitions.get(ContainerType.COLLECTION_LOG.id)?.apply {
            size = 2500
            pack()
        }
        // Vanilla rev-228 inv 169 is 3 slots; the divine rune pouch code
        // (RuneOnRunePouchItemAction, RunePouchInterface) requires 4, and
        // shrinking would drop runes from saved pouches.
        InventoryDefinitions.get(ContainerType.RUNE_POUCH.id)?.apply {
            size = 4
            pack()
        }
        // The slumbering Duke Sucellus (NPC 12166) has no options in vanilla;
        // the boss fight is initiated through this Attack option.
        NPCDefinitions.get(12166).apply {
            options[1] = "Attack"
            pack()
        }
        packSpellbookTeleportKeys()
        packItemOverrides()
        packNpcOverrides()
    }

    /**
     * The custom teleport interface keys its categories off item params 601
     * (category name) and 602 (description) on the vanilla spellbook teleport
     * tablet items; the vanilla teleport-destination params are stripped so
     * they don't double-key (SpellDefinitions.java and the teleport-1700
     * system rely on these).
     */
    private fun packSpellbookTeleportKeys() {
        keySpellbookItems(intArrayOf(3286, 4631, 9111, 20409), "Training")
        keySpellbookItems(intArrayOf(3289, 4634, 20759, 20410), "Skilling")
        keySpellbookItems(intArrayOf(3292, 4637, 9114, 20411), "Minigames")
        keySpellbookItems(intArrayOf(3296, 4640, 9117, 20412), "Wilderness")
        keySpellbookItems(intArrayOf(3301, 4643, 9120, 20413), "Bosses")
        keySpellbookItems(intArrayOf(3306, 4646, 9127, 20414), "Dungeons")
        keySpellbookItems(intArrayOf(3312, 4649, 9129, 20419), "Cities")
        keySpellbookItems(intArrayOf(21836, 4652, 9131, 20420), "Misc")
    }

    private val REMOVED_SPELL_PARAMS = intArrayOf(365, 367, 369, 606, 366, 368, 370, 607)

    private fun keySpellbookItems(ids: IntArray, category: String) {
        for (id in ids) {
            ItemDefinitions.get(id).apply {
                val params = parameters ?: Int2ObjectOpenHashMap<Any>().also { parameters = it }
                params.put(601, "$category Teleports")
                params.put(602, "Opens the teleport interface in the $category category.")
                for (key in REMOVED_SPELL_PARAMS) {
                    params.remove(key)
                }
                pack()
            }
        }
    }

    /**
     * Item definition overrides for kept custom content. New-id items are
     * Kryo copies of their source definition, exactly as the old TOML
     * item reader produced them.
     */
    private fun packItemOverrides() {
        // Enhanced ice gloves (IceGloves content) - copied before the 1580
        // edit below, matching the old TOML parse of a pristine source.
        TypeParser.KRYO.copy(ItemDefinitions.get(1580)).apply {
            id = 30030
            name = "Enhanced ice gloves"
            isGrandExchange = true
            replacementColours = shortArrayOf(32511, 32511)
            setOption(0, "Wear")
            setOption(1, "")
            pack()
        }
        // Ice gloves keep only their Wear option.
        ItemDefinitions.get(1580).apply {
            setOption(0, "Wear")
            setOption(1, "")
            pack()
        }
        // Pet mystery box (PetMysteryBox content).
        TypeParser.KRYO.copy(ItemDefinitions.get(6199)).apply {
            id = 30031
            name = "Pet mystery box"
            isGrandExchange = true
            placeholderId = 30032
            replacementColours = shortArrayOf(0)
            setOption(1, "Quick-Open")
            pack()
        }
        // Pet mystery box placeholder.
        TypeParser.KRYO.copy(ItemDefinitions.get(18086)).apply {
            id = 30032
            name = "null"
            placeholderId = 30031
            replacementColours = shortArrayOf(0)
            pack()
        }
        // Pet mystery box rewards gain a Drop option and GE tradeability.
        for (id in intArrayOf(20838, 20840, 20842, 20844, 11863, 12887, 12888, 12889,
                              12890, 12891, 11021, 11019, 11020, 11022)) {
            ItemDefinitions.get(id).apply {
                isGrandExchange = true
                setOption(4, "Drop")
                pack()
            }
        }
        // Tome of experience (NewCrystalChestLoot.kt).
        TypeParser.KRYO.copy(ItemDefinitions.get(22415)).apply {
            id = 30215
            price = 999
            setIsStackable(1)
            placeholderId = 30216
            placeholderTemplate = -1
            setOption(1, "Read-All")
            pack()
        }
        // Tome of experience placeholder.
        TypeParser.KRYO.copy(ItemDefinitions.get(14577)).apply {
            id = 30216
            placeholderTemplate = 14401
            placeholderId = 30215
            pack()
        }
        // Smouldering demon pet item (BossPet.java).
        TypeParser.KRYO.copy(ItemDefinitions.get(20023)).apply {
            id = 33250
            name = "Smouldering Demon"
            setOption(0, "")
            setOption(1, "")
            setOption(2, "")
            setOption(3, "")
            setOption(4, "Drop")
            pack()
        }
        // Clue scroll boxes (ClueItem.java + drop tables). The old TOML's
        // inventory models (60477-60482) were custom assets deleted long ago,
        // so the boxes borrow the vanilla scroll box art of the same tier.
        scrollBox(2803, "beginner", 24361, 30218)
        scrollBox(2805, "easy", 24362, 30219)
        scrollBox(2807, "medium", 24363, 30220)
        scrollBox(2809, "hard", 24364, 30221)
        scrollBox(2811, "elite", 24365, 30222)
        scrollBox(2813, "master", 24366, 30223)
        // Royal seed pod gains its Configure option.
        ItemDefinitions.get(19564).apply {
            setOption(1, "Configure")
            pack()
        }
        // Rotten potato (staff tool).
        ItemDefinitions.get(5733).apply {
            setOption(0, "")
            setOption(1, "")
            setOption(2, "Punishment")
            setOption(3, "Utility")
            setOption(4, "Destroy")
            pack()
        }
        // Sled (Ride option).
        ItemDefinitions.get(4083).apply {
            isGrandExchange = true
            setOption(0, "Ride")
            setOption(1, "")
            setOption(2, "")
            setOption(3, "")
            setOption(4, "Drop")
            pack()
        }
        // Silverlight demon-check. (The old TOML's param 451 line had no
        // reader and is intentionally not carried over.)
        ItemDefinitions.get(2402).apply {
            setOption(0, "")
            setOption(1, "Wear")
            setOption(2, "Check")
            pack()
        }
        // Max cape variants: Features/Commune ops (MaxCape content). Only the
        // live op4/op5 entries of the old TOML survive - its op2 "Teleports"
        // was dead.
        for (id in intArrayOf(13329, 13331, 13333, 13335, 21285, 21776, 21780, 21784,
                              24133, 24134, 24232, 24233, 24234)) {
            ItemDefinitions.get(id).apply {
                setOption(3, "Features")
                pack()
            }
        }
        for (id in intArrayOf(13337, 21898, 24135)) {
            ItemDefinitions.get(id).apply {
                setOption(3, "Commune")
                setOption(4, "Features")
                pack()
            }
        }
        // Ardougne max cape.
        ItemDefinitions.get(20760).apply {
            setOption(3, "Farm Teleport")
            setOption(4, "Features")
            pack()
        }
        // Slayer casket (clue reward casket 7956 repurposed as slayer loot).
        ItemDefinitions.get(7956).apply {
            name = "Slayer casket"
            setIsStackable(1)
            isGrandExchange = false
            isMembers = true
            pack()
        }
    }

    /**
     * A tiered clue scroll box (stackable clue holder) and its bank
     * placeholder.
     */
    private fun scrollBox(boxId: Int, tier: String, vanillaBoxId: Int, placeholder: Int) {
        ItemDefinitions.get(boxId).apply {
            name = "Scroll box ($tier)"
            setIsStackable(1)
            isGrandExchange = false
            isMembers = true
            price = 50
            shiftClickIndex = -2
            zoom = 770
            offsetX = 1
            offsetY = -6
            modelPitch = 236
            modelRoll = 1697
            modelYaw = 0
            inventoryModelId = ItemDefinitions.get(vanillaBoxId).inventoryModelId
            placeholderId = placeholder
            placeholderTemplate = -1
            setOption(0, "Open")
            setOption(1, "")
            setOption(4, "Drop")
            pack()
        }
        TypeParser.KRYO.copy(ItemDefinitions.get(14577)).apply {
            id = placeholder
            placeholderTemplate = 14401
            placeholderId = boxId
            pack()
        }
    }

    /**
     * NPC definition overrides for kept custom content. New-id NPCs are Kryo
     * copies of their source definition, exactly as the old TOML NPC reader
     * produced them.
     */
    private fun packNpcOverrides() {
        // Armoured zombies (region 11169 spawns + drop table).
        for (offset in 0..9) {
            TypeParser.KRYO.copy(NPCDefinitions.get(12720 + offset)).apply {
                id = 14113 + offset
                combatLevel = 109
                setOption(1, "Attack")
                pack()
            }
        }
        // Ashuelot Reis nurse banker (Bank/Collect are handled; the old
        // TOML's op4 "Presets" was dead and is dropped).
        NPCDefinitions.get(11289).apply {
            setOption(2, "Bank")
            setOption(4, "Collect")
            pack()
        }
        // Captain Errdo gnome glider quick-travel.
        NPCDefinitions.get(6088).apply {
            setOption(0, "Glider")
            setOption(2, "")
            pack()
        }
        // Captain Rimor CoX party layouts.
        NPCDefinitions.get(7595).apply {
            setOption(3, "Layouts")
            pack()
        }
        // Dying knight, non-collectable variant (InstancePortal.java).
        TypeParser.KRYO.copy(NPCDefinitions.get(5929)).apply {
            id = 16023
            direction = 32
            setOption(0, "Talk-to")
            setOption(1, "Collect")
            setOption(2, "")
            pack()
        }
        // Hagavik (Collect option).
        TypeParser.KRYO.copy(NPCDefinitions.get(8402)).apply {
            id = 16024
            name = "Hagavik"
            setOption(0, "Talk-to")
            setOption(1, "Collect")
            pack()
        }
        // TzHaar-Ket-Keh inferno practice mode.
        NPCDefinitions.get(7690).apply {
            setOption(0, "Talk-to")
            setOption(1, "Practice Mode")
            setOption(2, "")
            pack()
        }
        // Rise of the Six barrows brothers. (The old TOML's pet NPCs
        // 16045-16050 referenced dangling models and were dropped.)
        for (offset in 0..5) {
            TypeParser.KRYO.copy(NPCDefinitions.get(1672 + offset)).apply {
                id = 16035 + offset
                pack()
            }
        }
        // Smouldering demon pet NPC (BossPet.java). The old TOML's resizez
        // and familiar keys never mapped to a definition field and are not
        // carried over; the filteredops key set the array without touching
        // the filter flag, mirrored here via setFilteredOptions.
        NPCDefinitions.get(13602).apply {
            name = "Smouldering Demon"
            models = intArrayOf(53285)
            combatLevel = 0
            resizeX = 32
            resizeY = 32
            size = 1
            options = arrayOf(null, null, "Pick-up", null, null)
            setFilteredOptions(arrayOf(null, null, "Pick-up", null, null))
            isMinimapVisible = true
            pack()
        }
        // Sir Eldric, the PvM Arena supplies trader.
        NPCDefinitions.get(3516).apply {
            name = "Sir Eldric"
            setOption(0, "Talk-to")
            setOption(1, "Trade")
            pack()
        }
        // Vefari (vanilla rev-228 still names NPC 13677 "Weave"; the araxyte
        // cave hunt content refers to them as Vefari).
        NPCDefinitions.get(13677).apply {
            name = "Vefari"
            pack()
        }
    }

    /**
     * Object definition overrides. Runs where the custom objects packer used
     * to run, after the main definition pack pass.
     */
    @JvmStatic
    fun packObjects() {
        // Private portal used by GodwarsInstancePortal for GWD instances.
        TypeParser.cloneObject(ZAMORAK_PORTAL, 35015).apply {
            name = "Private portal"
            sizeX /= 2
            sizeY /= 2
            modelSizeX /= 2
            modelSizeY /= 2
            modelSizeHeight /= 2
            mapSceneId = 64
            setOption(0, "Use")
            pack()
        }
        // DT2 loot objects missing their Take option.
        ObjectDefinitions.get(47567).apply {
            setOption(1, "Take")
            pack()
        }
        ObjectDefinitions.get(47568).apply {
            setOption(1, "Take")
            pack()
        }
        // Magical wheat (Puro Puro spawns it as loc type 22 -
        // PuroPuroArea.java). The old TOML's cliptype/types keys never mapped
        // to a definition field; its real effect was the repack itself: this
        // encoder always re-emits the model list without loc types (opcode 5)
        // after the typed list, clearing the loc-type restriction so the
        // object renders at type 22. A plain repack reproduces that exactly.
        ObjectDefinitions.get(25016).pack()
        // GWD instance crevice exit (GodwarsInstancePortal).
        TypeParser.KRYO.copy(ObjectDefinitions.get(26769)).apply {
            id = 35013
            ambient = 25
            contrast = 500
            mapSceneId = -1
            pack()
        }
        // GWD private boss room portals (GodwarsInstancePortal).
        for ((from, to) in intArrayOf(26738, 9368, 20843, 26740)
                .zip(intArrayOf(50083, 35014, 35016, 35017))) {
            TypeParser.KRYO.copy(ObjectDefinitions.get(from)).apply {
                id = to
                name = "Private portal"
                mapSceneId = 64
                setOption(0, "Use")
                pack()
            }
        }
        // Saradomin encampment stepping stone in GWD instances.
        TypeParser.KRYO.copy(ObjectDefinitions.get(21120)).apply {
            id = 35018
            setOption(0, "Jump")
            pack()
        }
        // Nex red portal instance entrance.
        ObjectDefinitions.get(42941).apply {
            setOption(0, "Pass")
            setOption(2, "Peek")
            pack()
        }
        // Dagannoth Kings instance entrance crack.
        ObjectDefinitions.get(30169).apply {
            setOption(0, "Private")
            setOption(1, "Peek")
            pack()
        }
        // Dagannoth Kings ladder private-instance option.
        ObjectDefinitions.get(10230).apply {
            setOption(2, "Private")
            pack()
        }
        // Waterbirth dungeon roots no longer block projectiles.
        ObjectDefinitions.get(30170).apply {
            isProjectileClip = true
            pack()
        }
        // Giant Mole hill instance options.
        ObjectDefinitions.get(12202).apply {
            setOption(0, "Enter")
            setOption(1, "Public")
            setOption(2, "Private")
            pack()
        }
        // Thermonuclear smoke devil boss entrance crevice.
        ObjectDefinitions.get(535).apply {
            setOption(1, "Private")
            setOption(2, "Peek")
            pack()
        }
        // CoX entrance steps Reload option.
        ObjectDefinitions.get(29778).apply {
            setOption(1, "Reload")
            pack()
        }
        // Gauntlet reward chest no-prep entries. (The old TOML's 36080
        // Quick-Pass block was unhandled and is dropped.)
        ObjectDefinitions.get(36084).apply {
            setOption(2, "Normal-NoPrep")
            setOption(3, "Corrupted-NoPrep")
            pack()
        }
        // Catacombs of Kourend statue paid entry (KourendStatueObject.java).
        ObjectDefinitions.get(27785).apply {
            setOption(0, "Enter")
            setOption(1, "Enter-Paid")
            pack()
        }
        // Vardorvis instance rock (clone of the public one).
        TypeParser.KRYO.copy(ObjectDefinitions.get(48740)).apply {
            id = 48741
            pack()
        }
        // Raids repair hammer, injected into the maps by MapChanges.java.
        TypeParser.KRYO.copy(ObjectDefinitions.get(31634)).apply {
            id = 35020
            name = "<col=ff9040>Hammer</col>"
            models = intArrayOf(2376)
            pack()
        }
        // RDI bonfire (BonfireObject) - vanilla 29300 has no options at all.
        ObjectDefinitions.get(29300).apply {
            setOption(0, "Add-logs")
            pack()
        }
        // PvM Arena team portals.
        ObjectDefinitions.get(43765).apply {
            name = "<col=0000ff>Team portal blue</col>"
            setOption(0, "Enter")
            pack()
        }
        ObjectDefinitions.get(43767).apply {
            name = "<col=ff0000>Team portal red</col>"
            setOption(0, "Enter")
            replacementColours = intArrayOf(-3377, -1232, 639)
            pack()
        }
    }

    /**
     * Component definition overrides. Runs where the component TOMLs used to
     * pack, right after the main definitions pack pass (and before the
     * interface archive is finished).
     */
    @JvmStatic
    fun packComponents() {
        // Secondary home teleport destinations on the spellbook home
        // teleport buttons (SpellbookTeleport.java).
        for ((component, destination) in intArrayOf(4, 99, 143)
                .zip(arrayOf("Lumbridge", "Lunar Isle", "Arceuus"))) {
            ComponentDefinitions.get(218, component).apply {
                accessMask = 1030
                setOption(1, destination)
                pack()
            }
        }
        // XP Multiplier option on the XP orb (OrbsInterface.java).
        ComponentDefinitions.get(160, 5).apply {
            accessMask = 14
            setOption(2, "XP Multiplier")
            pack()
        }
        // Right-click "previous destination" teleport on the spellbook tab
        // of each gameframe pane (ResizablePaneInterface option 3). Vanilla
        // carries only ops 1-2 (mask 6) on these components.
        for ((pane, component) in intArrayOf(161, 164, 548).zip(intArrayOf(65, 58, 69))) {
            ComponentDefinitions.get(pane, component).apply {
                accessMask = 14
                setOption(2, "*")
                pack()
            }
        }
    }

    /**
     * Special attack metadata missing from the vanilla rev-228 enums.
     * Elder maul (or) is usable in game but vanilla only carries the
     * plain Elder maul entries.
     */
    @JvmStatic
    fun packSpecialAttacks() {
        EnumDefinitions.get(1739).apply {
            values[ELDER_MAUL_OR] = "Lowers the target's current Defence level by 35% on a successful hit. The effect is stackable and relative to the target's"
            pack()
        }
        EnumDefinitions.get(906).apply {
            values[ELDER_MAUL_OR] = 500
            pack()
        }
    }

    /**
     * Additive keys on the vanilla item retrieval-service enums for the kept
     * death-storage NPCs.
     */
    @JvmStatic
    fun packEnums() {
        // Item retrieval service entries for the kept death-storage NPCs
        // (dying knight 16023, Hagavik 16024, Rots) - additive keys on the
        // vanilla retrieval-service enums.
        EnumDefinitions.get(1753).apply {
            values[100] = "Dying Knight's salvage"
            values[101] = "Dying Knight's salvage"
            values[102] = "Hagavik's Item Retrieval Service"
            values[103] = "Hagavik's Item Retrieval Service"
            values[104] = "Rots Item Retrieval Service"
            values[105] = "Rots Item Retrieval Service"
            pack()
        }
        EnumDefinitions.get(1756).apply {
            values[100] = 995
            values[102] = 995
            values[104] = 995
            pack()
        }
        EnumDefinitions.get(1757).apply {
            values[100] = 200000
            values[102] = 100000
            values[104] = 100000
            pack()
        }
    }

    /**
     * Map edits for post-228 boss content. Runs where the effigy map edits
     * used to run, after the maps are copied into the output cache.
     */
    @JvmStatic
    fun applyMapEdits() {
        // Duke Sucellus instance scoreboard (DukeScoreboard.kt).
        edit(12132) {
            DUKE_SCOREBOARD(3041, 6430, 0, 4)
        }
        // Phantom Muspah scoreboard (PhantomMuspahStatistics.kt).
        edit(11681) {
            PHANTOM_MUSPAH_SCOREBOARD(2914, 10317, 0, 4)
        }
        // Scoreboard / Ancient Tablet placements for post-228 bosses.
        edit(8292) {
            replace(49475, 47589)
        }
        edit(4405) {
            replace(49476, 47598)
        }
        edit(14745) {
            replace(54270, 54149)
        }
    }

    class MapEdit(val regionId: Int) {
        private val objects = mutableListOf<WorldObject>()
        val replacements = mutableMapOf<Int, Int>()
        operator fun Int.invoke(x: Int, y: Int, z: Int, type: Int = 10, rotation: Int = 0) {
            objects += WorldObject(this, type, rotation, Location(x, y, z))
        }

        fun replace(oldId: Int, newId: Int) {
            replacements[oldId] = newId
        }

        private fun buildPredicate() : Predicate<WorldObject> =
            Predicate<WorldObject> {
                if(replacements.containsKey(it.id))
                    it.id = replacements[it.id]!!
                false
            }

        fun pack() {
            TypeParser.packMapPre209(regionId, null, Regions.inject(regionId, buildPredicate(), *objects.toTypedArray()))
        }
    }

    private fun edit(regionId: Int, block: MapEdit.() -> Unit) {
        MapEdit(regionId).apply(block).pack()
    }
}

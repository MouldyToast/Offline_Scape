package org.jesse.cache_tool.packing.custom

import org.jesse.game.obj.ids.ZAMORAK_PORTAL
import org.jesse.game.world.entity.player.container.impl.ContainerType
import mgi.tools.parser.TypeParser
import mgi.types.config.InventoryDefinitions
import mgi.types.config.ObjectDefinitions
import mgi.types.config.enums.EnumDefinitions
import mgi.types.config.npcs.NPCDefinitions

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
        packNpcOverrides()
    }

    /**
     * NPC definition overrides for kept custom content. New-id NPCs are Kryo
     * copies of their source definition, exactly as the old TOML NPC reader
     * produced them.
     */
    private fun packNpcOverrides() {
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

}

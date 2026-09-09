package org.jesse.cache_tool.packing.custom

import org.jesse.game.item.ids.ELDER_MAUL_OR
import org.jesse.game.obj.ids.DUKE_SCOREBOARD
import org.jesse.game.obj.ids.PHANTOM_MUSPAH_SCOREBOARD
import org.jesse.game.obj.ids.ZAMORAK_PORTAL
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.player.container.impl.ContainerType
import org.jesse.game.world.`object`.WorldObject
import org.jesse.game.world.region.Regions
import mgi.tools.parser.TypeParser
import mgi.types.config.InventoryDefinitions
import mgi.types.config.ObjectDefinitions
import mgi.types.config.enums.EnumDefinitions
import mgi.types.config.npcs.NPCDefinitions
import net.runelite.cache.util.ScriptVarType
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
        // The slumbering Duke Sucellus (NPC 12166) has no options in vanilla;
        // the boss fight is initiated through this Attack option.
        NPCDefinitions.get(12166).apply {
            options[1] = "Attack"
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
     * The tournament preset item picker list (enum 10024, read by the server
     * through Enums.TOURNAMENT_ITEMS_ENUM). Tournament content is kept, and
     * this enum does not exist in the vanilla cache. All entries are vanilla
     * item ids.
     */
    @JvmStatic
    fun packEnums() {
        EnumDefinitions.create(10024, ScriptVarType.INTEGER, ScriptVarType.OBJ).apply {
            defaultInt = -1
            values[0] = 565
            values[1] = 560
            values[2] = 9075
            values[3] = 557
            values[4] = 555
            values[5] = 562
            values[6] = 566
            values[7] = 554
            values[8] = 556
            values[9] = 561
            values[10] = 563
            values[11] = 564
            values[12] = 21880
            values[13] = 3144
            values[14] = 385
            values[15] = 391
            values[16] = 397
            values[17] = 13441
            values[18] = 11936
            values[19] = 6685
            values[20] = 10925
            values[21] = 3024
            values[22] = 2434
            values[23] = 2440
            values[24] = 2442
            values[25] = 2436
            values[26] = 12695
            values[27] = 2444
            values[28] = 3040
            values[29] = 4417
            values[30] = 11090
            values[31] = 2550
            values[32] = 5698
            values[33] = 24225
            values[34] = 10887
            values[35] = 11802
            values[36] = 20784
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

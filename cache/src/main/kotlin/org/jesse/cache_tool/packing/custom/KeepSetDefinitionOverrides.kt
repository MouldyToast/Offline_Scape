package org.jesse.cache_tool.packing.custom

import mgi.tools.parser.TypeParser
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
     * NPC definition overrides. Runs where the custom item packer used to
     * run, before the main definition pack pass.
     */
    @JvmStatic
    fun pack() {
        packNpcOverrides()
    }

    /**
     * NPC definition overrides for kept custom content. New-id NPCs are Kryo
     * copies of their source definition, exactly as the old TOML NPC reader
     * produced them.
     */
    private fun packNpcOverrides() {
        // TzHaar-Ket-Keh inferno practice mode.
        NPCDefinitions.get(7690).apply {
            setOption(0, "Talk-to")
            setOption(1, "Practice Mode")
            setOption(2, "")
            pack()
        }
    }

    /**
     * Object definition overrides. Runs where the custom objects packer used
     * to run, after the main definition pack pass.
     */
    @JvmStatic
    fun packObjects() {
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

}

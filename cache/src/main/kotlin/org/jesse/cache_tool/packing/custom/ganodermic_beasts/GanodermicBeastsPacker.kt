package org.jesse.cache_tool.packing.custom.ganodermic_beasts

import org.jesse.cache_tool.packing.assetsBase
import org.jesse.cache_tool.packing.custom.defaultModels
import mgi.custom.AnimationBase
import mgi.custom.FramePacker
import mgi.tools.parser.TypeParser
import mgi.types.config.AnimationDefinitions
import mgi.types.config.npcs.NPCDefinitions

/**
 * @author Jire
 */
internal object GanodermicBeastsPacker {

    @JvmStatic
    fun pack() = assetsBase("assets/osnr/ganodermic_beasts/") {
        defaultModels()

        "sequence_definitions" {
            AnimationDefinitions(id, mgiBuffer, AnimationDefinitions.AnimFormat.PRE_OPCODE_CHANGE).pack()
        }
        FramePacker.reset()
        "sequence_frames" {
            val (groupId, fileId, transformGroupId) = name.split('_').map { it.toInt() }
            val frameID = (groupId shl 16) or fileId
            val skeletonId = bytes[0].toInt() and 0xff shl 8 or (bytes[1].toInt() and 0xff)
            FramePacker.add(frameID, bytes)
        }
        FramePacker.write()
        "sequence_transforms" {
            AnimationBase.pack(id, bytes)
        }
        "npc_definitions" {
            NPCDefinitions(id, mgiBuffer, true).apply {
                resizeX = (resizeX * 1.5).toInt()
                resizeY = (resizeY * 1.5).toInt()
                size = 5
                if (id == 14698) {
                    isFamiliar = true
                    isMinimapVisible = false
                    options = arrayOf(null, null, "Pick-up", null, null)
                    combatLevel = 0
                    size = 1
                    resizeX = 80
                    resizeY = 80
                }
            }.pack()
        }
    }
}

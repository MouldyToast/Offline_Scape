package org.jesse.cache_tool.packing.custom

import org.jesse.game.obj.ids.*
import mgi.tools.parser.TypeParser
import mgi.types.config.ObjectDefinitions

object NearRealityCustomObjectsPacker {

    @JvmStatic
    fun pack(){
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

        ObjectDefinitions.get(47567).apply {
            setOption(1, "Take")
            pack()
        }

        ObjectDefinitions.get(47568).apply {
            setOption(1, "Take")
            pack()
        }

        TypeParser.cloneObject(31583, CHAOS_CHEST_OPENED).apply {
            name = "Chaos Chest"
            pack()
        }

        TypeParser.cloneObject(33114, CHAOS_CHEST_SPAWN).apply {
            name = "Chaos Chest"
            pack()
        }

        TypeParser.cloneObject(33115, CHAOS_CHEST_SPAWNED).apply {
            name = "Chaos Chest"
            pack()
        }


    }
}

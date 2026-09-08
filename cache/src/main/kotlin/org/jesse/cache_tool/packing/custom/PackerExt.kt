package org.jesse.cache_tool.packing.custom

import org.jesse.cache_tool.packing.AssetsBase
import org.jesse.cache_tool.packing.custom.NearRealityCustomItemPacker.CustomDefinition
import mgi.custom.AnimationBase
import mgi.custom.FramePacker
import mgi.tools.parser.TypeParser.packModel
import mgi.types.config.AnimationDefinitions
import mgi.types.config.SpotAnimationDefinition

fun AssetsBase.defaultSkeletons() = run {
    "skeletons" {
        AnimationBase.pack(id, bytes)
    }
}

fun AssetsBase.defaultSkins() = run {
    FramePacker.reset()
    "skins" {
        val (groupId, fileId) = name.split('_').map { it.toInt() }
        val frameID = (groupId shl 16) or fileId
        FramePacker.add(frameID, bytes)
    }
    FramePacker.write()
}

fun AssetsBase.defaultModels() = run {
    "models" {
        val custom = map[name] ?: map[name.lowercase()] ?: error("no id mapping found for $name")
        if (custom is CustomDefinition.Model)
            packModel(custom.modelId, bytes)
    }
}

fun AssetsBase.defaultSequences() = run {
    "sequences" {
        AnimationDefinitions(id, mgiBuffer, AnimationDefinitions.AnimFormat.PRE_OPCODE_CHANGE).pack()
    }
}

fun AssetsBase.defaultGraphics() = run {
    "graphics" {
        SpotAnimationDefinition(id, mgiBuffer).pack()
    }
}
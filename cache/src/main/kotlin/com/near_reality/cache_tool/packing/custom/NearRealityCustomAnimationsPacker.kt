package com.near_reality.cache_tool.packing.custom

import com.near_reality.cache_tool.packing.assetsBase
import com.near_reality.game.item.CustomItemId
import com.zenyte.game.item.ItemId
import mgi.custom.AnimationBase
import mgi.custom.FramePacker
import mgi.types.config.AnimationDefinitions
import java.util.*

object NearRealityCustomAnimationsPacker {
    @JvmStatic
    fun pack() {
        // push forward something, remove the encoded item
        AnimationDefinitions.get(3000).copy(12_000).apply {
            rightHandItem = -1
            leftHandItem = -1
            pack()
        }
        AnimationDefinitions.get(1658).copy(12_001).apply {
            rightHandItem = CustomItemId.LIME_WHIP_SPECIAL
            pack()
        }
        AnimationDefinitions.get(7514).copy(12_002).apply {
            rightHandItem = CustomItemId.LIME_WHIP_SPECIAL
            pack()
        }
        AnimationDefinitions.get(7208).copy(12_003).apply {
            println(frameLengths.contentToString())
            frameLengths = IntArray(frameLengths.size) { 4 }
            println(frameLengths.contentToString())
            pack()
        }
        AnimationDefinitions.get(7049).copy(12_004).apply {
            rightHandItem = CustomItemId.HOLY_GREAT_WARHAMMER
            pack()
        }
        AnimationDefinitions.get(7215).copy(12_005).apply {
            rightHandItem = CustomItemId.HOLY_GREAT_WARHAMMER
            pack()
        }

        AnimationDefinitions.get(7644).copy(12_006).apply {
            rightHandItem = ItemId.CORRUPTED_ARMADYL_GODSWORD
            pack()
        }

        AnimationDefinitions.get(2846).copy(12_007).apply {
            rightHandItem = ItemId.ECHO_AXE
            leftHandItem = -1
            pack()
        }
        AnimationDefinitions.get(8336).copy(12_008).apply {
            rightHandItem = ItemId.ECHO_HARPOON
            leftHandItem = -1
            pack()
        }
        AnimationDefinitions.get(8347).copy(12_009).apply {
            rightHandItem = ItemId.ECHO_PICKAXE
            leftHandItem = -1
            pack()
        }
        assetsBase("assets/osnr/custom_animations/armadyl_godbow_special/") {
            "sequence_definitions" {
                AnimationDefinitions(id, mgiBuffer, AnimationDefinitions.AnimFormat.PRE_OPCODE_CHANGE).pack()
            }
            FramePacker.reset()
            "sequence_frames" {
                val (groupId, fileId, _) = name.split('_').map { it.toInt() }
                val frameID = (groupId shl 16) or fileId
                FramePacker.add(frameID, bytes)
            }
            FramePacker.write()
            "sequence_transforms" {
                AnimationBase.pack(id, bytes)
            }
        }
    }
}

package org.jesse.cache_tool.packing.custom

object NearRealityCustomAnimationsPacker {
    @JvmStatic
    fun pack() {
        // All entries removed — native vanilla animations exist for every use case.
        // See commit message for investigation details and native anim ID mapping.
        //
        // To add custom animations in the future:
        //   AnimationDefinitions.get(<vanilla_id>).copy(<custom_id>).apply {
        //       rightHandItem = <item_id>
        //       pack()
        //   }
    }
}
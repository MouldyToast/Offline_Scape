package org.jesse.game.content.skills.woodcutting

import org.jesse.game.world.entity.masks.Animation

interface AxeDefinition {

    val itemId: Int

    val levelRequired: Int

    val cutTime: Int

    val treeCutAnimation: Animation?
    val trunkCutAnimation: Animation?
    val canoeCutAnimation: Animation?

}

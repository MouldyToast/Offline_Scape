package org.jesse.game.content.skills.mining

import org.jesse.game.world.entity.masks.Animation

interface PickAxeDefinition {

    val mineTime: Int

    val id: Int

    val level: Int

    val anim: Animation?

    val alternateAnimation: Animation?
}

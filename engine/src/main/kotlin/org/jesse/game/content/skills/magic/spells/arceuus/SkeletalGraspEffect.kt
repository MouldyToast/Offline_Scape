package org.jesse.game.content.skills.magic.spells.arceuus

import org.jesse.game.util.Utils
import org.jesse.game.world.entity.Entity
import org.jesse.game.world.entity.masks.Graphics
import org.jesse.game.world.entity.player.action.combat.magic.spelleffect.SpellEffect

/**
 * @author Kris | 16/06/2022
 */
class SkeletalGraspEffect : SpellEffect {
    override fun spellEffect(player: Entity, target: Entity, damage: Int) {
        if (damage == 0 || Utils.random(3) != 0) return
        target.graphics = Graphics(1860, 30, 0)
        val duration = when {
            target.hasWardOfArceuus -> 1
            target.hasMarkOfDarkness -> 6
            else -> 3
        }
        target.freezeWithNotification(duration, 0)
    }
}
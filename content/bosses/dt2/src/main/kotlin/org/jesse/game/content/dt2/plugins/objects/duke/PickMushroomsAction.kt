package org.jesse.game.content.dt2.plugins.objects.duke

import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.player.Action
import org.jesse.game.world.entity.player.Skills
import javax.swing.plaf.synth.SynthConstants

@Suppress("unused")
class PickMushroomsAction(private val mushroomType: Int) : Action() {
    override fun start(): Boolean {
        if (player.inventory.hasFreeSlots()) {
            return true
        }
        player.sendMessage("You require some free inventory space to do this.")
        return false
    }

    override fun process(): Boolean {
        if (player.inventory.hasFreeSlots()) {
            return true
        }
        player.sendMessage("You have run out of inventory space.")
        return false
    }

    override fun processWithDelay(): Int {
        player.sendSound(2581)
        player.animation = Animation(832)
        player.sendMessage("You manage to pick mushrooms.")
        player.inventory.addItem(mushroomType, 1)
        player.skills.addXp(Skills.FARMING, 1.0, false)
        return 4
    }
}
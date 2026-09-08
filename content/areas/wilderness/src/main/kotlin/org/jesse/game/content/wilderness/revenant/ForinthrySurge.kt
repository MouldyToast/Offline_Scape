package org.jesse.game.content.wilderness.revenant

import org.jesse.game.content.wilderness.revenant.npc.Revenant
import org.jesse.game.item.ids.*
import org.jesse.game.util.Colour
import org.jesse.game.world.entity.masks.UpdateFlag
import org.jesse.game.npc.ids.*
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.container.impl.equipment.EquipmentSlot
import org.jesse.game.world.entity.player.variables.TickVariable
import org.jesse.utils.TimeUnit

/**
 * @author Andys1814
 */
object ForinthrySurge {

    /**
     * The duration, in ticks, that Forinthry surge will last once activated.
     */
    private val DURATION = TimeUnit.MINUTES.toTicks(30).toInt()

    /**
     * Determines whether the given npc ID is eligible for the Forinthry surge bonus effects.
     */
    private fun isEligibleTarget(npcId: Int): Boolean {
        return Revenant.isRevenant(npcId) || npcId == REVENANT_MALEDICTUS
    }

    /**
     * Checks whether or not the given player has Forinthry surge active.
     */
    fun isActive(player: Player): Boolean {
        return player.variables.getTime(TickVariable.FORINTHRY_SURGE) > 0
    }

    /**
     * Determines whether the Forinthry surge bonus effects should apply against the given npc ID.
     */
    fun check(player: Player, npcId: Int): Boolean {
        return org.jesse.game.content.wilderness.revenant.ForinthrySurge.isActive(player) && org.jesse.game.content.wilderness.revenant.ForinthrySurge.isEligibleTarget(
            npcId
        )
    }

    /**
     * Activates Forinthry surge for the given player.
     */
    @JvmOverloads
    fun activate(player: Player, duration: Int = org.jesse.game.content.wilderness.revenant.ForinthrySurge.DURATION) {
        if (player.equipment.getId(EquipmentSlot.AMULET) != AMULET_OF_AVARICE) {
            player.sendMessage("<col=4f006f>You can't have Forinthry surge without wearing an Amulet of avarice")
            return
        }
        player.variables.schedule(duration, TickVariable.FORINTHRY_SURGE)
        player.updateFlags.flag(UpdateFlag.APPEARANCE)
        player.sendMessage(Colour.RS_GREEN.wrap("You feel the power of Forinthry surge granting you additional strength against the revenants."))
    }

    /**
     * Deactivates Forinthry surge for the given player, notifying them after doing so.
     */
    fun deactivate(player: Player) {
        player.variables.cancel(TickVariable.FORINTHRY_SURGE)
        player.updateFlags.flag(UpdateFlag.APPEARANCE)
        player.sendMessage("<col=4f006f>You have lost your Forinthry surge.</col>")
    }

    @JvmStatic
    fun tick(player: Player, remainingTicks: Int) {
        if (remainingTicks == 0) {
            player.sendMessage("<col=4f006f>You have lost your Forinthry surge.</col>")
            player.updateFlags.flag(UpdateFlag.APPEARANCE)
        }
    }
}

package org.jesse.game.content.tormented_demon.items

import org.jesse.game.content.seq
import org.jesse.game.content.consumables.ConsumableAnimation
import org.jesse.game.item.ids.*
import org.jesse.game.world.World
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.SkillConstants
import org.jesse.game.world.flooritem.FloorItem
import org.jesse.plugins.flooritem.FloorItemPlugin
import kotlin.math.floor

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-19
 */
class SmoulderingPileOfFlesh : FloorItemPlugin {

    override fun getItems(): IntArray =
        intArrayOf(SMOULDERING_PILE_OF_FLESH)

    private var chunksInPile = 4

    override fun handle(player: Player?, item: FloorItem?, optionId: Int, option: String?) {
        if (player == null || item == null) return
        val heal: Int = healedAmount(player)
        val hp = player.skills.getLevelForXp(SkillConstants.HITPOINTS)
        val currentHealth = player.skills.getLevel(SkillConstants.HITPOINTS)
        val boost = getHealthBoost(player)
        val max = hp + boost
        if (currentHealth > max + heal) {
            player.sendMessage("You are already at maximum health.")
            return
        }
        player seq ConsumableAnimation.EAT_ANIM.id
        player.setHitpoints(if ((currentHealth + heal) >= max) max else (currentHealth + heal))
        player.sendMessage("Even as the flesh burns your throat, you feel your wounds begin to mend.")
        chunksInPile--

        if (chunksInPile <= 0)
            World.destroyFloorItem(item)
    }

    /**
     * Calculates and returns the health boost based on the player's current hitpoints.
     *
     * @param player The player whose health boost is being calculated.
     * @return An integer representing the health boost applied to the player.
     */
    private fun getHealthBoost(player: Player): Int {
        return when(player.hitpoints) {
            in 0..10 -> 0
            in 11..20 -> 3
            in 21..25 -> 4
            in 24..30 -> 6
            in 31..40 -> 7
            in 41..50 -> 8
            in 51..60 -> 11
            in 61..70 -> 12
            in 71..75 -> 13
            in 76..80 -> 15
            in 81..90 -> 16
            in 91..92 -> 17
            else -> 18
        }
    }

    private fun healedAmount(player: Player): Int {
        val hitpoints = player.skills.getLevelForXp(SkillConstants.HITPOINTS)
        val c =
            if (hitpoints < 25) 2
            else if (hitpoints < 50) 4
            else if (hitpoints < 75) 6
            else if (hitpoints < 93) 8
            else 13
        return floor((hitpoints / 10f).toDouble()).toInt() + c
    }
}
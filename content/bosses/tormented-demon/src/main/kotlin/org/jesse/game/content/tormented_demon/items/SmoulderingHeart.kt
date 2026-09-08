package org.jesse.game.content.tormented_demon.items

import org.jesse.game.content.consumables.ConsumableEffects
import org.jesse.game.item.ids.*
import org.jesse.game.world.World
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.variables.TickVariable
import org.jesse.game.world.flooritem.FloorItem
import org.jesse.plugins.flooritem.FloorItemPlugin

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-19
 */
class SmoulderingHeart : FloorItemPlugin {

    override fun getItems(): IntArray =
        intArrayOf(SMOULDERING_HEART)

    override fun handle(player: Player?, item: FloorItem?, optionId: Int, option: String?) {
        if (player == null || item == null) return
        player.animation = Animation.STOMP
        // play graphic

        player.variables.schedule(500, TickVariable.SMOULDERING_HEART)
        ConsumableEffects.applyHeart(player)
        player.sendMessage("You crush the heart. Dark energy swirls around you...")
        World.destroyFloorItem(item)
    }
}
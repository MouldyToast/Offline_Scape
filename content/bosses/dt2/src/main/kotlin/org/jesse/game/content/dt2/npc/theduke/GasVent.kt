package org.jesse.game.content.dt2.npc.theduke

import org.jesse.game.util.Utils.getDistance
import org.jesse.game.world.entity.npc.NPC
import org.jesse.game.world.entity.player.Player

/**
 * Mack wrote original logic - Kry rewrote in NR terms
 * @author John J. Woloszyk / Kryeus
 * @date 8.14.2024
 */
data class GasVent(var cooldown: Int = 3): NPC(12198) {

    override fun postInit() {
        this.radius = 0
    }

    operator fun contains(player: Player): Boolean {
        return player.location.getAxisDistance(1, this.location, this.size) <= 1
    }
}
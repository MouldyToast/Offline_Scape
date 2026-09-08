package org.jesse.game.content.slayer

import org.jesse.game.world.entity.npc.NPC
import org.jesse.game.world.entity.player.Player
import java.util.function.Predicate

/**
 * @author John J. Woloszyk / Kryeus
 * @date 7.29.2025
 */
interface SlayerTask {
    fun validate(name: String, npc: NPC): Boolean
    fun getExperience(npc: NPC): Float
    val taskId: Int
    val monsters: Set<String>
    val monsterIds: Set<Int>
    val slayerRequirement: Int
    val tip: String
    val predicate: Predicate<Player>
    val taskName: String
    val enumName: String
}

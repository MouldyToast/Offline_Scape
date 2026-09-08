package com.near_reality.game.content.slayer

import com.zenyte.game.world.entity.player.Player
import java.util.function.Predicate

/**
 * @author John J. Woloszyk / Kryeus
 * @date 7.30.2025
 */
data class SlayerInfo(
    var taskId: Int = 1,
    var slayerRequirement: Int = 1,
    var combatRequirement: Int = 1,
    var wildernessLevel: Int = 0,
    var tip: String = "",
    var range: RegularTask.Range? = null,
    var predicate: Predicate<Player> = Predicate { true },
    var monsterData: SlayerMonsterInfo = SlayerMonsterInfo()
)
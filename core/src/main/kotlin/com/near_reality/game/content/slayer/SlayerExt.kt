package com.near_reality.game.content.slayer

import com.near_reality.game.content.slayer.dialogue.SumonaAssignmentD.Companion.SUMMONA_TASK_COST
import com.near_reality.tools.logging.GameLogMessage
import com.near_reality.tools.logging.GameLogger
import com.zenyte.game.item.ItemId
import com.zenyte.game.util.Utils
import com.zenyte.game.world.entity.attribute
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.SkillConstants

/**
 * @author John J. Woloszyk / Kryeus
 * @date 7.30.2025
 */
fun Player.getAllBossTasks() = BossTask.entries
    .toList()
    .sortedBy { it.name }

fun Player.getAllTasks() : List<SlayerTask> =
    RegularTask.entries + BossTask.entries

fun Player.getSumonaTasks(wildernessIncluded: Boolean = false) = BossTask.entries
    .filter { if(wildernessIncluded) !it.wilderness else true }
    .filter { it.predicate.test(this) }
    .toTypedArray()

fun Player.lookupLastAssignment() = if (slayer.lastAssignmentName == null) null else Assignment.getTask(slayer.lastAssignmentName)
infix fun Player.setSlayerMaster(master: SlayerMaster) { slayer.master = master }
infix fun Player.setTask(task: Assignment) { slayer.assignment = task }
infix fun Player.hasSlayerLevel(level: Int) : Boolean = skills.getLevel(SkillConstants.SLAYER) >= level
fun SlayerMaster.isKonar() = this == SlayerMaster.KONAR_QUO_MATEN

fun Player.underSumonaReqs() = skills.getLevel(SkillConstants.SLAYER) < SlayerMaster.SUMONA.slayerRequirement || skills.combatLevel < SlayerMaster.SUMONA.combatRequirement
fun Player.underSumonaGP() = inventory.getAmountOf(ItemId.COINS_995) < SUMMONA_TASK_COST
var Player.hasActiveSuperior: Boolean by attribute("superior monster", false)
var Player.overrideSuperiorRate: Int by attribute("superior rate", 0)
fun Player.generateSumonaTask(): Assignment {
    val last: SlayerTask? = this.slayer.lastAssignmentName?.let { Assignment.getTask(it) }
    val possible = BossTask.VALUES.filter { task ->
        (slayer.sumonaAssignWildernessTasks() || !task.wilderness) && task.predicate.test(this) && task != last
    }.toMutableList()

    if (possible.isEmpty()) {
        GameLogger.log { GameLogMessage.ServerError(severity = "MEDIUM", log = "Unable to find any sumona task. Granting default Jad task") }
        possible.add(BossTask.JAD)
    }

    val chosen = possible.random()

    val (min, max) = when (chosen) {
        BossTask.JAD,
        BossTask.INFERNO     -> 1 to 1
        BossTask.OLM,
        BossTask.TOB         -> 2 to 6
        BossTask.BARROWS     -> 15 to 65
        else                 -> 10 to 35
    }

    val amount = Utils.random(min, max)

    return Assignment(
        this,
        this.slayer,
        chosen,
        chosen.enumName,
        amount,
        amount,
        SlayerMaster.SUMONA
    )
}
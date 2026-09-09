package org.jesse.game.content.slayer

import org.jesse.tools.logging.GameLogMessage
import org.jesse.tools.logging.GameLogger
import org.jesse.game.item.ids.*
import org.jesse.game.util.Utils
import org.jesse.game.world.entity.attribute
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.SkillConstants

/**
 * @author John J. Woloszyk / Kryeus
 * @date 7.30.2025
 */
fun Player.getAllBossTasks() = BossTask.entries
    .toList()
    .sortedBy { it.name }

fun Player.getAllTasks() : List<SlayerTask> =
    RegularTask.entries + BossTask.entries

fun Player.lookupLastAssignment() = if (slayer.lastAssignmentName == null) null else Assignment.getTask(slayer.lastAssignmentName)
infix fun Player.setSlayerMaster(master: SlayerMaster) { slayer.master = master }
infix fun Player.setTask(task: Assignment) { slayer.assignment = task }
infix fun Player.hasSlayerLevel(level: Int) : Boolean = skills.getLevel(SkillConstants.SLAYER) >= level
fun SlayerMaster.isKonar() = this == SlayerMaster.KONAR_QUO_MATEN

var Player.hasActiveSuperior: Boolean by attribute("superior monster", false)
var Player.overrideSuperiorRate: Int by attribute("superior rate", 0)

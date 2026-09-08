package com.near_reality.game.content.slayer

import com.near_reality.tools.logging.GameLogMessage
import com.near_reality.tools.logging.GameLogger
import com.zenyte.game.content.achievementdiary.diaries.KaramjaDiary
import com.zenyte.game.content.achievementdiary.diaries.LumbridgeDiary
import com.zenyte.game.content.achievementdiary.diaries.MorytaniaDiary
import com.zenyte.game.content.achievementdiary.diaries.VarrockDiary
import com.zenyte.game.model.item.SkillcapePerk
import com.zenyte.game.util.Utils
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.impl.slayer.superior.SuperiorMonster
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.SkillConstants
import com.zenyte.game.world.entity.player.privilege.MemberRank
import com.zenyte.game.world.region.RegionArea
import java.util.function.Predicate


/**
 * @author John J. Woloszyk / Kryeus
 * @date 7.30.2025
 */
object SlayerHelper {



    fun getAssignment(
        player: Player,
        task: RegularTask,
        master: SlayerMaster
    ): Assignment {
        val slayer = player.slayer
        val taskSet: Task = task.getCertainTaskSet(master) ?: task.taskSet.random()
        val amount: Int = player.getAssigmentAmount(task, taskSet)
        val areas: MutableSet<Class<out RegionArea?>> = taskSet.areas
        val area: Class<out RegionArea?>? = if (areas.isNotEmpty()) areas.random() else null

        return Assignment(player, slayer, task, task.enumName, amount, amount, master, area)
    }

    private fun Player.getAssigmentAmount(task: RegularTask, taskSet: Task): Int {
        var min: Int = taskSet.minimumAmount
        var max: Int = taskSet.maximumAmount
        val range: RegularTask.Range? = task.extendedRange
        if (range != null) {
            if (slayer.isUnlocked(range.extensionName)) {
                min = range.min
                max = range.max
                return Utils.random(min, max)
            }
        }
        val amount = Utils.random(min, max)
        return amount
    }

    fun Player.getAssignment(task: RegularTask): Assignment {
        if (task === RegularTask.BOSS) {
            return generateBossTask(slayer.master)
        }
        return getAssignment(this, task, slayer.master)
    }

    private fun Player.generateBossTask(master: SlayerMaster): Assignment {
        val lastAssignment: SlayerTask? = lookupLastAssignment()
        val possibleTasks = getPossibleBossAssignments(master, lastAssignment)
        val tsk: BossTask = possibleTasks.random()
        return tsk createAssignmentFor this withMaster master
    }

    private infix fun BossTask.createAssignmentFor(player: Player)= Assignment(
        player,
        player.slayer,
        this,
        this.enumName,
        0,
        0,
        SlayerMaster.DURADEL
    )
    private infix fun Assignment.withMaster(master: SlayerMaster) = this.apply { this.master = master }
    private infix fun Player.banned(task: RegularTask) = slayer.bannedTasks.containsValue(task)
    private infix fun Player.underCombatFor(task: RegularTask): Boolean = slayer.master != SlayerMaster.KRYSTILIA && slayer.isCheckingCombat && getSkills().getCombatLevel() < task.combatRequirement
    private infix fun Player.underSlayerFor(task: RegularTask): Boolean = getSkills().getLevelForXp(SkillConstants.SLAYER) < task.slayerRequirement

    private fun Player.getPossibleBossAssignments(master: SlayerMaster, lastAssignment: SlayerTask?): List<BossTask> = buildList {
        for (tsk in BossTask.VALUES) {
            if (master == SlayerMaster.KRYSTILIA && !tsk.isAssignableByKrystilia || !tsk.predicate.test(this@getPossibleBossAssignments) || lastAssignment === tsk) {
                continue
            }
            if(master != SlayerMaster.SUMONA && tsk.isAssignableBySumonaOnly) continue
            add(tsk)
        }
        if (isEmpty()) {
            GameLogger.log{ GameLogMessage.ServerError(severity = "MEDIUM", log = "Unable to assign any boss task for $master. Assigning Giant Mole." )}
            add(BossTask.GIANT_MOLE)
        }
    }

    fun Player.getAvailableAssignments(master: SlayerMaster) : List<RegularTask> {
        val possibleTasks = mutableListOf<RegularTask>()
        val lastAssignment: SlayerTask? = lookupLastAssignment()
        for (tsk in RegularTask.VALUES) {
            if (this banned tsk || tsk === lastAssignment || this underSlayerFor tsk || this underCombatFor tsk) {
                continue
            }
            val `$taskSet`: Array<Task> = tsk.taskSet
            for (`$set` in `$taskSet`) {
                val predicate: Predicate<Player> = tsk.predicate
                if (`$set`.slayerMaster != master || !predicate.test(this)) {
                    continue
                }
                possibleTasks.add(tsk)
                break
            }
        }
        return possibleTasks
    }

    fun Player.generateTask(master: SlayerMaster): Assignment {
        val lastAssignment: SlayerTask? = lookupLastAssignment()

        if (lastAssignment != null && SkillcapePerk.SLAYER.isEffective(this) && Utils.random(9) == 0) {
            if (lastAssignment is BossTask) {
                return Assignment(this, this.slayer, lastAssignment, lastAssignment.enumName, 0, 0, master)
            }
            val regularTask: RegularTask = lastAssignment as RegularTask
            if (!(this banned regularTask)) {
                return getAssignment(this, regularTask, master)
            }
        }

        var weight = 0
        var currentWeight = 0
        val possibleTasks = mutableListOf<RegularTask>()
        for (potential in RegularTask.VALUES) {
            if (this banned potential || lastAssignment === potential || this underSlayerFor potential || this underCombatFor potential) {
                continue
            }
            val predicate: Predicate<Player> = potential.predicate
            val `$taskSet`: Array<Task> = potential.taskSet
            for (`$set` in `$taskSet`) {
                if (`$set`.slayerMaster != master || !predicate.test(this)) {
                    continue
                }
                weight += `$set`.weight
                possibleTasks.add(potential)
                break
            }
        }
        val `$randomTask` = Utils.random(weight)
        for (i in possibleTasks.indices.reversed()) {
            val `$task`: RegularTask = possibleTasks[i]
            val `$taskSet`: Task = `$task`.getCertainTaskSet(master) ?: continue
            val `$taskWeight`: Int = `$taskSet`.weight
            if ((`$taskWeight`.let { currentWeight += it; currentWeight }) >= `$randomTask`) {
                when (master) {
                    SlayerMaster.VANNAKA -> achievementDiaries.update(VarrockDiary.SLAYER_TASK_FROM_VANNAKA)
                    SlayerMaster.CHAELDAR -> achievementDiaries.update(LumbridgeDiary.GET_SLAYER_TASK_FROM_CHAELDAR)
                    SlayerMaster.DURADEL -> achievementDiaries.update(KaramjaDiary.SLAYER_TASK_BY_DURADEL)
                    SlayerMaster.MAZCHNA -> achievementDiaries.update(MorytaniaDiary.GET_A_SLAYER_TASK_FROM_MAZCHNA)
                    else -> {}
                }
                if (`$task` === RegularTask.BOSS) {
                    return generateBossTask(master)
                }
                return getAssignment(this, `$task`, master)
            }
        }
        throw RuntimeException("Unable to calculate a task for master $master.")
    }

    fun Player.getSuperiorBonusRate() =
        when (getMemberRank()) {
            MemberRank.TOPAZ -> 5
            MemberRank.SAPPHIRE -> 8
            MemberRank.EMERALD -> 10
            MemberRank.RUBY -> 12
            MemberRank.DIAMOND -> 15
            MemberRank.DRAGONSTONE -> 18
            MemberRank.ONYX -> 20
            MemberRank.ZENYTE -> 25
            MemberRank.ENCHANTED -> 30
            MemberRank.GOLD -> 40
            MemberRank.ETERNAL -> 60
            MemberRank.NEBULA -> 75
            MemberRank.CATALYTIC -> 100
            else -> 0
        }


    @JvmStatic fun shouldSpawnSuperior(player: Player, npc: NPC): Boolean {
        /* Pre-emptive checks before we math */
        if (player.hasActiveSuperior) return false
        if (!player.slayer.isBiggerAndBadder) return false
        if (!player.slayer.isCurrentAssignment(npc)) return false

        var worldRate = 100
        if(player.overrideSuperiorRate != 0) worldRate = player.overrideSuperiorRate
        if (player.variables.slayerBoosterTick > 0) {
            worldRate = 50.coerceAtLeast((worldRate * 0.75).toInt())
        }

        val bonusPercent = player.getSuperiorBonusRate()
        val factor = 1.0 + bonusPercent / 100.0
        val personalRate = 25.coerceAtLeast(Math.round(worldRate / factor).toInt())
        return Utils.random(personalRate) == 1
    }
}
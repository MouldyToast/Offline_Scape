package com.near_reality.game.content.slayer

import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.region.RegionArea
import java.util.function.Predicate

/**
 * @author John J. Woloszyk / Kryeus
 * @date 7.30.2025
 */

@DslMarker
annotation class TaskDsl

@TaskDsl
class TaskBuilder {
    private val _tasks = mutableListOf<Task>()
    /** at the end you get all your Tasks as an Array **/
    val tasks: Array<Task> get() = _tasks.toTypedArray()

    /** a tiny holder so we can chain min/max after weight **/
    inner class Pending internal constructor(val task: Task)

    /** when you say `TURAEL weight 8`, we create & register the Task immediately **/
    @TaskDsl
    infix fun SlayerMaster.weight(weight: Int): Pending {
        val t = Task(master = this, weight = weight, minimumAmount = 0, maximumAmount = 0)
        _tasks += t
        return Pending(t)
    }

    /** then you can say `.min(…)` to set the minimumAmount **/
    @TaskDsl
    infix fun Pending.min(min: Int): Pending {
        task.minimumAmount = min
        return this
    }

    /** and `.max(…)` to set the maximumAmount **/
    @TaskDsl
    infix fun Pending.max(max: Int): Pending {
        task.maximumAmount = max
        return this
    }

    @TaskDsl
    infix fun Pending.area(area: Class<out RegionArea>): Pending {
        task.areas.add(area)
        return this
    }


}

/** entry point **/
fun buildTasks(block: TaskBuilder.() -> Unit): Array<Task> =
    TaskBuilder().apply(block).tasks


@DslMarker
annotation class SlayerInfoDsl

@SlayerInfoDsl
class SlayerInfoBuilder {
    val value = SlayerInfo()

    inner class Pending internal constructor(val info: SlayerInfo)

    @SlayerInfoDsl
    fun tip(tip: String) {
        value.tip = tip
    }

    @SlayerInfoDsl
    fun combat(req: Int) {
        value.combatRequirement = req
    }

    @SlayerInfoDsl
    fun wildernessLvl(lvl: Int) {
        value.wildernessLevel = lvl
    }

    @SlayerInfoDsl
    fun slayer(req: Int) {
        value.slayerRequirement = req
    }

    @SlayerInfoDsl
    fun id(id: Int) {
        value.taskId = id
    }

    @SlayerInfoDsl
    fun extension(range: RegularTask.Range) {
        value.range = range
    }

    @SlayerInfoDsl
    fun predicate(pred: Predicate<Player>) {
        value.predicate = pred
    }

    @SlayerInfoDsl
    fun registerNames(vararg names: String) = names.map { it.lowercase() }.forEach { value.monsterData.names.add(it) }

    @SlayerInfoDsl
    fun registerIds(vararg ids: Int) = value.monsterData.ids.addAll(ids.toList())

}

fun buildInfo(block: SlayerInfoBuilder.() -> Unit): SlayerInfo =
    SlayerInfoBuilder().apply(block).value

@DslMarker
annotation class BossDsl

@BossDsl
class BossTaskBuilder {
    val value = BossTaskInfo()

    @BossDsl
    fun name(name: String) {
        value.taskName = name
        value.enumName = name
    }
    @BossDsl
    fun validate(block: (name: String, npc: NPC) -> Boolean) {
        value.validateBlock = block
    }
    @BossDsl
    fun monsters(vararg names: String) {
        value.monsters = names.toSet()
    }
    @BossDsl
    fun dynamicXp(block: (npc: NPC) -> Float) {
        value.experienceBlock = block
    }
    @BossDsl
    fun staticXp(block: Float) {
        value.xp = block
    }
    @BossDsl
    fun require(pred: (Player) -> Boolean) {
        value.predicate = Predicate(pred)
    }
    @BossDsl
    fun wilderness(bool: Boolean) {
        value.isWildernessTask = bool
    }
    @BossDsl
    fun sumonaOnly(bool: Boolean) {
        value.assignableBySumonaOnly = bool
    }
    @BossDsl
    fun krystilia(bool: Boolean) {
        value.assignableByKrystilia = bool
    }

    @BossDsl
    internal fun build() = value
}

fun buildBossTask(block: BossTaskBuilder.() -> Unit): BossTaskInfo =
    BossTaskBuilder().apply(block).build()
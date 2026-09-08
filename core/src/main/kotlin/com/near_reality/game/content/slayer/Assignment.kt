package com.near_reality.game.content.slayer

import com.zenyte.game.content.achievementdiary.DiaryReward
import com.zenyte.game.content.achievementdiary.DiaryUtil
import com.zenyte.game.content.boss.cerberus.Cerberus
import com.zenyte.game.content.boss.dagannothkings.DagannothKing
import com.zenyte.game.content.boss.grotesqueguardians.boss.Dusk
import com.zenyte.game.content.boss.kraken.Kraken
import com.zenyte.game.content.godwars.instance.GodwarsInstance
import com.zenyte.game.content.kebos.alchemicalhydra.npc.AlchemicalHydra
import com.zenyte.game.content.skills.slayer.*
import com.zenyte.game.item.ids.*
import com.zenyte.game.util.Utils
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.impl.kalphite.KalphiteQueen
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.SkillConstants
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities
import com.zenyte.game.world.entity.player.calog.CATierType
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot
import com.zenyte.game.world.region.GlobalAreaManager.getArea
import com.zenyte.game.world.region.RegionArea
import com.zenyte.game.world.region.area.CatacombsOfKourend
import com.zenyte.game.world.region.area.KalphiteLair
import com.zenyte.game.world.region.area.TaverleyDungeon
import com.zenyte.game.world.region.area.WaterbirthDungeon
import com.zenyte.game.world.region.area.godwars.GodwarsDungeonArea
import com.zenyte.game.world.region.area.wilderness.WildernessArea
import kotlin.math.max

/**
 * @author John J. Woloszyk / Kryeus
 * @date 7.30.2025
 */
class Assignment {
    @JvmOverloads
    constructor(
        player: Player,
        slayer: Slayer,
        task: SlayerTask,
        taskName: String,
        initialAmount: Int,
        amount: Int,
        master: SlayerMaster,
        area: Class<out RegionArea?>? = null
    ) {
        this.player = player
        this.slayer = slayer
        this.task = task
        this.taskName = taskName
        this.initialAmount = initialAmount
        this.amount = amount
        this.master = master
        this.area = area
        if (area != null) {
            this.areaName = area.simpleName
        }
    }

    private fun String.stripSumona(): String = removeSuffix("_SUMONA")
    private fun String.replaceKQA(): String {
        return if (this.equals("KalphiteQueenLair", ignoreCase = true))
            "KalphiteLair"
        else this
    }

    fun initialize(player: Player, old: Assignment) {
        this.player = player
        slayer = player.slayer
        initialAmount = old.initialAmount
        amount = old.amount
        master = old.master
        taskName = old.taskName.stripSumona()
        task = getTask(taskName)
        areaName = old.areaName
        if (areaName.isNotBlank()) {
            areaName.replaceKQA()
            if (task is RegularTask && master != null) {
                val regularTask = task as RegularTask
                val taskSet = regularTask.getCertainTaskSet(master!!)
                for (area in taskSet!!.areas) {
                    if (area.simpleName == areaName) {
                        this.area = area
                        break
                    }
                }
            }
        }
    }

    @Transient lateinit var player: Player
    @Transient lateinit var slayer: Slayer
    @Transient lateinit var task: SlayerTask

    var taskName: String = ""
    var initialAmount: Int = 0
    var amount: Int = 0
        set(value) {
            field = value
            player.varManager.sendVar(Slayer.TASK_AMOUNT_VAR, value)
        }
    var areaName: String = ""

    @Transient
    var area: Class<out RegionArea?>? = null
        private set
    var master: SlayerMaster? = null

    constructor()

    fun checkAssignment(name: String, npc: NPC) {
        if (!task.validate(name, npc)) {
            player.sendDeveloperMessage("Slayer | Did not validate to assignment for npc: $name ${npc.name} on assignment for ${task.monsters.joinToString(", ")}")
            return
        }
        if (master == SlayerMaster.KRYSTILIA) {
            if (!WildernessArea.isWithinWilderness(player)) {
                player.sendFilteredMessage("Only kills done within Wilderness will count towards Krystilia's slayer assignment.")
                return
            }
        } else if (master == SlayerMaster.KONAR_QUO_MATEN && area != null) {
            if (!player.inArea(area!!) && !checkExceptions(npc, area!!)) {
                player.sendMessage("Konar quo Maten requires you to complete your slayer task in the " + getArea(area!!) + ".")
                return
            }
        }
        var experience = task.getExperience(npc)
        if (player.inArea("Slayer Tower")) {
            for (index in DIARY_REWARDS.indices) {
                val reward = DIARY_REWARDS[index]
                if (DiaryUtil.eligibleFor(reward, player)) {
                    experience *= (1 + (0.025 * (index + 1))).toFloat()
                    break
                }
            }
        }

        if (CombatUtilities.dragonSlayerGlovesEquipped(player) && CombatUtilities.isDraconic(npc)) {
            experience *= 1.25.toFloat()
        }

        player.skills.addXp(SkillConstants.SLAYER, experience.toDouble())
        val braceletId = player.equipment.getId(EquipmentSlot.HANDS)

        val hasSlaughter = braceletId == BRACELET_OF_SLAUGHTER
        val hasExpeditious = braceletId == EXPEDITIOUS_BRACELET
        if (hasSlaughter || hasExpeditious) {
            val rate = 25
            val hasRolledEffect = Utils.random(99) <= rate
            if (hasRolledEffect) {
                if (task !== RegularTask.TZTOK_JAD && task !== RegularTask.TZKAL_ZUK) {
                    if (braceletId == BRACELET_OF_SLAUGHTER) {
                        var slaughterUses = player.getNumericAttribute("bracelet of slaughter uses").toInt() + 1
                        val chargesLeft = (30 - slaughterUses)
                        if (chargesLeft == 0) {
                            if (player.combatAchievements.hasTierCompleted(CATierType.ELITE) && Utils.random(9) == 0) {
                                slaughterUses = 0
                                player.sendMessage("Your bracelet of slaughter prevents your slayer count decreasing. <col=FF0000>It has recharged completely.")
                            } else {
                                player.sendMessage("Your bracelet of slaughter prevents your slayer count decreasing. <col=FF0000>It then crumbles to dust.")
                            }
                        } else if (chargesLeft == 1) {
                            player.sendMessage("Your bracelet of slaughter prevents your slayer count decreasing. <col=FF0000>It has one charge left.")
                        } else {
                            player.sendFilteredMessage("Your bracelet of slaughter prevents your slayer count decreasing.")
                        }
                        player.addAttribute("bracelet of slaughter uses", slaughterUses % 30)
                        if (slaughterUses >= 30) {
                            player.equipment[EquipmentSlot.HANDS] =
                                null
                        }
                        finish(npc)
                    } else if (braceletId == EXPEDITIOUS_BRACELET) {
                        var expeditiousBraceletUses = player.getNumericAttribute("expeditious bracelet uses").toInt() + 1
                        val chargesLeft = (30 - expeditiousBraceletUses)
                        if (chargesLeft == 0) {
                            if (player.combatAchievements.hasTierCompleted(CATierType.ELITE) && Utils.random(9) == 0) {
                                expeditiousBraceletUses = 0
                                player.sendMessage("Your expeditious bracelet helps you progress your slayer task faster. <col=FF0000>It has recharged completely.")
                            } else {
                                player.sendMessage("Your expeditious bracelet helps you progress your slayer task faster. <col=FF0000>It then crumbles to dust.")
                            }
                        } else if (chargesLeft == 1) {
                            player.sendMessage("Your expeditious bracelet helps you progress your slayer task faster. <col=FF0000>It has one charge left.")
                        } else {
                            player.sendFilteredMessage("Your expeditious bracelet helps you progress your slayer task faster.")
                        }
                        player.addAttribute("expeditious bracelet uses", expeditiousBraceletUses % 30)
                        if (expeditiousBraceletUses >= 30) {
                            player.equipment[EquipmentSlot.HANDS] =
                                null
                        }
                        amount = max(0.0, (amount - 2).toDouble()).toInt()
                        finish(npc)
                    }
                    return
                }
            }
        }
        if ((task === RegularTask.TZTOK_JAD && !name.equals(
                "TzTok-Jad",
                ignoreCase = true
            )) || (task === RegularTask.TZKAL_ZUK && !name.equals("TzKal-Zuk", ignoreCase = true))
        ) {
            return
        }
        amount--
        finish(npc)
    }

    fun finish(npc: NPC?) {
        val braceletId = player.equipment.getId(EquipmentSlot.HANDS)
        if (amount <= 0) {
            slayer.finishAssignment(npc)
        } else if (amount % 10 == 0) {
            if (braceletId == 11972 || braceletId == 11974 || braceletId == 11118 || braceletId == 11120 || braceletId == 11122 || braceletId == 11124) {
                player.sendMessage("You still need to kill " + amount + " x " + task.taskName + " to complete your current Slayer assignment")
            }
        }
    }

    fun checkExceptions(npc: NPC, area: Class<out RegionArea?>): Boolean {
        if (npc is AlchemicalHydra && (task == RegularTask.HYDRAS || task == BossTask.ALCHEMICAL_HYDRA)) {
            return true
        }
        //Only the Taverley dungeon area task will work.
        if (npc is Cerberus && task == RegularTask.HELLHOUNDS) {
            return area == TaverleyDungeon::class.java
        }
        //Only the Kalphite lair works, not the slayer-only cave.
        if (npc is KalphiteQueen && task == RegularTask.KALPHITE) {
            return area == KalphiteLair::class.java
        }
        //Only the Waterbirth dungeon will work.
        if (npc is DagannothKing && task == RegularTask.DAGANNOTH) {
            return area == WaterbirthDungeon::class.java
        }
        if (npc is Dusk && task == RegularTask.GARGOYLES) {
            return true
        }
        if (npc is Kraken && task == RegularTask.CAVE_KRAKEN) {
            return true
        }
        if (npc.id == NpcId.THERMONUCLEAR_SMOKE_DEVIL && task == RegularTask.SMOKE_DEVILS) {
            return true
        }
        //Only the Catacombs of Kourend work.
        if (npc.id == NpcId.SKOTIZO && (task == RegularTask.BLACK_DEMONS || task == RegularTask.GREATER_DEMONS)) {
            return area == CatacombsOfKourend::class.java
        }
        if (area == GodwarsDungeonArea::class.java) {
            return player.area is GodwarsInstance
        }
        return false
    }

    fun isValid(player: Player, npc: NPC): Boolean {
        if (task.validate(npc.getName(player), npc)) {
            if (master == SlayerMaster.KONAR_QUO_MATEN && area != null) {
                return player.inArea(area!!) || checkExceptions(npc, area!!)
            }
            return true
        }
        return false
    }

    companion object {
        @JvmStatic fun getTask(name: String): SlayerTask {
            for (regularTask in RegularTask.VALUES) {
                if (regularTask.name == name) {
                    return regularTask
                }
            }
            for (bossTask in BossTask.VALUES) {
                if (bossTask.name == name) {
                    return bossTask
                }
            }
            throw IllegalStateException("Task not found: $name")
        }

        private val DIARY_REWARDS = arrayOf(
            DiaryReward.MORYTANIA_LEGS4,
            DiaryReward.MORYTANIA_LEGS3,
            DiaryReward.MORYTANIA_LEGS2,
            DiaryReward.MORYTANIA_LEGS1
        )
    }
}

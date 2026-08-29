package com.near_reality.game.content.araxxor

import com.near_reality.game.content.araxxor.araxytes.Araxyte
import com.near_reality.game.content.araxxor.araxytes.impl.AcidicAraxyte
import com.near_reality.game.content.araxxor.araxytes.impl.MirrorbackAraxyte
import com.near_reality.game.content.araxxor.araxytes.impl.RupturaAraxyte
import com.near_reality.game.content.araxxor.attacks.Attack
import com.near_reality.game.content.araxxor.attacks.impl.CleaveAttack
import com.near_reality.game.content.araxxor.attacks.impl.MagicAttack
import com.near_reality.game.content.araxxor.attacks.impl.MeleeAttack
import com.near_reality.game.content.araxxor.attacks.impl.RangedAttack
import com.near_reality.game.content.araxxor.attacks.spec.AcidBall
import com.near_reality.game.content.araxxor.attacks.spec.AcidDrip
import com.near_reality.game.content.araxxor.attacks.spec.AcidSplatter
import com.near_reality.game.content.scoreboard.ScoreboardModule
import com.near_reality.game.content.seq
import com.zenyte.game.task.WorldTasksManager.schedule
import com.zenyte.game.util.Direction
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.npc.CombatScriptsHandler
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NPCCombat
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.world.entity.npc.Spawnable
import com.zenyte.game.world.entity.npc.combat.CombatScript
import com.zenyte.game.world.entity.player.Bonuses
import com.zenyte.game.world.entity.player.NotificationSettings
import com.zenyte.game.world.entity.player.Player
import java.util.*
import kotlin.random.Random

/**
 * @author Glabay | Glabay-Studios
 * @since 2024-10-20
 *
 * @author John J. Woloszyk / Kryeus
 * @date 7.9.2025
 *
 *  Glabay is not a good developer. 20-30 bugs were found in this code.
 *  Kryeus resolved said bugs.
 */
data class Araxxor(
    val instance: AraxxorInstance?,
    var spawnLocation: Location?,
) : NPC(
    ARAXXOR,
    spawnLocation,
    true
), CombatScript, Spawnable {

    @Suppress("UNUSED_PARAMETER")
    constructor(id: Int, location: Location?, direction: Direction, radius: Int): this(null, location)


    init {
        aggressionDistance = 64
        maxDistance = 64
        isForceAggressive = true
        combat = object : NPCCombat(this) {
            override fun isMelee(): Boolean = this@Araxxor.activeAttackStyle.isMelee()
            override fun combatAttack(): Int {
                if (target == null) {
                    return 0
                }
                val melee = isMelee
                if (npc.isProjectileClipped(target, melee)) {
                    return 0
                }
                val distance = if (melee || npc.isForceFollowClose) 0 else npc.attackDistance
                if (outOfRange(target, distance, target.size, melee)) {
                    return 0
                }
                addAttackedByDelay(target)
                return CombatScriptsHandler.specialAttack(npc, target)
            }

        }
    }

    /* Debugging vars */
    val debugProcessCalcs = false
    val debugCleaveAttack = false

    var primaryAraxyte: Araxyte? = null
    var enraged: Boolean = false
    private var minionIndex: Int = 0
    var activeAttackStyle: Attack = MeleeAttack()

    private fun getAttackSpeed(): Int = if (isHeathAtEnragedThreshold()) 4 else 6
    override fun isEntityClipped(): Boolean = false
    override fun isTolerable(): Boolean = false
    override fun validate(id: Int, name: String): Boolean = id == ARAXXOR
    override fun isIntelligent(): Boolean = true

    var attackCounter: Int = 0
    private var isHatchTick: Boolean = true


    private fun specialAttack(target: Entity) {
        instance ?: return
        isHatchTick = true
        when (primaryAraxyte) {
            is AcidicAraxyte -> AcidBall(instance).invoke(this, target)
            is MirrorbackAraxyte -> AcidSplatter(instance).invoke(this, target)
            is RupturaAraxyte -> AcidDrip(instance).invoke(this, target)
        }
    }

    private fun hatchSpiderEgg(target: Entity) {
        instance ?: return
        isHatchTick = false
        if (instance.araxytes.size > 0 && minionIndex < 9) {
            val hatching = instance.araxytes[minionIndex]
            if (!hatching.isDead || !hatching.isFinished) {
                hatching.hatchEgg(target)
            }
            minionIndex++
        }
    }

    override fun attack(target: Entity): Int {
        if (target !is Player || isDead || isFinished) return 1

        // Determine if we should trigger a hatch or special attack
        if (++attackCounter >= 3 && attackCounter % 3 == 0) {
            if (isHatchTick) {
                hatchSpiderEgg(target)
            } else if (!enraged) {
                specialAttack(target)
                return getAttackSpeed()
            }
        }

        syncAttackStyle()

        // Debug message for tracking attack style
        target.sendDeveloperMessage("Araxxor attack style: ${activeAttackStyle::class.simpleName}")

        // Perform the attack
        activeAttackStyle.invoke(this, target)
        return getAttackSpeed()
    }

    override fun isWithinMeleeDistance(npc: NPC, target: Entity): Boolean =
        npc.middleLocation.withinDistance(target, (this.size/2) + 1)


    override fun handleIngoingHit(hit: Hit?) {
        hit ?: return
        super.handleIngoingHit(hit)
        checkOutOfMeleeRange()
        mirrorBackDamage(hit)
    }

    private fun mirrorBackDamage(hit: Hit) {
        instance ?: return
        for (araxyte in instance.araxytes.filterIsInstance<MirrorbackAraxyte>()) {
            if (hit.damage > 1) {
                val hitBack = hit.damage / 2
                araxyte.applyHit(Hit(hit.source, hitBack, HitType.DEFAULT))
            }
        }
    }

    private fun checkOutOfMeleeRange() {
        val target = instance?.player ?: return
        if ((activeAttackStyle.isMelee()) && !isWithinMeleeDistance(this, target)) {
            syncAttackStyle()
        }
    }



    override fun processNPC() {
        super.processNPC()

        if (id != ARAXXOR) return
        // get the player object
        val target = instance?.player as Player
        // if they are not in the instance, stop
        if (target.mapInstance != instance)
            return
        // update the HuD
        if (target.hpHud != null && target.hpHud.isOpen)
            target.hpHud.updateValue(getHitpoints())

        val withinMeleeDistance = isWithinMeleeDistance(this, target)
        if(debugProcessCalcs) {
            debug("Araxxor: Target within melee distance : $withinMeleeDistance")
            debug("Araxxor: Force Follow Close : $isForceFollowClose")
        }

        // Check if the Inner-Hulk is coming out
        if (isHeathAtEnragedThreshold() && !enraged)
            enrage()
    }

    private fun enrage() {
        lock()
        enraged = true
        this seq 11488
        schedule(1) {
            this seq 11489
//            val models = this.definitions.models.clone().toMutableList()
//            models.add(54289)
//            models.add(54288)
//            setModelCustomization(*models.toIntArray())
            unlock()
        }

    }

    override fun sendDeath() {
        instance ?: return
        // Remove acid pools
        if (instance.poolObjects.isNotEmpty()) {
            instance.poolObjects.forEach(World::removeObject)
            instance.poolObjects.clear()
        }
        // Remove remaining Araxytes
        if (instance.araxytes.isNotEmpty()) {
            instance.araxytes.forEach(NPC::remove)
            instance.araxytes.clear()
        }
        enraged = false
        instance.hasLootAvailable = true
        setAnimation(getCombatDefinitions().deathAnim)
        schedule(5) {
            if (attacking != null && attacking is Player) {
                val player = (attacking as Player)
                player.hpHud.close()
                if (NotificationSettings.isKillcountTracked(name)) {
                    player.notificationSettings.increaseKill(name)
                    if (NotificationSettings.BOSS_NPC_NAMES.contains(name.lowercase(Locale.getDefault())))
                        player.notificationSettings.sendBossKillCountNotification(name)
                }
                player.slayer.checkAssignment(this)
                ScoreboardModule.updateAraxxorStatistics(player.bossTimer.currentTracker)
                player.bossTimer.finishTracking("Araxxor")
            }
            setTransformationPreservingStats(ARAXXOR_CORPSE)
        }
    }

    override fun setRespawnTask() {}

    fun resetInstance() {
        instance ?: return
        minionIndex = 0
        attackCounter = 0
        isHatchTick = true
        instance.poolObjects.forEach(World::removeObject)
        instance.poolObjects.clear()
        instance.npcs.forEach(NPC::remove)
        instance.npcs.clear()
        instance.araxytes.forEach(NPC::remove)
        instance.araxytes.clear()
    }

    private fun syncAttackStyle(forceDistanceAttack: Boolean = false) {
        val target = instance?.player ?: return

        val currentAttackStyle = activeAttackStyle
        val withinMeleeDistance = isWithinMeleeDistance(this, target)
        val shouldEnforceMelee = !currentAttackStyle.isMelee() && !withinMeleeDistance && Random.nextDouble() < 0.25

        if(enraged && withinMeleeDistance && currentAttackStyle !is CleaveAttack) {
            enrageCombatMode()
            return
        }

        if(currentAttackStyle.isMelee() && withinMeleeDistance) {
            debug("Araxxor: Target in melee range & current attack style is melee.")
            isForceFollowClose = false
            return
        }

        if(shouldEnforceMelee && !forceDistanceAttack) {
            meleeCombatMode()
            return
        }

        if(withinMeleeDistance) {
            meleeCombatMode()
            return
        }

        rangedCombatMode(target)
    }

    fun getMeleeAttack(): Attack =
        if (isHeathAtEnragedThreshold())
            CleaveAttack()
        else
            MeleeAttack()

    private fun isHeathAtEnragedThreshold(): Boolean =
        hitpoints < (0.25 * maxHitpoints).toInt()

    fun getDistanceAttack(target: Entity): Attack =
        if (attackingWithRange(target)) RangedAttack()
        else MagicAttack()


    private fun attackingWithRange(target: Entity): Boolean =
        if (target is Player) {
            val mageDef = target.bonuses.getBonus(Bonuses.Bonus.DEF_MAGIC)
            val rangeDef = target.bonuses.getBonus(Bonuses.Bonus.DEF_RANGE)
            rangeDef < mageDef
        }
        else false;


}
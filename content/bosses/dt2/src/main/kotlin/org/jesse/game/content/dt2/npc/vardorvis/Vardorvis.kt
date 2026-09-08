package org.jesse.game.content.dt2.npc.vardorvis

import org.jesse.game.content.dt2.area.DT2Module
import org.jesse.game.content.dt2.area.VardorvisInstance
import org.jesse.game.content.dt2.npc.*
import org.jesse.game.content.dt2.npc.vardorvis.attacks.Attack
import org.jesse.game.content.dt2.npc.vardorvis.attacks.impl.AutoAttack
import org.jesse.game.content.dt2.npc.vardorvis.attacks.impl.DartingSpikes
import org.jesse.game.content.dt2.npc.vardorvis.attacks.impl.Strangle
import org.jesse.game.content.dt2.npc.vardorvis.attacks.impl.headgaze.HeadGaze
import org.jesse.game.content.dt2.npc.vardorvis.attacks.impl.headgaze.HeadGazeProjectile
import org.jesse.game.content.dt2.npc.vardorvis.attacks.impl.headgaze.VardorvisHead
import org.jesse.game.content.dt2.npc.vardorvis.attacks.impl.swingingaxe.SwingingAxe
import org.jesse.game.content.dt2.npc.vardorvis.attacks.impl.swingingaxe.SwingingAxeTask
import org.jesse.game.content.dt2.npc.vardorvis.attacks.impl.swingingaxe.SwingingAxes
import org.jesse.game.content.seq
import org.jesse.game.content.spotanim
import org.jesse.game.GameInterface
import org.jesse.game.content.boss.BossRespawnTimer
import org.jesse.game.content.dt2.npc.leviathan.awakened
import org.jesse.game.content.skills.prayer.Prayer
import org.jesse.game.task.TickTask
import org.jesse.game.util.Colour
import org.jesse.game.util.Direction
import org.jesse.game.world.World
import org.jesse.game.world.entity.Entity
import org.jesse.game.world.entity.EntityHitBar
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.Tinting
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.Graphics
import org.jesse.game.world.entity.masks.Hit
import org.jesse.game.world.entity.masks.HitType
import org.jesse.game.world.entity.npc.NPC
import org.jesse.game.npc.ids.*
import org.jesse.game.world.entity.npc.combat.CombatScript
import org.jesse.game.world.entity.npc.combatdefs.NPCCombatDefinitions
import org.jesse.game.world.entity.npc.combatdefs.StatType
import org.jesse.game.world.entity.player.NotificationSettings
import org.jesse.game.world.entity.player.Player
import java.util.*
import kotlin.math.abs

/*
 * Sound Data:
 * - SoundEffect(id = 7139, radius = 9)
 * - SoundEffect(id = 7088)
 * - SoundEffect(id = 7078)
 * - SoundEffect(id = 7081)
 * - SoundEffect(id = 7083)
 * - SoundEffect(id = 7099)
 * - SoundEffect(id = 7086)
 * - SoundEffect(id = 7145)
 * - SoundEffect(id = 7109, radius = 10, delay = 30)
 * - SoundEffect(id = 5306, delay = 20)
 * - SoundEffect(id = 65535, radius = 10)
 * - SoundEffect(id = 7154, radius = 10)
 * - SoundEffect(name = "blood_sacrifice", delay = 2)
 * - SoundEffect(name = "sum2_spirit_beast_death")
 */

/**
 * @author John J. Woloszyk / Kryeus
 * @date 5.9.2024
 */
internal class Vardorvis(
    tile: Location,
    private val instance: VardorvisInstance,
    val difficulty: DT2BossDifficulty = instance.difficulty
) : NPC(
    if (difficulty == DT2BossDifficulty.AWAKENED) VARDORVIS_12224
    else VARDORVIS,
    tile,
    true
), CombatScript {

    private var enraged: Boolean = false
    private var cooldowns = mutableMapOf<Attack, Int>()

    override fun isIntelligent(): Boolean = true
    override fun isForceAggressive(): Boolean = true
    override fun getRespawnDelay(): Int = BossRespawnTimer.VARDORVIS.timer.toInteger()

    init {
        setMaxDistance(200)
        isForceAggressive = false
        setDamageCap(200)
        possibleTargets.add(instance.player)
        instance.player.hpHud.open(id, maxHitpoints)
        cooldowns.clear()
        hitBar = object : EntityHitBar(this) {
            override fun getType(): Int {
                return 20
            }
        }
    }


    override fun spawn(): NPC {
        instance.players?.forEach {
            if (awakened) {
                it.bossTimer.startTracking("vardorvis awakened")
            } else {
                it.bossTimer.startTracking("vardorvis")
            }
            it.hpHud.open(id, hitpoints)
        }
        return super.spawn()
    }

    override fun onDeath(source: Entity?) {
        super.onDeath(source)
        val awakened = false
        val name = if (awakened) "awakened vardorvis" else "vardorvis"
        instance.players.forEach {
            if (NotificationSettings.isKillcountTracked(name)) {
                it.notificationSettings.increaseKill(name)
                if (NotificationSettings.BOSS_NPC_NAMES.contains(name.lowercase(Locale.getDefault())))
                    it.notificationSettings.sendBossKillCountNotification(name)
            }
            DT2Module.updateVardorvisStatistics(it.bossTimer.currentTracker, awakened = awakened)

            it.bossTimer.finishTracking(name)
        }
    }

    override fun sendNotifications(player: Player?) {}

    override fun attack(target: Entity): Int {
        val atk = determineAttack()
        val defs: NPCCombatDefinitions = this.getCombatDefinitions()
        val decreaseInSpeed = if (hitpointsAsPercentage < 33) 1 else 0

        if (atk.requiredCooldownAttacks(enraged) != 0)
            cooldowns[atk] = atk.requiredCooldownAttacks(enraged)
        decreaseOtherCooldowns(atk)


        if (target is Player) {
            if (target.interfaceHandler.isVisible(GameInterface.ENTANGLED_PUZZLE.id))
                return defs.attackSpeed
            if (target.entangled)
                return defs.attackSpeed - decreaseInSpeed
        }

        when (atk) {
            is AutoAttack -> performAutoAttack(target)
            is DartingSpikes -> {
                val delay = performDartingSpikes(target as Player)
                if (delay == 0) {
                    return performAutoAttack(target)
                }
                return delay
            }

            is HeadGaze -> performHeadGaze()
            is SwingingAxes -> {
                performSwingingAxes()
                performAutoAttack(target)
            }

            is Strangle -> {
                performStrangle()
                return 14
            }
        }
        return defs.attackSpeed - decreaseInSpeed
    }

    private fun performStrangle() {
        val player = instance.player
        player.sendMessage(Colour.RS_RED.wrap("Vardorvis entangles you in some tendrils!"))
        player.cancelCombat()
        player.entangled = true

        GameInterface.ENTANGLED_PUZZLE.open(player)

        val failMessage: String = Colour.RS_RED.wrap("Vardorvis drains a significant portion of your health!")
        World.sendGraphics(Graphics(2523), player.location)
        World.sendGraphics(Graphics(2527, 0, 100), player.location)
        val vardorvis = this
        vardorvis seq 10342
        schedule(object : TickTask() {
            override fun run() {
                if (!player.entangled) {
                    stop()
                    return
                }
                when (ticks++) {
                    1 -> vardorvis seq 10343
                    9 -> {
                        if (player.entangled && !isDead) {
                            instance.player.sendMessage(failMessage)
                            val dmg = if (difficulty == DT2BossDifficulty.AWAKENED) 80 else 50
                            instance.player.applyHit(Hit(dmg, HitType.TYPELESS))
                            applyHit(Hit(dmg, HitType.HEALED))
                        }
                        stop()
                        return
                    }
                }
                if (ticks % 2 == 0 && player.entangled) {
                    player spotanim 2525
                    World.sendGraphics(Graphics(2527, 0, 100), player.location)
                }
            }

            override fun stop() {
                super.stop()
                instance.resetEntanglement(false)
                vardorvis seq 10344
                vardorvis.animation = Animation(10345, 3)
                player.entangled = false
                player.interfaceHandler.closeInterface(GameInterface.ENTANGLED_PUZZLE)
            }

        }, 0, 0)
    }

    override fun postHitProcess(hit: Hit) {
        if (hit.source is Player)
            instance.player.hpHud.updateValue(getHitpoints())
    }


    private fun Npc.performDartingSpikes(target: Player): Int {
        val targetPosition = target.position.copy()
        val direction = specialDashDirection(targetPosition) ?: return 0

        schedule {
            performDash(target.position, targetPosition, direction, 0)
            schedule {
                if (performDash(target.position, targetPosition, null, 1)) {
                    schedule {
                        performDash(target.position, targetPosition, null, 2)
                    }
                }
            }
        }

        return combatDefinitions.attackSpeed * 3
    }

    private fun Npc.specialDashDirection(target: Location): Triple<Direction, Direction, Direction?>? {
        val xDelta = target.x - position.x
        val yDelta = target.y - position.y

        val xDirection = if (xDelta > 0) Direction.EAST else Direction.WEST
        val yDirection = if (yDelta > 0) Direction.NORTH else Direction.SOUTH

        val mainDirection = if (abs(xDelta) > abs(yDelta)) xDirection else yDirection
        val subDirection = if (mainDirection == xDirection) yDirection else xDirection

        val diagonalDirection = when {
            xDirection == Direction.EAST && yDirection == Direction.NORTH -> Direction.NORTH_EAST
            xDirection == Direction.EAST && yDirection == Direction.SOUTH -> Direction.SOUTH_EAST
            xDirection == Direction.WEST && yDirection == Direction.NORTH -> Direction.NORTH_WEST
            xDirection == Direction.WEST && yDirection == Direction.SOUTH -> Direction.SOUTH_WEST
            else -> null
        }

        // If moving the npc will cause it to be out of bounds, return null
        val final = position.transform(mainDirection, 4).transform(subDirection, 2)
        if (!final.inArea(VardorvisFightArea::class, instance) || !World.isFloorFree(final, 1)
        ) {
            return null
        }

        return Triple(mainDirection, subDirection, diagonalDirection)
    }

    private fun Npc.performDash(
        actualTargetPosition: Location,
        firstTargetPosition: Location,
        candidate: Triple<Direction, Direction, Direction?>?,
        count: Int
    ): Boolean {
        val (mainDirection, subDirection, diagonalDirection) = candidate ?: specialDashDirection(firstTargetPosition)
        ?: return false

        val startingPosition =
            firstTargetPosition.transform(mainDirection.opposing, 2).transform(subDirection.opposing, 1)

        val targetPosition = startingPosition.transform(mainDirection, 4).transform(subDirection, 2)
        addGroundTendril(targetPosition, delay = 12)

        if (count == 0) {
            // Put one under the npc
            addGroundTendril(startingPosition, delay = 3)
            // Put one under the target
            addGroundTendril(firstTargetPosition, delay = 3)
            // Put one in the main direction's opposite, 3 tiles away
            val mainPosition = startingPosition.transform(mainDirection, 3)
            addGroundTendril(mainPosition, delay = 9)
            addGroundTendril(startingPosition.transform(mainDirection, 1).transform(subDirection, 2), delay = 9)
        } else if (count == 1) {
            addGroundTendril(startingPosition.transform(mainDirection, 1), delay = 3)
            addGroundTendril(startingPosition.transform(subDirection, 2), delay = 6)
            addGroundTendril(startingPosition.transform(mainDirection, 4), delay = 9)
            addGroundTendril(startingPosition.transform(mainDirection, 2).transform(subDirection, 2), delay = 12)
        } else {
            addGroundTendril(startingPosition.transform(mainDirection, 1).transform(subDirection, 1), delay = 3)
            addGroundTendril(startingPosition.transform(mainDirection, 1).transform(subDirection, 3), delay = 6)
        }

        combat.combatDelay = combatDefinitions.attackSpeed

        playAnimation(10341)
        position.playSound(id = 7154, delay = 0, radius = 10)
        exactMove(
            secondLocation = targetPosition,
            secondDuration = 30,
            direction = diagonalDirection ?: Direction.fromJagAngle(direction)
        )

        // Calculate the rectangular area between startingPosition and targetPosition
        val minX = minOf(startingPosition.x, targetPosition.x)
        val minY = minOf(startingPosition.y, targetPosition.y)
        val maxX = maxOf(startingPosition.x, targetPosition.x)
        val maxY = maxOf(startingPosition.y, targetPosition.y)

        val southWestPosition = Location(minX, minY, startingPosition.plane)
        val width = maxX - minX + 1 // +1 because positions are inclusive
        val height = maxY - minY + 1

        val occupiedArea = OccupiedArea(
            southWestPosition,
            width,
            height
        )

        return occupiedArea.intersectsWith(actualTargetPosition.occupiedArea)
    }


    private fun Npc.addGroundTendril(position: Location, delay: Int) {
        position.spotanim(2510, delay)
        schedule(delay = 3) {
            position.spotanim(2512, delay)

            val players = position.players
            players.forEach {
                val damage = random(20..25)

                it.applyHit(Hit(damage, HitType.REGULAR))
                applyHit(Hit(damage / 2, HitType.HEALED))
            }
        }
    }

    private fun performAutoAttack(target: Entity): Int {
        val defs: NPCCombatDefinitions = this.getCombatDefinitions()
        val attDefs = defs.attackDefinitions
        if (attDefs != null) {
            val drawback = attDefs.drawbackGraphics
            if (drawback != null)
                this.graphics = drawback
        }
        val maxHit: Int = if (difficulty == DT2BossDifficulty.AWAKENED) 50 else 35
        val damage: Int = getRandomMaxHit(this, maxHit, CombatScript.MELEE, target)
        if (attDefs != null && target is Player) {
            val sound = attDefs.startSound
            if (sound != null)
                target.sendSound(sound)
        }
        val hit = Hit(this, damage, HitType.MELEE)
        delayHit(this, 0, target, hit)
        if (hit.damage > 0 && hit.isAccurate && (target is Player) && !target.prayerManager.isActive(Prayer.PROTECT_FROM_MELEE))
            hit.source.applyHit(Hit(hit.damage / 2, HitType.HEALED))

        this.setAnimation(defs.attackAnim)
        return defs.attackSpeed
    }

    private fun performSwingingAxes(): Int {
        val defs: NPCCombatDefinitions = this.getCombatDefinitions()
        val axes = mutableListOf<SwingingAxe>()
        val player: Player = instance.player

        instance.translatedAxes.entries
            .shuffled()
            .take(getAxeCount())
            .forEach { axes.add(SwingingAxe(instance, it.key)) }

        axes.forEach {
            it.spawn()
            it.faceLocation = instance.getLocation(Location(1129, 3418))
            it seq 10364
        }
        schedule(SwingingAxeTask(instance, player, axes), 0, 0)
        return defs.attackSpeed
    }

    private fun performHeadGaze(): Int {
        val location = instance.player.getOffsetLocationInArena(instance)
        if (location.isEmpty) return 1
        val spawn = VardorvisHead(location.get())
        spawn.spawn()
        spawn.setFaceEntity(instance.player)
        spawn seq 10348
        spawn spotanim 2520

        val defs: NPCCombatDefinitions = this.getCombatDefinitions()
        val head: Optional<VardorvisHead> = Optional.of(spawn)

        HeadGazeProjectile(instance, head.get()).headGazeProjectile()

        instance.player.sendMessage(Colour.STRANGLE_WOOD_PINK.wrap("Vardorvis's head gazes upon you..."))

        schedule(2) { head.get().sendDeath() }

        return defs.attackSpeed
    }

    private fun getAxeCount(): Int =
        if (hitpointsAsPercentage < 50) 3 else 2

    private fun rateForCurrentHP(atk: Attack): Int {
        if (this.hitpointsAsPercentage <= 33) {
            enraged = true
            return atk.getEnrageRate()
        }
        if (this.hitpointsAsPercentage <= 66) {
            return atk.getSecondRate()
        }
        return atk.getFirstRate()
    }

    private fun decreaseOtherCooldowns(lastAttack: Attack) {
        val removals = mutableListOf<Attack>()
        for (cooldown in cooldowns) {
            if (cooldown.key == lastAttack)
                continue
            if (cooldown.value == 0) {
                removals.add(cooldown.key)
                continue
            }
            cooldown.setValue(cooldown.value - 1)
        }

        for (remove in removals)
            cooldowns.remove(remove)
    }

    private fun determineAttack(): Attack {
        val availableAttacks = determineAvailableAttacks()
        val attackOddsPool = mutableListOf<Attack>()
        for (atk in availableAttacks) {
            attackOddsPool.addAll(Collections.nCopies(rateForCurrentHP(atk), atk))
        }
        val currentSize = attackOddsPool.size
        attackOddsPool.addAll(Collections.nCopies(abs(100 - currentSize), AutoAttack))
        instance.debugMechanic(
            "Auto: ${attackOddsPool.filter { it == AutoAttack }.size}, " +
                    "Axes: ${attackOddsPool.filter { it == SwingingAxes }.size}, " +
                    "Gaze: ${attackOddsPool.filter { it == HeadGaze }.size}, " +
                    "Strangle: ${attackOddsPool.filter { it == Strangle }.size}, " +
                    "Spikes: ${attackOddsPool.filter { it == DartingSpikes }.size}"
        )
        if (!instance.player.location.withinDistance(this.position, 1))
            attackOddsPool.removeAll { it is AutoAttack }
        return if (attackOddsPool.isEmpty()) AutoAttack else attackOddsPool.random()
    }

    private fun determineAvailableAttacks(): List<Attack> {
          val potentialAttacks = mutableListOf(SwingingAxes, DartingSpikes, HeadGaze, Strangle)
          potentialAttacks.removeIf {
              !it.getEnabled() || it.getRequiredHp() <= this.hitpointsAsPercentage
          }

          val removals = mutableListOf<Attack>()
          for (atk in potentialAttacks) {
              if (cooldowns[atk] != null && cooldowns[atk]!! > 0)
                  removals.add(atk)
          }

          for (remove in removals)
              potentialAttacks.remove(remove)
        return potentialAttacks
    }

    override fun getCombatDefinitions(): NPCCombatDefinitions {
        val defs = super.getCombatDefinitions()

        val defenseReduction = 70 - ((hitpointsAsPercentage / 100) * 70)
        val baseDefense = when (difficulty) {
            DT2BossDifficulty.NORMAL -> 215
            DT2BossDifficulty.AWAKENED -> 268
            DT2BossDifficulty.QUEST -> 180
        }

        val strengthIncrease = ((hitpointsAsPercentage / 100) * 70)
        val baseStrength = when (difficulty) {
            DT2BossDifficulty.NORMAL -> 270
            DT2BossDifficulty.AWAKENED -> 391
            DT2BossDifficulty.QUEST -> 210
        }

        defs.statDefinitions.set(StatType.DEFENCE, baseDefense - defenseReduction)
        defs.statDefinitions.set(StatType.STRENGTH, baseStrength + strengthIncrease)
        return defs
    }

    override fun onFinish(source: Entity?) {
        super.onFinish(source)
        instance.killAxes = true
        instance.killBleed = true
        instance.player.entangled = false
        instance.player.tinting = Tinting(-1, -1, -1, 0, 0, 0)
        cooldowns.clear()
        setRespawnTask()
    }
}
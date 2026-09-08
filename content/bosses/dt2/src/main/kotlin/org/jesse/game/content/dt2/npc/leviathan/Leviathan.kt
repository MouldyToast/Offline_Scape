package org.jesse.game.content.dt2.npc.leviathan

import org.jesse.game.content.dt2.area.DT2Module
import org.jesse.game.content.dt2.npc.*
import org.jesse.game.content.dt2.npc.leviathan.LeviathanConstants.BOULDER_PROJECTILE
import org.jesse.game.content.dt2.npc.leviathan.LeviathanConstants.FALLING_BOULDER_DELAYS
import org.jesse.game.content.dt2.npc.leviathan.LeviathanConstants.LIGHTNING_PROJECTILE
import org.jesse.game.content.dt2.npc.leviathan.LeviathanConstants.SMOKE_BLAST_DATA
import org.jesse.game.content.dt2.npc.leviathan.LeviathanConstants.STRIKE_BORDER_AREAS
import org.jesse.game.content.dt2.npc.leviathan.LeviathanConstants.TAIL_DATA
import org.jesse.game.content.dt2.npc.leviathan.LeviathanConstants.VOLLEY_SPEEDS
import org.jesse.game.content.skills.prayer.Prayer
import org.jesse.game.model.CameraShakeType
import org.jesse.game.task.WorldTask
import org.jesse.game.util.Direction
import org.jesse.game.world.Position
import org.jesse.game.world.Projectile
import org.jesse.game.world.World
import org.jesse.game.world.entity.*
import org.jesse.game.world.entity.masks.Graphics
import org.jesse.game.world.entity.masks.Hit
import org.jesse.game.world.entity.masks.HitType
import org.jesse.game.world.entity.npc.CombatScriptsHandler
import org.jesse.game.world.entity.npc.NPC
import org.jesse.game.world.entity.npc.NPCCombat
import org.jesse.game.npc.ids.*
import org.jesse.game.world.entity.npc.combat.CombatScript
import org.jesse.game.world.entity.npc.combatdefs.AttackType
import org.jesse.game.world.entity.player.NotificationSettings
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.action.combat.CombatUtilities
import org.jesse.game.world.entity.player.action.combat.magic.CombatSpell
import org.jesse.game.world.`object`.WorldObject
import org.jesse.game.world.region.DynamicArea
import java.lang.ref.WeakReference
import java.util.*
import kotlin.jvm.optionals.getOrNull
import kotlin.math.roundToInt

typealias Npc = NPC

enum class SpecialPhase {
    VOLLEY,
    ENRAGED_VOLLEY,
    LIGHTNING,
    SHADOW;
}

val NPC_SPECIAL_PHASE = "npc_special_phase"
val NPC_SPECIAL_PHASE_NEXT = "npc_special_phase_next"
val NPC_IS_WAITING = "npc_is_waiting"
val LEVIATHAN_STUNNED_COUNTER = "leviathan_stunned_counter"
val LOWEST_HITPOINTS = "lowest_hitpoints"
val LEVIATHAN_SPECIAL_SPEED = "leviathan_special_speed"
val LEVIATHAN_SPECIAL_TICKS = "leviathan_special_ticks"


var NPC.phase by attrEnumNullable<SpecialPhase>(NPC_SPECIAL_PHASE)
var NPC.nextPhase by attrEnumNullable<SpecialPhase>(NPC_SPECIAL_PHASE_NEXT)

val NPC.awakened get() = id == THE_LEVIATHAN_12215
var NPC.waiting by attrOrElse(NPC_IS_WAITING, true)

var NPC.stunnedCounter by attrOrElse(LEVIATHAN_STUNNED_COUNTER, 0)

val Entity.leviathan: NPC?
    get() = World.findNPC(location, 20) {
        it.id == THE_LEVIATHAN || it.id == THE_LEVIATHAN_12215
    }.getOrNull()
val Player.awakenedEncounter: Boolean
    get() = leviathan?.awakened == true

var NPC.lowestHitpoints by attrOrElse(LOWEST_HITPOINTS, 10_000)
val NPC.lowestHitpointsAsPercentage: Int
    get() = (lowestHitpoints.toDouble() / maxHitpoints * 100).toInt()

val NPC.isEnraged get() = lowestHitpointsAsPercentage <= 20
var NPC.specialSpeed by attrOrElse(LEVIATHAN_SPECIAL_SPEED, -2)
var NPC.specialTicks by attrOrElse(LEVIATHAN_SPECIAL_TICKS, -10)

/**!
 * @author Khaled Abdeljaber
 */
class Leviathan(id: Int, position: Location, val instance: LeviathanInstance) : NPC(id, position, Direction.WEST, 0),
    CombatScript {

    init {
        setAttackDistance(64)
        setDeathDelay(4)
        this.hitBar = object : EntityHitBar(this) {
            override fun getType(): Int {
                return 19
            }
        }
        combat = object : NPCCombat(this) {
            override fun combatAttack(): Int {
                if (target == null) {
                    return 0
                }
                addAttackedByDelay(target)
                return CombatScriptsHandler.specialAttack(npc, target)
            }
        }
    }

    private var combatTask: WorldTask? = null
    private var lightningTask: WorldTask? = null
    private val boulders = mutableSetOf<WorldObject>()

    override fun canAttack(source: Player?): Boolean {
        return super.canAttack(source)
    }

    override fun canAttackInSingleZone(target: Entity?): Boolean {
        return super.canAttackInSingleZone(target)
    }

    override fun sendNotifications(player: Player?) {}
    override fun processEntity() {
        super.processEntity()

        if (!isEnraged || isDead || waiting) return

        specialTicks++
        if (specialTicks < 5) {
            return
        }

        specialTicks = 0
        performBoulderFall(first = false, resetCombat = false)
    }

    override fun attack(target: Entity): Int {
        if (waiting || target !is Player || isStunned) {
            return 1
        }

        if (combatTask?.exists() == true) {
            return 1
        }

        when (phase) {
            SpecialPhase.VOLLEY, SpecialPhase.ENRAGED_VOLLEY -> {
                return if (!middleLocation.withinDistance(target.position, 5)) {
                    performVolleyAttack(target)
                } else {
                    val roll = random(0..3)
                    when (roll) {
                        0 -> meleeHitPrimary(target)
                        1 -> meleeHitSecondary(target)
                        else -> performVolleyAttack(target)
                    }
                }
            }

            else -> {}
        }
        return 1
    }

    private fun meleeHitPrimary(target: Entity): Int {
        setFaceEntity(target)

        animation = LeviathanConstants.LEVIATHAN_BITE
        CombatUtilities.delayHit(
            this,
            0,
            target,
            Hit(this, CombatUtilities.getRandomMaxHit(this, 38, AttackType.MELEE, target), HitType.MELEE)
        )

        return 4
    }

    private fun meleeHitSecondary(target: Entity): Int {
        setFaceEntity(target)

        animation = LeviathanConstants.LEVIATHAN_BITE_2
        CombatUtilities.delayHit(
            this,
            0,
            target,
            Hit(this, CombatUtilities.getRandomMaxHit(this, 38, AttackType.MELEE, target), HitType.MELEE)
        )

        return 4
    }

    private fun performVolleyAttack(target: Player): Int {
        val index = when {
            isEnraged -> VOLLEY_SPEEDS.size - 3
            else -> specialSpeed.coerceIn(0, VOLLEY_SPEEDS.size - 1)
        }
        val volley = VOLLEY_SPEEDS[index]
        val first = specialSpeed == -2 && nextPhase == null

        specialSpeed = (specialSpeed + 1).coerceIn(0, VOLLEY_SPEEDS.size - 1)

        val attackSpeed = volley.a
        val startCycle = volley.b
        val lengthAdjustment = volley.c
        val amount = volley.d
        val melee = volley.e

        return performSpecialVolley(
            target,
            if (isEnraged) Short.MAX_VALUE.toInt() else amount,
            attackSpeed,
            startCycle,
            lengthAdjustment,
            !isEnraged && melee,
            first
        )
    }

    private fun NPC.performSpecialVolley(
        target: Player,
        amount: Int,
        ticks: Int,
        startCycle: Int,
        lengthAdjustment: Int,
        melee: Boolean,
        first: Boolean
    ): Int {
        setFaceEntity(target)

        val fast = ticks == 1
        var volleyCount = 0

        combatTask?.stop()


        val combatTask = object : WorldTask {
            private var tickCounter = if (fast) -1 else 0
            private var projectileDelay = 0

            override fun run() {
                if (volleyCount >= amount) {
                    animation = null
                    performBoulderFall(first)
                    stop()
                    return
                }

                if (tickCounter == -1) {
                    animation = LeviathanConstants.LEVIATHAN_LAUNCH_ORB_START
                } else if (tickCounter == 0) {
                    // Play the initial animation based on whether it's fast or slow
                    if (fast) {
                        animation = LeviathanConstants.LEVIATHAN_LAUNCH_ORB_END_FAST
                    } else {
                        animation = LeviathanConstants.LEVIATHAN_LAUNCH_ORB_START
                    }

                    val types =
                        if (melee) LeviathanConstants.VolleyType.ALL else LeviathanConstants.VolleyType.DISTANCED
                    val type = types.random()
                    graphics = (if (!fast || volleyCount == 0) type.start else type.follow)
                    target.sendSound(type.sound)

                    val projectile = Projectile(
                        type.projectile.id,
                        137,
                        25,
                        if (volleyCount == 0) startCycle else startCycle,
                        30,
                        lengthAdjustment,
                        124,
                        0
                    )

                    projectileDelay = projectile.build(getFaceLocation(target, size), target)
                    schedule(delay = projectileDelay + 1) {
                        if (!target.location.withinDistance(this@Leviathan.position, 64)) return@schedule
                        val damage = target.getBallDamage(type.hitType)
                        val hit = Hit(
                            this@Leviathan,
                            damage,
                            true,
                            if (damage > 0) type.hitType else HitType.MISSED,
                            0
                        )
                        hit.isForcedHitsplat = true
                        target.applyHit(hit)

                        if (damage > 0) {
                            target.graphics = Graphics(type.impact.id, 0, 100)
                        }
                    }
                } else if (tickCounter == 1 && !fast) {
                    animation = LeviathanConstants.LEVIATHAN_LAUNCH_ORB_END_SINGLE
                }

                tickCounter++

                if (!fast) {
                    if (tickCounter >= ticks) {
                        tickCounter = 0
                        volleyCount++
                    }
                } else {
                    tickCounter = 0
                    volleyCount++
                }
            }
        }
        this@Leviathan.combatTask = schedule(combatTask, 0, 0, target)
        return 30
    }

    fun Player.getBallDamage(type: HitType): Int {
        val base = random(10..30)
        var drain = if (isEnraged) if (insidePathfinder(null)) 0.0 else 0.20 else 0.0

        if (type == HitType.MELEE) {
            if (!prayerManager.isActive(Prayer.PROTECT_FROM_MELEE)) {
                drain = 1.0
            }
        } else if (type == HitType.RANGED) {
            if (!prayerManager.isActive(Prayer.PROTECT_FROM_MISSILES)) {
                drain = 1.0
            }
        } else if (type == HitType.MAGIC) {
            if (!prayerManager.isActive(Prayer.PROTECT_FROM_MAGIC)) {
                drain = 1.0
            }
        }

        drain = drain.coerceAtLeast(0.0)
        return (base * drain).toInt()
    }

    override fun postHitProcess(hit: Hit?) {
        super.postHitProcess(hit)

        instance.players.forEach {
            it.hpHud.updateValue(getHitpoints())
        }
    }

    fun startEngagement() {
        instance.players.forEach {
            combat.target = it
            it.sendSound(3906)
        }

        lowestHitpoints = maxHitpoints
        stunnedCounter = 0

        specialSpeed = -2
        specialTicks = -10
        phase = SpecialPhase.VOLLEY
        nextPhase = null
    }

    fun Npc.performBoulderFall(first: Boolean, resetCombat: Boolean = true) {
        if (resetCombat) {
            animation = LeviathanConstants.LEVIATHAN_ROCKFALL
            if (first) {
                selectNextPhase(false)
            }
        }

        val positions = mutableSetOf<Location>()
        repeat(random(15..18)) {
            val candidate = findAreaRandom(LeviathanFightArea::class, 200, instance) {
                !positions.contains(it) && it.isTileFree
            } ?: return
            positions.add(Location(candidate.x, candidate.y, 0))
        }

        positions.forEach { playFallingBoulder(it, permanent = false, always = false) }

        instance.players?.forEach {
            if (resetCombat) {
                it.queueBoulderFallVisuals()
            }
            performTargetBoulder(it)
        }

        if (!resetCombat) {
            return
        }

        schedule(delay = 5) {
            combat.combatDelay = 0
            attackingDelay = 0
        }
    }

    private fun Player.queueBoulderFallVisuals() {
        sendSound(LeviathanConstants.LEVIATHAN_BOULDER_FALLING_START_SOUND)
        packetDispatcher.resetCamera()
        packetDispatcher.sendCameraShake(
            CameraShakeType.LEFT_AND_RIGHT, 7, 0, 0
        )
        packetDispatcher.sendCameraShake(
            CameraShakeType.UP_AND_DOWN, 7, 0, 0
        )
        packetDispatcher.sendCameraShake(
            CameraShakeType.FRONT_AND_BACK, 5, 0, 0
        )

        schedule(delay = 3) {
            packetDispatcher.resetCamera()
        }
    }

    private fun Npc.selectNextPhase(skipHint: Boolean) {
        val nextChoices = listOf(SpecialPhase.LIGHTNING, SpecialPhase.SHADOW)

        if (skipHint) {
            val first = nextPhase == null
            if (first) {
                this.nextPhase = nextChoices.random()
            } else if (nextPhase == SpecialPhase.LIGHTNING) {
                this.nextPhase = SpecialPhase.SHADOW
            } else {
                this.nextPhase = SpecialPhase.LIGHTNING
            }
        } else {
            val first = nextPhase == null
            if (first) {
                this.nextPhase = nextChoices.random()
            }

            performBoulderHint()
            selectNextPhase(true)
        }
    }

    private fun Npc.performBoulderHint() {
        if (nextPhase == SpecialPhase.SHADOW) {
            playFallingBoulder(instance[2081, 6379, 0], permanent = true, always = true)
            playFallingBoulder(instance[2081, 6365, 0], permanent = true, always = true)
        } else {
            playFallingBoulder(instance[2073, 6372, 0], permanent = true, always = true)
            playFallingBoulder(instance[2089, 6372, 0], permanent = true, always = true)
        }
    }

    private fun Npc.performTargetBoulder(target: Player) {
        // There is always chip damage from the boulders
        target.applyHit(
            Hit(random(5..10), HitType.DEFAULT)
        )

        playFallingBoulder(target.position.copy(), permanent = true, always = false)

        if (awakened) {
            for (direction in Direction.mainDirections) {
                val position = target.position.transform(direction)
                playFallingBoulder(position, permanent = true, always = false)
            }
        }
    }

    private fun Npc.playFallingBoulder(candidate: Location, permanent: Boolean, always: Boolean): Boolean {
        val position = if (always && candidate.containsObjShape(10)) {
            candidate.randomSafeNearest(radius = 4)
        } else candidate

        if (position.containsObjShape(10)) {
            return false
        }

        val (orientation, gfx) = if (permanent) {
            val orientation = random(0..3)
            orientation to when (orientation) {
                0 -> LeviathanConstants.LEVIATHAN_BOULDER_FALLS_THEN_STAYS_0
                1 -> LeviathanConstants.LEVIATHAN_BOULDER_FALLS_THEN_STAYS_1
                2 -> LeviathanConstants.LEVIATHAN_BOULDER_FALLS_THEN_STAYS_2
                else -> LeviathanConstants.LEVIATHAN_BOULDER_FALLS_THEN_STAYS_3
            }
        } else {
            val orientation = random(0..1)
            orientation to when (orientation) {
                0 -> LeviathanConstants.LEVIATHAN_BOULDER_FALLS_THEN_BREAKS_A
                else -> LeviathanConstants.LEVIATHAN_BOULDER_FALLS_THEN_BREAKS_B
            }
        }

        val delay = if (permanent) 50 else FALLING_BOULDER_DELAYS.random()
        position.spotanim(gfx.id, delay = delay)
        addBoulderOnFloor(position, gfx.def.sequenceDelay, permanent, orientation)
        return true
    }

    fun Npc.addBoulderOnFloor(position: Location, duration: Int, persistent: Boolean, orientation: Int = -1) {
        val orientationNormalized = if (orientation == -1) 0 else orientation

        if (orientation == -1) {
            position.spotanim(LeviathanConstants.LEVIATHAN_BOULDER_SHADOW.id, delay = duration)
        }
        position.playSound(LeviathanConstants.LEVIATHAN_SHADOW_BOULDER_LAND_SOUND.id, delay = 5)

        schedule(delay = clientTicksToGameTicks(duration)) {
            if (persistent && !isDead) {
                WorldObject(
                    47590,
                    10,
                    orientationNormalized,
                    position
                ).spawn()

                WorldObject(
                    26209,
                    10,
                    orientationNormalized,
                    position
                ).spawn()

                val obj = WorldObject(
                    47590,
                    10,
                    orientationNormalized,
                    position
                )

                obj.spawn()
                boulders.add(obj)
            }

            position.players.forEach {
                val position = position.randomSafeNearest(radius = 5) {
                    it.inArea(LeviathanFightArea::class, instance)
                }

                it.animation = LeviathanConstants.HUMAN_HIT_WITH_OBSTACLE_FOOT
                it.applyHit(
                    Hit(
                        random(10..30),
                        HitType.DEFAULT
                    )
                )
                it.setLocation(position)
                it.exactMove(
                    secondLocation = position,
                    firstDuration = 0,
                    secondDuration = 30,
                    direction = position.direction(it.position)
                )
            }
        }
    }

    private fun Npc.spawnAbyssalPathfinder() {
        val target = instance.players.randomOrNull() ?: return
        val location = target.position.copy()
        val position = LeviathanAbyssalPathfinder.findNearestPosition(location, instance)

        repeat(3) {
            for (direction in Direction.cardinalDirections) {
                position.transform(direction)
                    .spotanim(LeviathanConstants.LEVIATHAN_ABYSSAL_PATHFINDER.id, height = 124, delay = (it + 1) * 30)
            }
        }

        schedule(delay = 2) {
            instance.players.forEach {
                it.sendMessage("<col=ef0083>The Leviathan focuses on you intensely...</col>")
            }

            LeviathanAbyssalPathfinder(
                position, this@Leviathan, instance
            ).spawn()
        }
    }

    private fun Player.insidePathfinder(pathfinder: Npc?): Boolean {
        val pathfinder = pathfinder ?: findNpc(ABYSSAL_PATHFINDER) ?: return false

        val radius = if (awakenedEncounter) 5 else 3
        return pathfinder.location.withinDistance(location, radius)
    }

    fun Player.checkAbyssalPathfinder(npc: NPC) {
        if (insidePathfinder(npc)) {
            tinting = Tinting(
                9, 7, 70, 70, 0, 30
            )
        } else {
            tinting = LeviathanConstants.RESET_TINTING
        }
    }

    private fun Player.spawnTornado() {
        val position = location.random(radius = 6) {
            it.inArea(LeviathanFightArea::class, instance) && location.withinDistance(it, 4)
        }
        LeviathanTornado(
            WeakReference(this@spawnTornado),
            WeakReference(this@Leviathan),
            position,
            instance
        ).spawn()
    }


    override fun getXpModifier(hit: Hit): Float {
        if (phase == SpecialPhase.ENRAGED_VOLLEY) {
            val source = hit.source as? Player ?: return super.getXpModifier(hit)
            val pathfinder = source.insidePathfinder(null)
            if (pathfinder) {
                hit.setGuaranteed()
            }
        }

        return super.getXpModifier(hit)
    }

    fun onScheduledAttack(player: Player, spell: CombatSpell?) {
        if (!isStunnable(spell)) {
            return
        }

        schedule(delay = 1) {
            if (!isStunnable(spell)) {
                return@schedule
            }

            stun(player)
        }
    }

    override fun handleIngoingHit(hit: Hit) {
        super.handleIngoingHit(hit)

        if (waiting) {
            return
        } else {
            if (!checkForStunnedHit(hit, first = true)) {
                checkForDamageReduction(hit)
            }

            val before = lowestHitpointsAsPercentage
            lowestHitpoints = minOf(lowestHitpoints, hitpoints - hit.damage)
            if (lowestHitpoints < 0) {
                lowestHitpoints = hitpoints
            }
            // Every 25% of the Leviathan's health, we reduce the attack speed of the special attack
            val after = lowestHitpointsAsPercentage
            if (awakened) {
                if (before > 50 && after <= 50) {
                    instance.players?.forEach {
                        it.spawnTornado()
                    }
                }
            }

            if (before > 20 && after <= 20) {
                enrage()
            }
        }
    }

    private fun retaliateWithSpecial(hit: Hit) {
        hit.setGuaranteed()
        animation = LeviathanConstants.LEVIATHAN_LAUNCH_ORB_END_STUNNED

        val instance = instance
        instance.players.forEach {
            it.sendMessage("<col=06600c>You hit the Leviathan right in its weak spot!</col>")
        }

        for (data in TAIL_DATA) {
            val tail = instance[data.b].findNpc()
            tail?.animation = data.d
        }

        val target = hit.source as? Player ?: instance.players.randomOrNull() ?: return

        selectNextPhase(true)

        if (nextPhase == SpecialPhase.SHADOW) {
            performSmokeBarrage(target)
        } else {
            performSpecialLightning(target)
        }
    }

    fun Npc.performSmokeBarrage(target: Player) {
        phase = SpecialPhase.SHADOW

        val combatTask = object : WorldTask {
            private var stage = 0
            private var repeatCount = 0

            override fun run() {
                when (stage) {
                    0 -> {
                        setFaceEntity(target)
                        animation = LeviathanConstants.LEVIATHAN_ROCKFALL
                        instance.players?.forEach {
                            it.sendMessage("<col=a53fff>The Leviathan begins to spit out debris...")
                            it.sendSound(
                                SoundEffect.get(
                                    LeviathanConstants.LEVIATHAN_BOULDER_FALLING_START_SOUND.id,
                                    5
                                )
                            )
                        }
                        stage++
                    }

                    1 -> if (repeatCount < 3) {
                        repeatCount++
                    } else {
                        repeatCount = 0

                        animation = LeviathanConstants.LEVIATHAN_SHADOW_BOULDER_START
                        instance.players?.forEach {
                            it.sendSound(LeviathanConstants.LEVIATHAN_SHADOW_BOULDER_START_SOUND)
                        }
                        stage++
                    }

                    2 -> {
                        if (repeatCount < 9) {
                            val final = repeatCount == 8

                            animation =
                                if (final) LeviathanConstants.LEVIATHAN_SHADOW_BOULDER_END else LeviathanConstants.LEVIATHAN_LAUNCH_SHADOW_BOULDER
                            val targetPosition = target.position.copy()
                            val sourcePosition = getFaceLocation(target, size)

                            BOULDER_PROJECTILE.build(sourcePosition, targetPosition)
                            instance.players?.forEach {
                                it.sendSound(LeviathanConstants.LEVIATHAN_SHADOW_BOULDER_LAUNCH_SOUND)
                            }
                            addBoulderOnFloor(
                                targetPosition, BOULDER_PROJECTILE.getProjectileDuration(
                                    sourcePosition, targetPosition
                                ), true
                            )
                            repeatCount++
                            if (final) {
                                stage++
                            }
                        } else {
                            stage++
                        }
                    }

                    3, 4 -> {
                        stage++
                    }

                    5 -> {
                        animation = LeviathanConstants.LEVIATHAN_LIGHTNING_BARRAGE_END
                        graphics = LeviathanConstants.LEVIATHAN_SMOKE_BARRAGE

                        SMOKE_BLAST_DATA.values.forEach { (gfx, position, delay) ->
                            val pos = instance[position]
                            if (pos.hasLineOfSightTo(this@performSmokeBarrage) || hasLineOfSightTo(pos)) {
                                pos.spotanim(id = gfx.id, height = 0, delay = delay)
                            }
                        }
                        stage++
                        repeatCount = 0
                    }

                    6 -> {
                        if (repeatCount < 3) {
                            checkForPlayer(instance, repeatCount)
                            repeatCount++
                        } else {
                            setFaceEntity(null)
                            phase = SpecialPhase.VOLLEY
                            stop() // End task after a delay
                        }
                    }
                }
            }
        }
        combatTask.run()
        this@Leviathan.combatTask = schedule(combatTask, 0, 0, target)
    }

    fun Npc.checkForPlayer(instance: DynamicArea, gameTickDelay: Int) {
        instance.players.filter {
            val reverted = instance.get(it.position, static = false)
            val data = SMOKE_BLAST_DATA[reverted] ?: return@filter false
            val clientTickDelay = clientTicksToGameTicks(data.third)

            gameTickDelay == clientTickDelay && hasLineOfSightTo(it.position)
        }.forEach {
            it.applyHit(
                Hit(
                    random(10..30),
                    HitType.DEFAULT
                )
            )
        }
    }


    private fun Npc.performSpecialLightning(target: Player) {
        phase = SpecialPhase.LIGHTNING

        val combatTask = object : WorldTask {
            private var stage = 0
            private var angle = jagAngle
            private var repeatCount = 0

            override fun stop() {
                super.stop()
                lightningTask?.stop()
            }

            override fun run() {
                when (stage) {
                    0 -> {
                        instance.players?.forEach {
                            it.sendMessage("<col=a53fff>The Leviathan charges up a lightning attack...")
                            it.sendSound(LeviathanConstants.LEVIATHAN_ELECTRICITY_START_SOUND)
                        }
                        setFaceEntity(null)

                        animation = LeviathanConstants.LEVIATHAN_LIGHTNING_BARRAGE_START
                        graphics = LeviathanConstants.LEVIATHAN_LIGHTNING_BARRAGE_START_GFX

                        launchLightningTimer()
                        stage++
                    }

                    1 -> {
                        if (repeatCount < 2) {
                            val targetAngle = jagAngle(target)
                            angle = angle.setJagAngle(targetAngle, 300)
                            faceAngle(angle.toInt())
                            repeatCount++
                        } else {
                            faceAngle(angle.toInt())
                            stage++
                        }
                    }

                    2 -> {
                        if (repeatCount < 35) {
                            shootExpandingElectricity(angle, target)
                            val targetAngle = jagAngle(target)
                            angle = angle.setJagAngle(targetAngle, 100)
                            faceAngle(angle.toInt())
                            repeatCount++
                        } else {
                            animation = LeviathanConstants.LEVIATHAN_LIGHTNING_BARRAGE_END
                            stage++
                        }
                    }

                    3 -> {
                        phase = SpecialPhase.VOLLEY
                        stop()
                    }
                }
            }
        }
        combatTask.run()
        this@Leviathan.combatTask = schedule(combatTask, 0, 0)
    }

    fun Npc.shootExpandingElectricity(directionAngle: JagAngle, target: Player) {
        animation = LeviathanConstants.LEVIATHAN_LIGHTNING_BARRAGE_CONTINUE
        graphics = LeviathanConstants.LEVIATHAN_LIGHTNING_BARRAGE_START_GFX

        val centerPosition = middleLocation
        var width = 1
        var stepCounter = 0

        val angleTranslation = 200

        val allPositions = mutableSetOf<Location>()
        for (depth in 1..15) {
            val positions = mutableListOf<Location>()

            val forwardPosition = centerPosition.transformAngle(directionAngle.toInt(), depth)
            positions.add(forwardPosition)

            if (width >= 2) {
                val rightAngle = directionAngle.setJagAngle(directionAngle.toInt() + angleTranslation)
                val rightPosition = forwardPosition.transformAngle(rightAngle.toInt(), 1)
                positions.add(rightPosition)
            }

            if (width >= 3) {
                val leftAngle = directionAngle.setJagAngle(directionAngle.toInt() - angleTranslation)
                val leftPosition = forwardPosition.transformAngle(leftAngle.toInt(), 1)
                positions.add(leftPosition)
            }

            if (width >= 4) {
                val rightPosition2 = forwardPosition.transformAngle(directionAngle.toInt() + 2 * angleTranslation, 2)
                positions.add(rightPosition2)
            }

            if (width >= 5) {
                val leftPosition2 = forwardPosition.transformAngle(directionAngle.toInt() - 2 * angleTranslation, 2)
                positions.add(leftPosition2)
            }

            for (pos in positions) {
                if (width == 1 || pos.inArea(LeviathanFightArea::class, instance)) {
                    pos.spotanim(LeviathanConstants.LEVIATHAN_GROUND_ELECTRICITY.id, delay = 2 + (depth * 2))
                    allPositions.add(pos)
                }
            }

            stepCounter++
//        if (stepCounter != (if (width == 1) 2 else 3)) continue
            if (stepCounter != 3) continue

            width += 1
            stepCounter = 0
        }

        // Apply damage to players within the electricity effect area
        instance.players?.forEach {
            val location = it.nextLocation?.copy() ?: it.position.copy()
            if (location in allPositions) {
                it.applyHit(
                    Hit(
                        random(20..30),
                        HitType.DEFAULT
                    )
                )
            }
            it.sendSound(LeviathanConstants.LEVIATHAN_ELECTRICITY_SHOT_SOUND)
        }
    }

    fun Npc.launchLightningTimer() {
        lightningTask?.stop()
        val lightningTask = object : WorldTask {
            var ticks = 0
            override fun run() {
                if (phase != SpecialPhase.LIGHTNING) {
                    return stop()
                }

                if (ticks % 4 == 0) {
                    shootElectricityBall()
                }
                ticks++
            }
        }
        this@Leviathan.lightningTask = schedule(lightningTask, 0, 0)
    }

    fun Npc.shootElectricityBall() {
        val areas = mutableSetOf<Location>()
        STRIKE_BORDER_AREAS.forEach { area ->
            repeat(random(1..3)) {
                findAreaRandom(area, 300, instance) {
                    !areas.contains(it) && it.isTileFree
                }?.let { position -> areas.add(position) }
            }
        }

        areas.forEach { position ->
            launchLightingStrike(instance, position)
        }

        if (randomRoll(3, 1)) {
            instance.players.forEach {
                val inside = STRIKE_BORDER_AREAS.any { area ->
                    it.position.inArea(area, instance)
                }
                if (!inside) return@forEach
                launchLightingStrike(instance, it.position.copy())
            }
        }
    }

    private fun Npc.launchLightingStrike(instance: DynamicArea, targetPosition: Location) {
        val sourcePosition = middleLocation
        val projectile = LIGHTNING_PROJECTILE.build(sourcePosition, targetPosition)
        val delay = LIGHTNING_PROJECTILE.getProjectileDuration(sourcePosition, targetPosition)

        targetPosition.spotanim(LeviathanConstants.LEVIATHAN_BOULDER_SHADOW.id, delay = delay - 60)
        targetPosition.spotanim(LeviathanConstants.LEVIATHAN_LIGHTNING_STRIKE.id, delay = delay)
        targetPosition.playSound(LeviathanConstants.LEVIATHAN_LIGHTNING_STRIKE_SOUND.id, delay = delay, radius = 3)

        schedule(delay = projectile) {
            targetPosition.players.forEach {
                it.applyHit(
                    Hit(
                        random(20..30),
                        HitType.DEFAULT
                    )
                )
            }
        }
    }

    private fun Npc.checkForDamageReduction(hit: Hit) {
        // Damage dealt to the Leviathan is reduced by 33% for the duration of each special attack.
        if (phase != SpecialPhase.LIGHTNING && phase != SpecialPhase.SHADOW) return
        hit.damage = (hit.damage * (1.0 - 0.33)).roundToInt()
    }

    private fun Npc.checkForStunnedHit(hit: Hit, first: Boolean): Boolean {
        if (!isStunned || phase != SpecialPhase.VOLLEY) {
            return false
        }

        val target = hit.source as? Player ?: instance.players?.firstOrNull() ?: return false
        val backAngle = jagAngle.inverse()
        val myJagAngle = if (first) jagAngle(target) else jagAngle(target.lastLocation)

        if (myJagAngle.isWithinRange(backAngle, 650)) {
            retaliateWithSpecial(hit)
        } else if (first) {
            checkForStunnedHit(hit, first = false)
        } else {
            hit.damage = hit.damage.coerceAtMost(10)
        }

        return true
    }

    private fun Npc.isStunnable(spell: CombatSpell?): Boolean {
        if (phase != SpecialPhase.VOLLEY || specialSpeed < 0 || isStunned) {
            return false
        }
        if (spell == null || !spell.isShadow) return false
        return true
    }

    fun Npc.enrage() {
        combatTask?.stop()

        performBoulderFall(false)
        spawnAbyssalPathfinder()
        phase = SpecialPhase.ENRAGED_VOLLEY
    }

    fun Npc.stun(target: Player) {
        combatTask?.stop()
        animation = LeviathanConstants.LEVIATHAN_LAUNCH_ORB_END_STUNNED

        setFaceEntity(null)
        faceEntity(target)

        stunnedCounter++
        removeStun()
        stun(if (awakened) 8 else 15)
        reduceVolleySpeed()

        instance.players?.forEach {
            it.sendMessage("<col=06600c>Your spell stuns the Leviathan!</col>")
        }
    }

    private fun Npc.reduceVolleySpeed() {
        specialSpeed = (specialSpeed - 3).coerceAtLeast(-1) // It's really two be we increment by one prior
    }

    override fun getMeleePrayerMultiplier(): Double {
        return 0.3
    }

    override fun isProjectileClipped(target: Position?, closeProximity: Boolean): Boolean {
        return false
    }

    override fun isMovementRestricted(): Boolean {
        return true
    }

    override fun remove() {
        super.remove()
        combatTask?.stop()
    }

    override fun spawn(): NPC {
        if (instance.isConstructed) {
            instance.constructed(this)
            return this
        }
        return super.spawn()
    }

    fun respawn() {
        try {
            super.spawn()
        } catch (e: Exception) {
        }
    }

    override fun onDeath(source: Entity?) {
        super.onDeath(source)

        combatTask?.stop()

        val npcs = position.findNpcs(radius = 30) {
            id == ABYSSAL_PATHFINDER || id == 12222
        }
        npcs.forEach {
            it.remove()
        }

        boulders.forEach {
            it.position.spotanim(LeviathanConstants.LEVIATHAN_BOULDER_IMPACT_BREAK)
            it.remove()
        }
        boulders.clear()

        instance.players?.forEach {
            val name = if (awakened) "awakened leviathan" else "leviathan"
            if (NotificationSettings.isKillcountTracked(name)) {
                it.notificationSettings.increaseKill(name)
                if (NotificationSettings.BOSS_NPC_NAMES.contains(name.lowercase(Locale.getDefault())))
                    it.notificationSettings.sendBossKillCountNotification(name)
            }
            DT2Module.updateLeviathanStatistics(it.bossTimer.currentTracker, awakened)
            it.bossTimer.finishTracking(name)
        }
    }

    override fun setFaceEntityCombat(entity: Entity?) {

    }

    override fun setFaceEntity(entity: Entity?) {
        super.setFaceEntity(entity)
    }

    override fun isForceAggressive(): Boolean {
        return true
    }

    private val CombatSpell.isShadow: Boolean
        get() = this == CombatSpell.SHADOW_BARRAGE ||
                this == CombatSpell.SHADOW_BLITZ ||
                this == CombatSpell.SHADOW_BURST ||
                this == CombatSpell.SHADOW_RUSH
}
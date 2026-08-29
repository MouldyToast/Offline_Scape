package com.near_reality.game.content.dt2.npc.theduke

import com.near_reality.game.content.dt2.area.DT2Module
import com.near_reality.game.content.dt2.area.DukeSucellusInstance
import com.near_reality.game.content.dt2.npc.*
import com.near_reality.game.content.offset
import com.zenyte.game.content.skills.prayer.Prayer
import com.zenyte.game.item.ItemId
import com.zenyte.game.task.TickTask
import com.zenyte.game.task.WorldTasksManager.schedule
import com.zenyte.game.util.Direction
import com.zenyte.game.util.Utils
import com.zenyte.game.world.Projectile
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.SoundEffect
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.combat.CombatScript
import com.zenyte.game.world.entity.player.NotificationSettings
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.calog.CAType
import java.util.*

/**
 * Mack wrote original logic - Kry rewrote in NR terms
 * @author John J. Woloszyk / Kryeus
 * @date 8.14.2024
 */
class DukeSucellusEntity(val arena: DukeSucellusInstance) :
    NPC(12166, arena.getLocation(3036, 6452), Direction.SOUTH, 0), CombatScript {

    val difficulty: DT2BossDifficulty = arena.difficulty
    var specialAttackActive: Boolean = false

    var attackCounter = 0

    val slumbering get() = id == 12166


    override fun postInit() {
        setShouldUpdateOptionsMask(true)
    }

    override fun performDefenceAnimation(attacker: Entity?) {}
    override fun sendNotifications(player: Player?) {}

    fun disturb(player: Player) {
        schedule(object : TickTask() {
            override fun run() {
                when (ticks) {
                    0 -> {
                        player.sendMessage("<col=ff289d>Duke Sucellus awakens...")
                        setTransformation(difficulty.getTransformation())
                        player.inventory.container.findAllById(ItemId.ARDERMUSCA_POISON).values.forEach {
                            player.inventory.container.remove(
                                it
                            )
                        }
                        player.packetDispatcher.sendMusic(763)
                        setAnimation(Animation(10179))
                        setHitpoints(difficulty.getHitpoints())
                        player.hpHud.open(difficulty.getTransformation(), difficulty.getHitpoints())
                        val awakened = false
                        if (awakened) {
                            player.bossTimer.startTracking("awakened duke")
                        } else {
                            player.bossTimer.startTracking("duke")
                        }
                        combat = DukeCombatController(this@DukeSucellusEntity)
                        possibleTargets.add(arena.player)
                        combat.combatDelay = 5
                        aggressionDistance = 15
                        maxDistance = 200
                        isForceAggressive = true
                        attackCounter = 0
                        specialAttackActive = false
                        setAttackDistance(20)
                        arena.dukeAwakened()
                    }

                    1 -> isForceAttackable = true
                    3 -> stop()
                }
                ticks++
            }
        }, 0, 0)
    }

    override fun postHitProcess(hit: Hit?) {
        super.postHitProcess(hit)
        arena.player.hpHud.updateValue(getHitpoints())
    }

    override fun isLocked(): Boolean =
        id == 12166 || super.isLocked()

    override fun isDead(): Boolean {
        if (slumbering) return false
        return super.isDead()
    }

    override fun onDeath(source: Entity?) {
        super.onDeath(source)
        if (source is Player) {
            val kills = source.getKillcount(this)
            if (kills > 0)
                source.combatAchievements.complete(CAType.DUKE_SUCELLUS_ADEPT)
            if (kills >= 50)
                source.combatAchievements.complete(CAType.DUKE_SUCELLUS_MASTER)
            if (difficulty == DT2BossDifficulty.AWAKENED)
                source.combatAchievements.complete(CAType.DUKE_SUCELLUS_SLEEPER)

            val awakened = difficulty == DT2BossDifficulty.AWAKENED
            val name = if (awakened) "awakened duke sucellus" else "duke sucellus"
            source.bossTimer.finishTracking(name)
            DT2Module.updateDukeStatistics(source.bossTimer.currentTracker, awakened = awakened)

            if (NotificationSettings.isKillcountTracked(name)) {
                source.notificationSettings.increaseKill(name)
                if (NotificationSettings.BOSS_NPC_NAMES.contains(name.lowercase(Locale.getDefault())))
                    source.notificationSettings.sendBossKillCountNotification(name)
            }
        }
    }

    override fun onFinish(source: Entity?) {
        if (isFinished) return

        try {
            try {
                val p = source as Player
                sendNotifications(p)
                drop(middleLocation offset Pair(0, -4))
            } catch (_: Exception) {
            }
            isFinished = true
            routeEvent = null
            interactingWith = null
            World.updateEntityChunk(this, true)
            lastChunkId = -1
            if (!interactingEntities.isEmpty()) {
                interactingEntities.clear()
            }
        } catch (_: Throwable) {
        }
        reset()
        setTransformation(12166)
        combat.reset()
        arena.dukeGoodnight()
        arena.player.hpHud.close()
        World.removeNPC(this)
        spawn()
    }

    override fun isAttackable(e: Entity): Boolean =
        !slumbering

    private fun Location.safeFromMage(): Boolean =
        this.y == arena.leftBound.y &&
                (this.x == arena.leftBound.x ||
                        this.x == arena.leftBound.x - 1 ||
                        this.x == arena.rightBound.x ||
                        this.x == arena.rightBound.x + 1)

    override fun attack(target: Entity): Int {
        if (target !is Player) return 0

        if (specialAttackActive) {
            target.sendDeveloperMessage("Special Attack in progress")
            return 0
        }
        if (target.location.safeFromMage()) {
            meleeAttack(target, reduced = true)
            return getAttackDelay()
        }
        if (!isWithinMeleeDistance(this, target)) {
            magicAttack(target)
            return getAttackDelay()
        }
        meleeAttack(target)
        return getAttackDelay()
    }

    private fun getAttackDelay(): Int {
        val targetHp = (0.25 * maxHitpoints).toInt()
        return if (hitpoints < targetHp) 4 else 5
    }

    override fun canMove(fromX: Int, fromY: Int, direction: Int): Boolean = false

    private fun meleeAttack(player: Player, reduced: Boolean = false) {
        if (attackCounter == 5) {
            if (!handleSpecialAttack(player))
                performStandardMelee(player, reduced)
        } else {
            performStandardMelee(player, reduced)
            attackCounter = (attackCounter + 1) % 6
        }
    }


    val DUKE_FLOOR_GFX = mapOf(
        coord(3036, 6451, 0) to 2440,
        coord(3037, 6451, 0) to 2441,
        coord(3038, 6451, 0) to 2442,
        coord(3039, 6451, 0) to 2443,
        coord(3040, 6451, 0) to 2442,
        coord(3041, 6451, 0) to 2441,
        coord(3042, 6451, 0) to 2440
    )

    private fun performStandardMelee(player: Player, reduced: Boolean) {
        setAnimation(Animation(10176))
        graphics = Graphics(2439)
        player.sendSound(7186)

        DUKE_FLOOR_GFX.forEach { (coord, graphic) ->
            arena[coord].spotanim(graphic)
        }

        val location = arena[player.location]
        if (location.x in DUKE_MELEE_MIN_XY.first..DUKE_MELEE_MAX_XY.first && location.y in DUKE_MELEE_MIN_XY.second..DUKE_MELEE_MAX_XY.second) {
            player.applyHit(Hit(random(3..5), HitType.TYPELESS))
        }

        schedule(1) {
            val location = arena[player.location]
            if (location.x in DUKE_MELEE_MIN_XY.first..DUKE_MELEE_MAX_XY.first && location.y in DUKE_MELEE_MIN_XY.second..DUKE_MELEE_MAX_XY.second) {
                player.applyHit(difficulty.getMeleeHit(this, player, reduced))
            }
        }
    }

    private fun magicAttack(player: Player) {
        if (attackCounter == 5) {
            if (!handleSpecialAttack(player))
                performStandardMagic(player)
        } else {
            performStandardMagic(player)
            attackCounter = (attackCounter + 1) % 6
        }
    }

    private fun handleSpecialAttack(player: Player): Boolean {
        if (!arena.passingGas) {
            DukeSpecialAttack.DeathGaze(this, player)
            schedule(15) {
                DukeSpecialAttack.GasFlare(this, player)
                if (hitpoints < (0.50 * maxHitpoints).toInt())
                    DukeSpecialAttack.GasFlareEcho(this, player)
            }
            attackCounter = (attackCounter + 1) % 6
            return true
        }
        return false
    }

    private fun performStandardMagic(player: Player) {
        setAnimation(Animation(10176))

        val delay = World.sendProjectile(this.middleLocation offset Pair(-1, 0), player, magicProjectile)
        player.scheduleHit(this, difficulty.getMagicHit(this, player), delay, false)
        player.sendSound(7197)
        player.sendSound(SoundEffect(7197, 1, delay * 30, 0))
    }

    private val magicProjectile = Projectile(2434, 32, 10, 20, 30, 2, 32, 10)

    private fun DT2BossDifficulty.getHitpoints(): Int = when (this) {
        DT2BossDifficulty.NORMAL -> 440
        DT2BossDifficulty.AWAKENED -> 1540
        DT2BossDifficulty.QUEST -> 350
    }

    private fun DT2BossDifficulty.getTransformation(): Int = when (this) {
        DT2BossDifficulty.NORMAL -> 12191
        DT2BossDifficulty.AWAKENED -> 12195
        DT2BossDifficulty.QUEST -> 12190
    }

    private fun DT2BossDifficulty.getMeleeHit(duke: DukeSucellusEntity, player: Player, reduced: Boolean = false): Hit {
        val minHit = when (this) {
            DT2BossDifficulty.NORMAL -> 25
            DT2BossDifficulty.AWAKENED -> 30
            DT2BossDifficulty.QUEST -> 12
        }

        val maxHit = when (this) {
            DT2BossDifficulty.NORMAL -> 50
            DT2BossDifficulty.AWAKENED -> 75
            DT2BossDifficulty.QUEST -> 20
        }
        var dmgReduction = 1.0
        if (player.prayerManager.isActive(Prayer.PROTECT_FROM_MELEE)) {
            dmgReduction = when (this) {
                DT2BossDifficulty.NORMAL -> 0.5
                DT2BossDifficulty.AWAKENED -> 0.3
                DT2BossDifficulty.QUEST -> 0.7
            }
        }

        var damage = (Utils.random(minHit, maxHit) * dmgReduction)
        if (reduced)
            damage = if (player.prayerManager.isActive(Prayer.PROTECT_FROM_MELEE)) 5.0 else 11.0
        return Hit(duke, damage.toInt(), HitType.MELEE)
    }

    private fun DT2BossDifficulty.getMagicHit(duke: DukeSucellusEntity, player: Player, cutIn: Int = 1): Hit {
        val minHit = when (this) {
            DT2BossDifficulty.NORMAL -> 28
            DT2BossDifficulty.AWAKENED -> 35
            DT2BossDifficulty.QUEST -> 12
        }

        val maxHit = when (this) {
            DT2BossDifficulty.NORMAL -> 44
            DT2BossDifficulty.AWAKENED -> 61
            DT2BossDifficulty.QUEST -> 20
        }
        var dmgReduction = 1.0
        if (player.prayerManager.isActive(Prayer.PROTECT_FROM_MAGIC)) {
            dmgReduction = when (this) {
                DT2BossDifficulty.NORMAL -> 0.5
                DT2BossDifficulty.AWAKENED -> 0.3
                DT2BossDifficulty.QUEST -> 0.7
            }
        }

        val damage = (Utils.random(minHit, maxHit) * dmgReduction) / cutIn
        val hit = Hit(duke, damage.toInt(), HitType.MAGIC)
        hit.onLand { player.sendSound(7197) }
        return hit
    }

    companion object {
        val DUKE_MELEE_MIN_XY = 3036 to 6451
        val DUKE_MELEE_MAX_XY = 3042 to 6456
    }
}



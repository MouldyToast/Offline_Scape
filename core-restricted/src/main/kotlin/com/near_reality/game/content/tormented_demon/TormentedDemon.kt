package com.near_reality.game.content.tormented_demon

import com.near_reality.game.content.seq
import com.near_reality.game.content.tormented_demon.attacks.Attack
import com.near_reality.game.content.tormented_demon.attacks.impl.FireBomb
import com.near_reality.game.content.tormented_demon.attacks.impl.MagicAttack
import com.near_reality.game.content.tormented_demon.attacks.impl.MeleeSlash
import com.near_reality.game.content.tormented_demon.attacks.impl.RangedAttack
import com.near_reality.game.util.Ticker
import com.near_reality.game.world.entity.player.tormentedDemonAccuracyBoost
import com.zenyte.game.content.boss.BossRespawnTimer
import com.zenyte.game.content.skills.prayer.Prayer
import com.zenyte.game.item.Item
import com.zenyte.game.task.WorldTasksManager.schedule
import com.zenyte.game.util.Direction
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.EntityHitBar
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.world.entity.npc.NpcOverhead
import com.zenyte.game.world.entity.npc.Spawnable
import com.zenyte.game.world.entity.npc.combat.CombatScript
import com.zenyte.game.world.entity.player.Player
import kotlin.random.Random


/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-12
 */
class TormentedDemon(
    id: Int,
    spawnLocation: Location?,
    direction: Direction,
    radius: Int
) : NPC(
    id,
    spawnLocation,
    direction,
    radius
), CombatScript,
   Spawnable {

    private val DAMAGE_THRESHOLD: Int = 150
    private var ticker: Int = 0
    private var damageCounter: Int = 0
    private var attackCounter: Int = 1
    private var activePrayer: Prayer? = null
    val accuracyBoostTimer: Ticker = Ticker(25)
    private var fireShieldActive: Boolean = true
    private var activeAttackStyle: Attack = MeleeSlash()

    fun getMagicSpellAnimation(): Int = 11388
    fun getMeleeSlashAnimation(): Int = 11392
    fun getRangeAttackAnimation(): Int = 11389
    fun getFireSkullThrowAnimation(): Int = 11387

    private fun getSpawnAnimation(): Animation = Animation(11395)

    private fun getFireShield(): Int = 2849
    private fun getFireShieldStart(): Int = 2850
    private fun getFireShieldRemove(): Int = 2852

    private fun getCombatDelay(): Int =
        getCombatDefinitions().attackSpeed

    override fun getRespawnDelay(): Int =
        BossRespawnTimer.TORMENTED_DEMON.timer.toInt()

    override fun isEntityClipped(): Boolean = true

    override fun isTolerable(): Boolean = false

    override fun validate(id: Int, name: String): Boolean =
        id == TORRMENTED_DEMON || id == TORRMENTED_DEMON_13600 || id == TORRMENTED_DEMON_13601

    override fun spawn(): NPC {
        super.spawn()
        graphics = Graphics(2847)
        this seq getSpawnAnimation().id
        damageCounter = 0
        temporaryAttributes["npc_overhead_prayers"] = true
        val randomPrayerIndex: Int = Random.nextInt(0, 2)
        when(randomPrayerIndex) {
            0 -> setPrayer(Prayer.PROTECT_FROM_MELEE)
            1 -> setPrayer(Prayer.PROTECT_FROM_MAGIC)
            else -> setPrayer(Prayer.PROTECT_FROM_MISSILES)
        }
        return this
    }

    // If we retreat, heal the demon to full health
    override fun getRetreatingAction(): Runnable =
        Runnable {
            val health: Int = getHitpoints()
            val currentHealth: Int = getCombatDefinitions().hitpoints
            val difference = health - currentHealth
            heal(difference)
        }

    override fun sendDeath() {
        graphics = Graphics(2848)
        super.sendDeath()
    }

    init {
        aggressionDistance = 16
        attackDistance = 16

        hitBar = object : EntityHitBar(this) {
            override fun getType(): Int {
                return 20
            }
        }
    }

    private fun switchPrayer(lastHit: Hit) {
        val type = lastHit.hitType
        when(type) {
            HitType.MELEE -> setPrayer(Prayer.PROTECT_FROM_MELEE)
            HitType.MAGIC -> setPrayer(Prayer.PROTECT_FROM_MAGIC)
            HitType.RANGED -> setPrayer(Prayer.PROTECT_FROM_MISSILES)
            else -> {}
        }
    }

    private fun setPrayer(prayer: Prayer) {
        activePrayer = prayer
        when(prayer) {
            Prayer.PROTECT_FROM_MELEE -> { overhead = NpcOverhead.MELEE }
            Prayer.PROTECT_FROM_MAGIC -> { overhead = NpcOverhead.MAGE }
            Prayer.PROTECT_FROM_MISSILES -> { overhead = NpcOverhead.RANGE }
            else -> {}
        }
        resetAttackedByDelay()
    }

    private fun toggleFireShield() {
        fireShieldActive = !fireShieldActive
    }

    private fun activateFireShield() {
        graphics = Graphics(getFireShieldStart())
        schedule(2) { toggleFireShield() }
    }

    private fun switchAttack(target: Entity) {
        isForceFollowClose = false
        // Random chance to Melee
        if (Random.nextDouble() < 0.33)
            isForceFollowClose = true

        val newStyle =
            if (isForceFollowClose) MeleeSlash()
            else if (Random.nextBoolean()) MagicAttack()
            else RangedAttack()
        if (newStyle == activeAttackStyle)
            switchAttack(target)
        else
            activeAttackStyle = newStyle
    }

    override fun attack(target: Entity): Int {
        val demon = this
        if (attackCounter == 1) {
            toggleFireShield()
            graphics = Graphics(getFireShieldRemove())
        }
        if (attackCounter++ % 10 == 0) {
            FireBomb().invoke(demon, target)
            toggleFireShield()
            graphics = Graphics(getFireShieldRemove())
            switchAttack(target)
            return getCombatDelay()
        }
        if (getAttack() is MeleeSlash) {
            if (!this.middleLocation.withinDistance(target.location, 2)) {
                switchAttack(target)
                return getCombatDelay()
            }
        }
        getAttack().invoke(demon, target)
        return getCombatDelay()
    }

    private fun getAttack(): Attack = activeAttackStyle

    override fun processNPC() {
        super.processNPC()
        accuracyBoostTimer.tick()

        if (accuracyBoostTimer.finished) {
            val target = attackedBy
            if (target is Player)
                target.tormentedDemonAccuracyBoost = true
        }
    }

    override fun handleIngoingHit(hit: Hit?) {
        hit ?: return
        graphics = Graphics(getFireShield())
        val weapon = hit.weapon
        val type = hit.hitType
        if (weapon != null && "Dwarf Multicannon" == weapon.toString())
            hit.damage = 0
        when(type) {
            HitType.MELEE -> if (activePrayer == Prayer.PROTECT_FROM_MELEE) hit.damage = 0
            HitType.MAGIC -> if (activePrayer == Prayer.PROTECT_FROM_MAGIC) hit.damage = 0
            HitType.RANGED -> if (activePrayer == Prayer.PROTECT_FROM_MISSILES) hit.damage = 0
            else -> {}
        }
        val attacker = hit.source
        if (attacker is Player) {
            if (getAttack() is MeleeSlash) {
                if (!this.middleLocation.withinDistance(attacker.location, 2))
                    switchAttack(attacker)
            }
            if (hit.damage > 0)
                damageCounter += hit.damage
            // if the counter has passed the threshold, switch the attack, and the protection prayer
            if (damageCounter >= DAMAGE_THRESHOLD) {
                switchPrayer(hit)
                damageCounter = 0
                getCombat().combatDelay = getCombatDelay()
            }
            if (fireShieldActive)
                if (weapon is Item && !isWeakToWeapon(weapon))
                    hit.damage = (hit.damage * 0.8).toInt()
            else if (weapon is Item && isWeakToWeapon(weapon) && isApplicableWeapon(hit))
                hit.damage = getBoostedDamage(hit.damage, attacker.attackingDelay)
            // if the shield is down, and we're hit, we want to reactivate the shield
            if (!fireShieldActive)
                activateFireShield()
        }
        super.handleIngoingHit(hit)
    }

    private fun isWeakToWeapon(weapon: Item): Boolean =
        weapon.isDemonbaneWeapon || weapon.isAbyssalWeapon

    private fun getBoostedDamage(baseDamage: Int, attackSpeed: Long): Int {
        val damageBoost = (attackSpeed * attackSpeed) - 16
        return (baseDamage + damageBoost).toInt()
    }

    private fun isApplicableWeapon(hit: Hit?): Boolean {
        hit ?: return false
        val spell = hit.spell
        if (spell != null) return true
        val weapon = hit.weapon ?: return false
        if (weapon is Item) {
            if (weapon.name.contains("crossbow", true)) return true
            if (weapon.name.contains("ballista", true)) return true
            if (weapon.definitions.bonuses[2] > 0) return true
        }
        return false
    }
}
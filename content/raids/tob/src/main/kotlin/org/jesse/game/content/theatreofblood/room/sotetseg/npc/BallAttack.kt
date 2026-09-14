package org.jesse.game.content.theatreofblood.room.sotetseg.npc

import org.jesse.game.content.chambersofxeric.greatolm.scripts.Lightning
import org.jesse.game.content.skills.prayer.Prayer
import org.jesse.game.util.Utils
import org.jesse.game.world.Projectile
import org.jesse.game.world.entity.SoundEffect
import org.jesse.game.world.entity.masks.Graphics
import org.jesse.game.world.entity.player.Player
import org.jesse.utils.TimeUnit

/**
 * @author Tommeh
 * @author Jire
 */
internal enum class BallAttack(
    private val projectile: Projectile,
    private val requiredPrayer: Prayer
) {

    MAGIC(Projectile(1606, 80, 120, 80, 0, 4 * 30, 0, 0), Prayer.PROTECT_FROM_MAGIC) {
        override fun Sotetseg.performHit(target: Player) {
            delayHit(0, target, magic(target, if (room.raid.hardMode) 75 else 50))
            target.graphics = magicGraphics
            target.sendSound(magicSoundEffect)
        }
    },
    RANGE(Projectile(1607, 80, 120, 80, 0, 4 * 30, 0, 0), Prayer.PROTECT_FROM_MISSILES) {
        override fun Sotetseg.performHit(target: Player) {
            delayHit(0, target, ranged(target, if (room.raid.hardMode) 75 else 50))
            target.sendSound(rangeSoundEffect)
        }
    };

    abstract fun Sotetseg.performHit(target: Player)

    fun createProjectile(ricochet: Boolean) = if (ricochet) Projectile(projectile, 5 * 30) else projectile

    fun applyHit(target: Player, sotetseg: Sotetseg) {
        if (!sotetseg.shouldAttack() || !sotetseg.room.isValidTarget(target)) return

        sotetseg.performHit(target)

        if (!target.prayerManager.isActive(requiredPrayer)) {
            Lightning.deactivateOverheadProtectionPrayers(target, target.prayerManager, true)
        }
    }

    companion object {

        private val values = values()

        fun randomBall() = values[Utils.random(values.lastIndex)]

        private val magicGraphics = Graphics(131, 0, 124)
        private val magicSoundEffect = SoundEffect(156, 6, 0)

        private val rangeSoundEffect = SoundEffect(4015, 6, 0)

    }

}
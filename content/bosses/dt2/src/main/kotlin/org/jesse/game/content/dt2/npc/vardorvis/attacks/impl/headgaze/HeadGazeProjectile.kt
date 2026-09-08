package org.jesse.game.content.dt2.npc.vardorvis.attacks.impl.headgaze

import org.jesse.api.model.Skill
import org.jesse.game.content.dt2.area.VardorvisInstance
import org.jesse.game.content.dt2.npc.DT2BossDifficulty
import org.jesse.game.content.seq
import org.jesse.game.content.spotanim
import org.jesse.game.content.skills.prayer.Prayer
import org.jesse.game.task.TickTask
import org.jesse.game.task.WorldTasksManager.schedule
import org.jesse.game.util.Utils
import org.jesse.game.world.Projectile
import org.jesse.game.world.World
import org.jesse.game.world.entity.player.Player

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-08
 */
class HeadGazeProjectile(
    var instance: VardorvisInstance,
    private var head: VardorvisHead,
    private val projectileID: Int = 2521,
    private val headGaze: Int = 2520
) {

    fun headGazeProjectile() {
        val isAwakened = instance.difficulty == DT2BossDifficulty.AWAKENED
        val magicRoll = isAwakened && Utils.random(1) == 0
        val player: Player = instance.player

        val projectile = Projectile(projectileID, 64, 32, 64, 0)

        head seq 10348
        head spotanim headGaze

        val timer = World.sendProjectile(head.position, player.position, projectile)

        schedule(object : TickTask() {

            override fun run() {
                if (ticks == timer) {
                    if (magicRoll && !player.prayerManager.isActive(Prayer.PROTECT_FROM_MAGIC))
                        player.prayerManager.deactivateActivePrayers()
                    if (!magicRoll && !player.prayerManager.isActive(Prayer.PROTECT_FROM_MISSILES))
                        player.prayerManager.deactivateActivePrayers()
                    stop()
                }
                ticks++
            }

            override fun stop() {
                super.stop()
                player.drainSkill(Skill.PRAYER.ord, 10)

            }
        }, 0, 0)
    }
}

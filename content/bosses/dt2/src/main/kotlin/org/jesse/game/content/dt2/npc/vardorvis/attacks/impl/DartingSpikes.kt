package org.jesse.game.content.dt2.npc.vardorvis.attacks.impl

import org.jesse.game.content.dt2.npc.vardorvis.attacks.Attack

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-08
 */
object DartingSpikes : Attack {
    override fun getRequiredHp(): Double = 75.00
    override fun getFirstRate() = 15
    override fun getSecondRate() = 20
    override fun getEnrageRate() = 10
    override fun getEnabled(): Boolean = true
    override fun getAutoHeal(): Boolean = true
    override fun requiredCooldownAttacks(enrage: Boolean): Int = 8
}
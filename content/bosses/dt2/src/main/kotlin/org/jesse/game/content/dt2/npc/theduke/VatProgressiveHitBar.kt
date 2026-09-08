package org.jesse.game.content.dt2.npc.theduke

import org.jesse.game.content.dt2.npc.whisperer.whispererTimerCurrent
import org.jesse.game.content.dt2.npc.whisperer.whispererTimerMax
import org.jesse.game.world.entity.HitBar
import org.jesse.game.world.entity.npc.NPC
import mgi.types.config.HitbarDefinitions
import kotlin.math.min

class VatProgressiveHitBar(val npc: NPC) : HitBar() {
    override fun getType(): Int {
        return 31
    }

    override fun getPercentage(): Int {
        val multiplier = getMultiplier()
        val current = npc.whispererTimerCurrent
        val maximum = npc.whispererTimerMax

        val mod: Float = maximum.toFloat() / multiplier
        return (multiplier - min(((current + mod) / mod).toInt().toDouble(), multiplier.toDouble())).toInt()
    }

    private fun getMultiplier(): Int {
        val type = type
        return HitbarDefinitions.get(type).size
    }
}
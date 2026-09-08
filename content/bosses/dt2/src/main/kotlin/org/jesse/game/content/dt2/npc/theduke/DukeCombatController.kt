package org.jesse.game.content.dt2.npc.theduke

import org.jesse.game.util.CollisionUtil
import org.jesse.game.world.Position
import org.jesse.game.world.entity.npc.CombatScriptsHandler
import org.jesse.game.world.entity.npc.NPCCombat

class DukeCombatController(entity: DukeSucellusEntity) : NPCCombat(entity) {
    override fun outOfRange(
        targetPosition: Position?,
        maximumDistance: Int,
        targetSize: Int,
        checkDiagonal: Boolean,
    ): Boolean {
        return false
    }

    override fun combatAttack(): Int {
        if (target == null) {
            return 0
        }
        addAttackedByDelay(target)
        return CombatScriptsHandler.specialAttack(npc, target)
    }


}
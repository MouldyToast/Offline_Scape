package org.jesse.game.content.boss.abyssalsire.spawns

import org.jesse.game.content.boss.abyssalsire.AbyssalSire
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.util.Direction
import org.jesse.game.world.Projectile
import org.jesse.game.world.World
import org.jesse.game.world.entity.Entity
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.npc.NPC
import org.jesse.game.npc.ids.*
import org.jesse.game.world.entity.npc.combat.CombatScript

/**
 * @author Jire
 * @author Kris
 */
internal class AbyssalSireSpawn(
    location: Location,
    private val spawns: AbyssalSireSpawns,
    private val sire: AbyssalSire
) : NPC(SPAWN, location, Direction.SOUTH, 5), CombatScript {

    override fun spawn(): NPC {
        spawned = true
        attackDistance = 4
        setTarget(sire.target!!.get())
        WorldTasksManager.schedule({
            if (isFinished || isDead) return@schedule

            sire.scionMatured = true
            setTransformation(SCION)
        }, 19)

        return super.spawn()
    }

    override fun finish() {
        super.finish()

        spawns.removeSpawn(this)
    }

    override fun attack(target: Entity): Int {
        animate()
        if (!combat.outOfRange(target, 0, target!!.size, true)) {
            delayHit(0, target, melee(target, if (id == SCION) 15 else 6))
        } else {
            delayHit(World.sendProjectile(this, target, rangedProj), target, ranged(target, if (id == SCION) 15 else 6))
        }

        return getCombatDefinitions().attackSpeed
    }

    companion object {
        private val rangedProj = Projectile(628, 168, 120, 40, 15, 10, 64, 5)
    }

}
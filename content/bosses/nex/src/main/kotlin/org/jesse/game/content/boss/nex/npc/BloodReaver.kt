package org.jesse.game.content.boss.nex.npc

import org.jesse.game.content.godwars.GodType
import org.jesse.game.content.godwars.npcs.AbstractKillcountNPC
import org.jesse.game.util.Direction
import org.jesse.game.world.Projectile
import org.jesse.game.world.World
import org.jesse.game.world.entity.Entity
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.Graphics
import org.jesse.game.world.entity.npc.combat.CombatScript

open class BloodReaver(id: Int, tile: Location?, facing: Direction, radius: Int) :
    AbstractKillcountNPC(id, tile, facing, radius), CombatScript {

    init {
        isCrawling = true
        setInGodwars(true)
    }

    override fun attack(target: Entity): Int {
        setAnimation(Animation(9194))
        val projectile = Projectile(372, 0, 0, 51, 23, -5, 64, 10)
        delayHit(this, World.sendProjectile(this, target, projectile), target, magic(target, 20).onLand {
            target.graphics = Graphics(375, -1, 0)
            val healAmount = (it.damage * 0.25).toInt()
            if (healAmount > 0)
                heal(healAmount)
        })
        return getCombatDefinitions().attackSpeed
    }

    override fun type(): GodType = GodType.ANCIENT

}

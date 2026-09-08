package org.jesse.game.content.theatreofblood.room.verzikvitur.second

import org.jesse.game.content.theatreofblood.room.verzikvitur.VerzikVitur
import org.jesse.game.content.theatreofblood.room.verzikvitur.spiders.NylocasMatomenos
import org.jesse.game.world.WorldThread
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.Hit
import org.jesse.game.world.entity.masks.HitType
import org.jesse.game.world.entity.npc.NPC

/**
 * @author Jire
 */

internal fun VerzikVitur.spawnMatomenos() {
    lastShield = WorldThread.getCurrentCycle()
    animation = spawnMatomenosAnimation

    if (crabs.isNotEmpty()) {
        for (crab in crabs) {
            if (!crab.isDead && !crab.isFinished) {
                scheduleHit(crab, Hit(crab.hitpoints, HitType.HEALED), 0)
            }
            crab.finish()
            //TODO animation, projectile etc
        }
        crabs.clear()
    }

    crabs.add(NylocasMatomenos(room, room.getBaseLocation(38, 24)).apply(NPC::spawn))
    crabs.add(NylocasMatomenos(room, room.getBaseLocation(26, 24)).apply(NPC::spawn))
}

private val spawnMatomenosAnimation = Animation(8117)

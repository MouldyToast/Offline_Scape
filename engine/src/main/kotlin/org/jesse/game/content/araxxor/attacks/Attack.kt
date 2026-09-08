package org.jesse.game.content.araxxor.attacks

import org.jesse.game.world.entity.Entity

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-20
 */
interface Attack {
    operator fun invoke(araxxor: org.jesse.game.content.araxxor.Araxxor, target: Entity?)
}
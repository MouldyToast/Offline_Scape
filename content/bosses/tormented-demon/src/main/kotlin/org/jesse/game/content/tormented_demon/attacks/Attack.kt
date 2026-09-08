package org.jesse.game.content.tormented_demon.attacks

import org.jesse.game.content.tormented_demon.TormentedDemon
import org.jesse.game.world.entity.Entity

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-16
 */
interface Attack {
    operator fun invoke(demon: TormentedDemon, target: Entity?)
}
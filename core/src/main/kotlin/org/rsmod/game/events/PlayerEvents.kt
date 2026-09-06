package org.rsmod.game.events

import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.player.Player
import org.rsmod.events.UnboundEvent

class PlayerLoginEvent(val player: Player) : UnboundEvent

class PlayerLogoutEvent(val player: Player) : UnboundEvent

/**
 * Published once per tick from inside Player.processEntity, at EXACTLY the
 * position the farming/hunter/prayer per-tick drivers used to occupy (after
 * the charge-degradation block, before the acid-pool check), inside the same
 * try/catch. Subscribers therefore observe — and on throw, interact with —
 * the same intra-tick state the direct calls did. Transitional zenyte
 * bridge: OpenRune's end-state is per-system soft timers, not a broadcast
 * process event.
 */
class PlayerProcessEvent(val player: Player) : UnboundEvent

/**
 * Published immediately BEFORE removeHitpoints so subscribers observe the
 * player's pre-hit hitpoints (required by the ToA damage tracker migrated
 * in Phase C).
 */
class PlayerDamageReceivedEvent(
    val player: Player,
    val source: Entity?,
    val damage: Int,
    val hitType: HitType,
) : UnboundEvent

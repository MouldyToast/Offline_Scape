package org.rsmod.game.events

import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.player.Player
import org.rsmod.events.UnboundEvent

class PlayerLoginEvent(val player: Player) : UnboundEvent

class PlayerLogoutEvent(val player: Player) : UnboundEvent

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

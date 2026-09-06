package org.rsmod.game.events

import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.masks.Hit
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

/**
 * Published from Player.sendDeath (and the parallel custom-death path in
 * PlayerDeathHandler.sendDeath) at EXACTLY the position the retribution
 * check used to occupy — after the admin-hp-event early return, before the
 * bounty block. source is the killer (Player from the vanilla path; may be
 * any Entity or null from the custom path).
 */
class PlayerDeathStartEvent(val player: Player, val source: Entity?) : UnboundEvent

/**
 * Published from Player.removeHitpoints as the FIRST statement inside the
 * !isDead() branch — i.e. only for surviving players, with post-hit
 * hitpoints applied, at EXACTLY the position the redemption check used to
 * occupy (before the faith-necklace / phoenix / deadly-prayers /
 * ring-of-life checks). cappedDamage is the damage after capping to
 * pre-hit hitpoints.
 */
class PlayerPostDamageEvent(val player: Player, val hit: Hit, val cappedDamage: Int) : UnboundEvent

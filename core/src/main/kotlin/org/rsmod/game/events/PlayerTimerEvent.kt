package org.rsmod.game.events

import com.zenyte.game.world.entity.player.Player
import org.rsmod.events.KeyedEvent

/**
 * The Soft half of OpenRune's PlayerTimerEvent (api/player/src/main/
 * kotlin/org/rsmod/api/player/events/PlayerTimerEvent.kt). DIVERGENCE:
 * lives in org.rsmod.game.events (this port's event home) rather than
 * org.rsmod.api.player.events, and the Normal half (SuspendEvent +
 * ProtectedAccess) is not ported. Body otherwise upstream.
 */
public class PlayerTimerEvent {
    public class Soft(public val player: Player, timerType: Int) : KeyedEvent {
        override val id: Long = timerType.toLong()
    }
}

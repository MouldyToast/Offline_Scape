@file:JvmName("SpellbookSwapHooks")

package com.zenyte.game.content.skills.magic.spells.lunar

import com.google.common.eventbus.Subscribe
import com.zenyte.plugins.events.ServerLaunchEvent
import org.rsmod.game.events.PlayerLogoutEvent

/**
 * T2-a: lunar spellbook-swap revert moved off the Player logout block.
 * Runs later within the same logout method than the inline call did;
 * persisted state identical (save serializes after logout completes),
 * and the body only touches combatDefinitions (verified).
 */
@Subscribe
fun onServerLaunch(event: ServerLaunchEvent) {
    event.worldThread.eventBus.subscribeUnbound(PlayerLogoutEvent::class.java) {
        SpellbookSwap.checkSpellbook(player)
    }
}

package com.near_reality.game.model.item.submenu.impl.max_cape

import com.near_reality.game.model.item.submenu.ISubMenuAction
import com.zenyte.game.content.skills.magic.Spellbook
import com.zenyte.game.util.Colour
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.logger.NearRealityLogger

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-04-10
 */
class MaxCapeSpellbookAction(
    private val standard: Int = 0,
    private val ancient: Int = 1,
    private val lunar: Int = 2,
    private val arceuus: Int = 3,
    private val check: Int = 4
): ISubMenuAction {

    private val logger = NearRealityLogger.getLogger(MaxCapeSpellbookAction::class.java)

    override fun onAction(player: Player, selectedItemIndex: Int) {
        when(selectedItemIndex) {
            standard, ancient, lunar, arceuus -> player.swapSpellbookToIndex(selectedItemIndex)
            check -> player.checkSpellbookSpawnsRemaining()
            else -> logger.warn("Unused action index in MaxCapeSpellbookAction: $selectedItemIndex")
        }
    }

    private fun Player.swapSpellbookToIndex(bookIndex: Int) {
        if (variables.spellbookSwaps >= 5) {
            dialogue { plain("You may only switch spellbooks five times per day. Try again tomorrow.") }
            return
        }
        getCombatDefinitions().setSpellbook(Spellbook.VALUES[bookIndex], true)
        val count: Int = variables.spellbookSwaps + 1
        variables.spellbookSwaps = count
        sendMessage(Colour.RED.wrap("You have changed your spellbook $count/5 times today."))
    }

    private fun Player.checkSpellbookSpawnsRemaining() {
        dialogue { plain("You have changed your spellbook ${variables.spellbookSwaps}/5 times today.") }
    }

}
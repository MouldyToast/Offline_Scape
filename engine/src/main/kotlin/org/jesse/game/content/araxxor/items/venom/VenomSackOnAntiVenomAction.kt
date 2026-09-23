package org.jesse.game.content.araxxor.items.venom

import org.jesse.game.item.Item
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.ItemOnItemAction
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.SkillConstants
import org.jesse.plugins.dialogue.SkillDialogue

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-11-01
 */
class VenomSackOnAntiVenomAction : ItemOnItemAction {
    override fun handleItemOnItemAction(player: Player?, from: Item?, to: Item?, fromSlot: Int, toSlot: Int) {
        if (player == null) return
        if (player.skills.getLevel(SkillConstants.HERBLORE) < 94) {
            player.sendMessage("You need a herblore level of 94 to brew the Extended Anti-Venom.")
            return
        }
        val venomSack = if (from?.id == ARAXYTE_VENOM_SAC) from else to
        val potion = if (from?.id != ARAXYTE_VENOM_SAC) from else to

        val dose = potion?.name?.replace(Regex("[^0-9]"), "")?.toIntOrNull() ?: 0
        if (venomSack == null || potion == null) return
        if (venomSack.amount >= dose)
            player.dialogueManager.start(
                ExtendedAntiVenomCreationDialogue(
                    player,
                    potion.id
                )
            )
        else
            player.sendMessage("You need at least $dose venom sack${if (dose == 1) "" else "s"} to do this")
    }

    override fun getMatchingPairs(): Array<ItemOnItemAction.ItemPair> {
        return arrayOf(
            ItemOnItemAction.ItemPair.of(ANTIVENOM4_12913, ARAXYTE_VENOM_SAC),
            ItemOnItemAction.ItemPair.of(ANTIVENOM3_12915, ARAXYTE_VENOM_SAC),
            ItemOnItemAction.ItemPair.of(ANTIVENOM2_12917, ARAXYTE_VENOM_SAC),
            ItemOnItemAction.ItemPair.of(ANTIVENOM1_12919, ARAXYTE_VENOM_SAC)
        )
    }

    override fun getItems(): IntArray? = null

    class ExtendedAntiVenomCreationDialogue(
        player: Player,
        val potion: Int,
        vararg val potions: Item = arrayOf(Item(CombineAntiVenomPotion.pots[potion]))
    ) : SkillDialogue(
        player,
        DIALOGUE_MESSAGE,
        potions[0]
    ) {

        companion object {
            private const val DIALOGUE_MESSAGE = "How many would you like to make?"
        }

        override fun run(slotId: Int, amount: Int) {
            player.actionManager.setAction(CombineAntiVenomPotion(amount, potion))
        }
    }
}
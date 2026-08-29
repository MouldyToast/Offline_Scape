package com.near_reality.game.content.remnantpets

import com.near_reality.game.item.CustomObjectId
import com.zenyte.GameToggles
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.ItemOnObjectAction
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.Expression
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.WorldObject

class EternalPrimalFire : ObjectAction, ItemOnObjectAction {
    override fun handleObjectAction(
        player: Player,
        `object`: WorldObject,
        name: String,
        optionId: Int,
        option: String
    ) {
        player.dialogue { plain("Please use your remnant pet on the altar to attempt fission.") }
    }

    override fun handleItemOnObjectAction(player: Player, item: Item, slot: Int, `object`: WorldObject?) {
        if(!player.hasSpokenToDrifter) {
            player.dialogue{ player("Perhaps I should check with the guy<br>down here first before touching this", Expression.CALM) }
            return
        }
        if(!GameToggles.ORIGINS_PRIMAL_FIRE_ENABLED) {
            player.sendMessage("This has been disabled temporarily for game integrity.")
            player.sendMessage("Please check discord for updates.")
            return
        }
        if(item.id != ItemId.PET_DARK_ROC && item.id != ItemId.PET_DARK_KRATOS)
            PrimalFission.attemptFission(item, player)
        else PrimalFission.askForConfirmationForFK(player, item)
    }

    override fun getItems() = arrayOf(*RemnantPetManager.standardPets.map { it.itemId }.toTypedArray(), ItemId.PET_DARK_ROC, ItemId.PET_DARK_KRATOS)

    override fun getObjects() = arrayOf(CustomObjectId.ETERNAL_PRIMAL_FIRE)
}
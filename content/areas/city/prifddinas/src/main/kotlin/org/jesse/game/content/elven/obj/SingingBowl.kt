package org.jesse.game.content.elven.obj

import org.jesse.game.content.crystal.CrystalRecipe
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.obj.ids.*
import org.jesse.game.world.`object`.WorldObject

/**
 * Handles the Singing bowl object interactions,
 * players can use the bowl to craft new crystal items by following a [recipe][CrystalRecipe].
 *
 * https://oldschool.runescape.wiki/w/Singing_bowl
 *
 * @author Stan van der Bend
 */
@Suppress("unused")
class SingingBowl : ObjectAction {

    override fun handleObjectAction(
        player: Player,
        `object`: WorldObject,
        name: String,
        optionId: Int,
        option: String,
    ) {
        if (option == "Sing-crystal")
            player.dialogueManager.start(SingCrystalDialogue(player))
    }

    override fun getObjects() = arrayOf(SINGING_BOWL_36552)
}


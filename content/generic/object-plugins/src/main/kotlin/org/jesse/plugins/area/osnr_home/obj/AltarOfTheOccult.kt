package org.jesse.plugins.area.osnr_home.obj


import org.jesse.game.content.skills.magic.Spellbook
import org.jesse.game.item.Item
import org.jesse.game.item.ids.*
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.dialogue.start
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.obj.ids.*
import org.jesse.game.world.`object`.WorldObject

class AltarOfTheOccult : ObjectAction {

    override fun handleObjectAction(
        player: Player,
        `object`: WorldObject,
        name: String,
        optionId: Int,
        option: String
    ) {
        val spellbook = when (option) {
            "Venerate" -> Spellbook.NORMAL
            "Standard" -> Spellbook.NORMAL
            "Regular" -> Spellbook.NORMAL
            "Ancient" -> Spellbook.ANCIENT
            "Lunar" -> Spellbook.LUNAR
            "Arceuus" -> Spellbook.ARCEUUS
            else -> return
        }
        if (player.combatDefinitions.spellbook == spellbook) {
            player.dialogueManager.start {
                plain("You are already using this spellbook.<br><br>Choose a different one.")
            }
            return
        }
        player.animation = Animation(PRAY_ANIM)
        player.combatDefinitions.setSpellbook(spellbook, true)
        player.dialogueManager.start {
            item(Item(ANCIENT_STAFF), "Your spellbook has been changed.")
        }
    }

    override fun getObjects() = arrayOf(ALTAR_OF_THE_OCCULT)

    private companion object {
        const val PRAY_ANIM = 645
    }

}

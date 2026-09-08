package com.zenyte.game.content.theatreofblood.plugin.`object`

import com.zenyte.game.item.ids.*
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.obj.ids.*
import com.zenyte.game.world.`object`.WorldObject

/**
 * Grand bookshelves can be found inside Verzik Vitur's treasure vault, after defeating her in the Theatre of Blood.
 * They can be searched for a collection of books:
 *
 *  1. Serafina's Diary
 *  2. The Butcher
 *  3. Arachnids of Vampyrium
 *  4. The Shadow Realm
 *  5. The Wild Hunt
 *  6. Verzik Vitur - Patient Record
 *
 * @author Stan van der Bend
 */
@Suppress("unused")
class GrandBookshelvesObject : ObjectAction {

    override fun handleObjectAction(
        player: Player,
        obj: WorldObject,
        name: String,
        optionId: Int,
        option: String
    ) {
        if (option == "Search") {
            val bookId = bookItemIds.random()
            player.sendDeveloperMessage("TODO: figure out how this works on OSRS")
        }
    }

    override fun getObjects() = arrayOf(
        GRAND_BOOKSHELF,
        GRAND_BOOKSHELF_33001,
        GRAND_BOOKSHELF_33002,
        GRAND_BOOKSHELF_33003
    )

    private companion object {
        val bookItemIds = arrayOf(
            SERAFINAS_DIARY,
            THE_BUTCHER,
            ARACHNIDS_OF_VAMPYRIUM,
            THE_SHADOW_REALM,
            THE_WILD_HUNT,
            VERZIK_VITUR__PATIENT_RECORD
        )
    }
}

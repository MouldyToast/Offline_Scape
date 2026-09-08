package org.jesse.content.group_ironman.npc

import org.jesse.game.util.Direction
import org.jesse.game.util.Utils
import org.jesse.game.world.entity.ForceTalk
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.npc.NPC
import org.jesse.game.npc.ids.*
import org.jesse.game.world.entity.npc.Spawnable

@Suppress("unused")
class D3ald1iFi5her(id: Int, tile: Location?, facing: Direction?, radius: Int)
    : NPC(id, tile, facing, radius), Spawnable {

    private var chatDelay: Long = 0

    override fun processNPC() {
        super.processNPC()
        if (chatDelay < Utils.currentTimeMillis() && Utils.random(20) == 0) {
            chatDelay = Utils.currentTimeMillis() + 5000
            forceTalk = MESSAGES.random()
        }
    }

    override fun validate(id: Int, name: String?): Boolean =
        id == D3AD1I_F15HER

    private companion object {
        val MESSAGES = arrayOf(
            ForceTalk("Cor blimey, This is a big one!"),
            ForceTalk("Fishing levels?"),
            ForceTalk("Oh no... It got away!"),
            ForceTalk("Is that a shark?"),
            ForceTalk("Here fishy fishies!"),
        )
    }
}

package com.near_reality.game.content.dt2.npc.vardorvis.ui

import com.near_reality.game.content.dt2.area.VardorvisInstance
import com.near_reality.game.content.dt2.npc.attrOrElse
import com.near_reality.game.content.dt2.npc.instanceArea
import com.zenyte.game.GameInterface
import com.zenyte.game.model.ui.Interface
import com.zenyte.game.model.ui.InterfacePosition
import com.zenyte.game.util.Utils
import com.zenyte.game.world.entity.attribute
import com.zenyte.game.world.entity.player.Player

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-01-18
 */
class EntangledPuzzle : Interface() {

    private var Player.totalSplats by attrOrElse("totalSplats", 6)
    private var Player.poppedSplats by attrOrElse("poppedSplats", 0)

    override fun attach() {
        put(11, "blood_splat_1")
        put(10, "blood_splat_2")
        put(9, "blood_splat_3")
        put(8, "blood_splat_4")
        put(7, "blood_splat_5")
        put(6, "blood_splat_6")
    }

    override fun build() {
        bind("blood_splat_1") { player: Player?, _: Int, _: Int, _: Int -> clickSplat(player!!) }
        bind("blood_splat_2") { player: Player?, _: Int, _: Int, _: Int -> clickSplat(player!!) }
        bind("blood_splat_3") { player: Player?, _: Int, _: Int, _: Int -> clickSplat(player!!) }
        bind("blood_splat_4") { player: Player?, _: Int, _: Int, _: Int -> clickSplat(player!!) }
        bind("blood_splat_5") { player: Player?, _: Int, _: Int, _: Int -> clickSplat(player!!) }
        bind("blood_splat_6") { player: Player?, _: Int, _: Int, _: Int -> clickSplat(player!!) }
    }

    private fun clickSplat(player: Player) {
        player.poppedSplats++
        if (player.poppedSplats == player.totalSplats) {
            close(player)

            val instance = player.instanceArea as? VardorvisInstance ?: return
            instance.resetEntanglement(true)
        }
    }

    override fun open(player: Player?) {
        player ?: return
        super.open(player)

        val dispatcher = player.packetDispatcher

        player.interfaceHandler.closeInterface(InterfacePosition.CENTRAL)
        player.interfaceHandler.sendInterface(this)

        player.poppedSplats = 0
        player.totalSplats = Utils.random(1, 6)

        dispatcher.sendClientScript(1816, player.totalSplats)
    }

    public override fun close(player: Player?) {
        super.close(player)
    }

    override fun getInterface(): GameInterface =
        GameInterface.ENTANGLED_PUZZLE
}
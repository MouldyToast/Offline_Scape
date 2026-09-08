package com.near_reality.game.content.dt2.area

import com.near_reality.game.content.dt2.npc.DT2BossDifficulty
import com.near_reality.game.content.dt2.npc.deathsToAwakenedVardorvis
import com.near_reality.game.content.dt2.npc.deathsToVardorvis
import com.near_reality.game.content.dt2.npc.whisperer.WhispererConstants
import com.near_reality.game.content.dt2.npc.whisperer.WhispererNPC
import com.near_reality.game.content.dt2.npc.whisperer.addTentacles
import com.near_reality.game.content.dt2.npc.whisperer.sanity
import com.near_reality.game.content.faceDir
import com.zenyte.game.GameInterface
import com.zenyte.game.task.WorldTasksManager.schedule
import com.zenyte.game.util.Direction
import com.zenyte.game.world.Position
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.npc.ids.*
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.cutscene.FadeScreen
import com.zenyte.game.world.region.PrebuiltDynamicArea
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin
import com.zenyte.game.world.region.area.plugins.DeathPlugin
import com.zenyte.game.world.region.area.plugins.GravestoneLocationPlugin
import com.zenyte.plugins.dialogue.PlainChat

data class WhispererInstance(
    val player: Player,
    val difficulty: DT2BossDifficulty = DT2BossDifficulty.NORMAL
) : PrebuiltDynamicArea(sw = 10595, ne = 9571, se = -1, nw = -1),
    CannonRestrictionPlugin,
    GravestoneLocationPlugin,
    DeathPlugin {
    override fun name(): String = "Whisperer Instance"

    override fun isMultiwayArea(position: Position?): Boolean = true

    override fun constructed() {
        spawnBarrier()
        transferPlayer()
    }

    private fun transferPlayer() {
        player.lock()
        val screen = FadeScreen(player) {
            player.setLocation(entranceTile)
            player.faceDirection(Direction.WEST)
            player.dialogueManager.start(PlainChat(player, "You approach the figure in the water."))
        }
        player.dialogueManager.start(PlainChat(player, "You approach the figure in the water.", false))
        screen.fade()
        schedule(2) {
            screen.unfade()
            player.unlock()
            player.faceDir(Direction.EAST)
            player.sanity = 100

            val oddFigure = WhispererNPC(
                ODD_FIGURE,
                getLocation(2655, 6368, 0),
                Direction.NORTH,
                this
            ).spawn()
            oddFigure.animation = WhispererConstants.WHISPERER_ODD_FIGURE_SPAWN
        }
    }

    private val entranceTile: Location get() = getLocation(2656, 6382, 0)

    override fun enter(player: Player) {
        player.mapInstance = this@WhispererInstance
        player.teleport(entranceTile)

        GameInterface.WHISPERER_SANITY.open(player)
    }

    override fun leave(player: Player, logout: Boolean) {
        player.hpHud.close()
        player.interfaceHandler.closeInterface(GameInterface.WHISPERER_SANITY)

        if (logout) {
            player.teleport(onLoginLocation())
        }
    }

    override fun onLoginLocation(): Location {
        return Location(2656, 6400, 0)
    }

    override fun getGravestoneLocation(): Location {
        return onLoginLocation()
    }

    private fun spawnBarrier() {
        addTentacles(fixed = true, offset = false)
        addTentacles(fixed = true, offset = true)
    }

    override fun getPlayers(): MutableSet<Player> {
        val players = super.getPlayers()
        if (inside(player.position)) {
            players.add(player)
        }
        return players
    }

    override fun sendDeath(player: Player?, source: Entity?): Boolean {
        player ?: return true
        DT2Module.getWhispererStatistics(difficulty == DT2BossDifficulty.AWAKENED).globalDeathCount++
        if (difficulty == DT2BossDifficulty.AWAKENED)
            player.deathsToAwakenedVardorvis++
        else
            player.deathsToVardorvis++
        return false
    }

    override fun isSafe(): Boolean = false

    override fun getDeathInformation(): String? = null

    override fun getRespawnLocation(): Location? = null
}
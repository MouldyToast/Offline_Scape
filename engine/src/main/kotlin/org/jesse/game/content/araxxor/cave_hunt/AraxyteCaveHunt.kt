package org.jesse.game.content.araxxor.cave_hunt

import org.jesse.game.content.*
import org.jesse.game.item.ids.*
import org.jesse.game.task.WorldTasksManager.schedule
import org.jesse.game.util.Direction
import org.jesse.game.world.World.spawnObject
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.npc.NPC
import org.jesse.game.npc.ids.*
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.cutscene.FadeScreen
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.game.obj.ids.*
import org.jesse.game.world.`object`.WorldObject
import org.jesse.game.world.region.PrebuiltDynamicArea
import org.jesse.game.world.region.dynamicregion.MapBuilder
import org.jesse.plugins.dialogue.PlainChat

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-11-14
 */
data class AraxyteCaveHunt(
    val player: Player?
) : PrebuiltDynamicArea(allocatedArea = MapBuilder.findEmptyChunk(8, 8), region = 15002) {

    override fun name(): String = "araxyte_cave_hunt"

    private val objectType: Int = 10
    private val npcTile: Location = getLocation(3734, 9865, 0)

    private val northWebTunnelLocation: Location = Location(3731, 9868, 0)
    private val southWebTunnelLocation: Location = Location(3731, 9859, 0)
    private val westWebTunnelLocation: Location = Location(3727, 9863, 0)
    private val eastWebTunnelLocation: Location = Location(3736, 9863, 0)

    private var progression: Int = 0

    var roomCompleted: Boolean = true

    val entryTile: Location = getLocation(3731, 9865, 0)
    val correctTunnel: Int = WEB_TUNNEL_54155
    val wrongTunnel: Int = WEB_TUNNEL_54159

    override fun enter(player: Player?) {}

    fun progressCaveHunt() {
        progression++
        setupStage()
    }

    override fun constructed() {
        transferPlayer()
    }

    private fun setupStage() {
        if (progression < 8) {
            if (!passesPassiveCheck()) {
                player?.dialogue { plain("You get lost and find yourself back at the beginning...") }
                player?.setLocation(exitLocation)
                return
            }
        }
        npcs.forEach { it.remove() }
        when (progression) {
            0 -> spawnWithSafeToThe(North)
            1 -> spawnWithSafeToThe(North)
            2 -> spawnWithSafeToThe(South)
            3 -> spawnWithSafeToThe(South)
            4 -> spawnWithSafeToThe(West)
            5 -> spawnWithSafeToThe(East)
            6 -> spawnWithSafeToThe(West)
            7 -> spawnWithSafeToThe(East)
            8 -> {
                // Slay the Araxyte with Halbread
                spawnWithSafeToThe(WestEast)
                // spawn NPC
                val loneAraxyte = LoneAraxyte(this, npcTile)
                    loneAraxyte.radius = 0
                    loneAraxyte.spawn()
                addNpc(loneAraxyte)
            }
            9 -> {
                // Talk to Vefari with Slayer helm
                spawnWithSafeToThe(WestEast)
                val weave = NPC(WEAVE_13677, npcTile, true)
                    weave.radius = 0
                    weave.spawn()
                addNpc(weave)
            }
            10 -> {
                // Talk to Araxi with Pet
                spawnWithSafeToThe(WestEast)
                val arancini = NPC(ARANCINI, npcTile, true)
                    arancini.radius = 0
                    arancini.spawn()
                addNpc(arancini)
            }
            11 -> {
                // use the amulet on the Man
                spawnWithSafeToThe(WestEast)
                val man = NPC(MAN_13679, npcTile, true)
                    man.radius = 0
                    man.spawn()
                addNpc(man)
            }
            12 -> {
                player?.setLocation(exitLocation)
                player?.mapInstance = null
                destroyRegion()
            }
        }
    }

    private fun spawnWithSafeToThe(dir: Direction) {
        when (dir) {
            North -> {
                spawnObject(WorldObject(correctTunnel, objectType, 3, getLocation(northWebTunnelLocation)))
                spawnObject(WorldObject(wrongTunnel, objectType, 1, getLocation(southWebTunnelLocation)))
                spawnObject(WorldObject(wrongTunnel, objectType, 0, getLocation(eastWebTunnelLocation)))
                spawnObject(WorldObject(wrongTunnel, objectType, 2, getLocation(westWebTunnelLocation)))
            }
            South -> {
                spawnObject(WorldObject(wrongTunnel, objectType, 3, getLocation(northWebTunnelLocation)))
                spawnObject(WorldObject(correctTunnel, objectType, 1, getLocation(southWebTunnelLocation)))
                spawnObject(WorldObject(wrongTunnel, objectType, 0, getLocation(eastWebTunnelLocation)))
                spawnObject(WorldObject(wrongTunnel, objectType, 2, getLocation(westWebTunnelLocation)))
            }
            East -> {
                spawnObject(WorldObject(wrongTunnel, objectType, 3, getLocation(northWebTunnelLocation)))
                spawnObject(WorldObject(wrongTunnel, objectType, 1, getLocation(southWebTunnelLocation)))
                spawnObject(WorldObject(correctTunnel, objectType, 0, getLocation(eastWebTunnelLocation)))
                spawnObject(WorldObject(wrongTunnel, objectType, 2, getLocation(westWebTunnelLocation)))
            }
            West -> {
                spawnObject(WorldObject(wrongTunnel, objectType, 3, getLocation(northWebTunnelLocation)))
                spawnObject(WorldObject(wrongTunnel, objectType, 1, getLocation(southWebTunnelLocation)))
                spawnObject(WorldObject(wrongTunnel, objectType, 0, getLocation(eastWebTunnelLocation)))
                spawnObject(WorldObject(correctTunnel, objectType, 2, getLocation(westWebTunnelLocation)))
            }
            else -> {
                roomCompleted = false
                spawnObject(WorldObject(-1, objectType, 3, getLocation(northWebTunnelLocation)))
                spawnObject(WorldObject(-1, objectType, 1, getLocation(southWebTunnelLocation)))
                spawnObject(WorldObject(correctTunnel, objectType, 0, getLocation(eastWebTunnelLocation)))
                spawnObject(WorldObject(wrongTunnel, objectType, 2, getLocation(westWebTunnelLocation)))
            }
        }
    }

    private fun passesPassiveCheck(): Boolean {
        if (player == null) return false
        if (player.isDeveloper) return true
        val hasBoots = player.equipment.containsItem(ARANEA_BOOTS)
        val isEnvenomed = player.toxins.isVenomed
        return hasBoots && isEnvenomed
    }

    private fun transferPlayer() {
        if (player == null) return
        player.lock()
        val screen = FadeScreen(player) {
            player.setLocation(entryTile)
            player.faceDirection(Direction.WEST)
        }
        player.dialogueManager.start(PlainChat(player, "You crawl through the webbed tunnel.", false))
        screen.fade()
        schedule(2) {
            screen.unfade()
            if (!passesPassiveCheck()) {
                player.dialogue { plain("You get lost and find yourself back at the beginning...", false) }
                player.setLocation(exitLocation)
                player.unlock()
                return@schedule
            }
            setupStage()
            player.mapInstance = this@AraxyteCaveHunt
            player.unlock()
        }
    }

    companion object {
        val exitLocation: Location = Location(3682, 9802, 0)
    }
}
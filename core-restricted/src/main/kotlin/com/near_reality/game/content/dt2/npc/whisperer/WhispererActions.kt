package com.near_reality.game.content.dt2.npc.whisperer

import com.near_reality.game.content.dt2.npc.exactMove
import com.near_reality.game.content.dt2.npc.instanceArea
import com.near_reality.game.content.dt2.npc.opposing
import com.near_reality.game.content.dt2.npc.schedule
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.item.ItemId.BLACKSTONE_FRAGMENT_28357
import com.zenyte.game.model.item.pluginextensions.ItemPlugin
import com.zenyte.game.model.music.Music
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Direction
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.ODD_FIGURE
import com.zenyte.game.world.entity.npc.actions.NPCPlugin
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.cutscene.FadeScreen
import com.zenyte.game.world.entity.player.dialogue.options
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject

/**
 * @author Khaled Abdeljaber
 */

class LeviathanHandholdsObjectAction : ObjectAction {

    override fun handleObjectAction(player: Player, obj: WorldObject, name: String, optionId: Int, option: String) {
        when (option) {
            "Escape" -> {
                player.options("Are you sure you want to escape?") {
                    "Yes" {
                        handleQuickEscape(player, obj)
                    }
                    "No" { }
                }
            }

            "Quick-escape" -> handleQuickEscape(player, obj)
        }
    }

    private fun handleQuickEscape(player: Player, obj: WorldObject) {
        player.blockIncomingHits(3)
        fadeTeleport(player, Location(2656, 6400, 0))
    }

    private fun fadeTeleport(player: Player, location: Location) {
        val screen = FadeScreen(player) { player.setLocation(location) }
        screen.fade()
        WorldTasksManager.schedule({ screen.unfade() }, 2)
    }


    override fun getObjects(): Array<Any> {
        return arrayOf(
            ObjectId.TENTACLE
        )
    }
}


class OddFigureAction : NPCPlugin() {

    override fun handle() {
        bind("Disturb") { player: Player, npc: NPC ->
            if (!player.inventory.containsItem(BLACKSTONE_FRAGMENT_28357)) {
                player.sendMessage("You need a Blackstone Fragment to begin this Fight.")
                return@bind
            }

            player.disturbOrbAwakenedCheck(npc)
        }
    }

    fun Player.disturbOrbAwakenedCheck(whisperer: NPC) {
        if (inventory.containsItem(ItemId.AWAKENERS_ORB)) {
            options("Consume the awakener's orb to awaken The Whisperer?") {
                "Yes" {
                    inventory.deleteItem(ItemId.AWAKENERS_ORB, 1)
                    disturbOrb(whisperer, true)
                }
                "No" {
                    disturbOrb(whisperer, false)
                }
            }
        } else {
            disturbOrb(whisperer, false)
        }
    }

    fun Player.disturbOrb(whisperer: NPC, awakened: Boolean) {
        if (!inventory.containsItem(ItemId.BLACKSTONE_FRAGMENT_28357)) {
            sendMessage("It would not be wise to proceed without a way to traverse the Shadow Realm.")
            return
        }
        animation = Animation(827)
        sanity = 100
        schedule(delay = 1) {
            Music.getOrNull("Song of the Silent Choir")?.let { music.unlock(it) }

            animation = WhispererConstants.HUMAN_WHISPERER_PUSH_BACK_GET_UP
            sendSound(3201)
            val dir = Direction.getDirection(location, whisperer.location)
            exactMove(
                secondLocation = location.transform(dir.opposing, 3),
                secondDuration = 60,
                direction = dir
            )
            mapInstance?.apply {
                addTentacles(true, false)
                addTentacles(true, true)
            }
            whisperer.animation = WhispererConstants.WHISPERER_ODD_FIGURE_RISE
            schedule(delay = 4) {
                whisperer.animation = null
                whisperer.waiting = false
                whisperer.setTransformation(if (awakened) NpcId.THE_WHISPERER_12206 else NpcId.THE_WHISPERER)
                whisperer.hitpoints = whisperer.maxHitpoints
                whisperer.lowestHitpoints = whisperer.maxHitpoints
                whisperer.rotation = SpecialAttackRotations.entries.random()
                whisperer.phase = SpecialPhase.STANDARD
                whisperer.combat.combatDelay = 4
                instanceArea?.players?.forEach {
                    if (awakened) {
                        it.bossTimer.startTracking("whisperer awakened")
                    } else {
                        it.bossTimer.startTracking("whisperer")
                    }
                    it.hpHud.open(whisperer.id, whisperer.maxHitpoints)
                    it.hpHud.updateValue(whisperer.hitpoints)
                }
            }
        }
    }

    override fun getNPCs(): IntArray {
        return intArrayOf(ODD_FIGURE)
    }
}

class BlackstoneFragmentAction : ItemPlugin() {

    override fun handle() {
        bind("recall") { player: Player, item: Item, slotId: Int ->
            player.recallBlackstone()
        }

        bind("activate") { player: Player, item: Item, slotId: Int ->
            player.instanceArea?.let { player.activateBlackstone(it, force = false) }
        }
    }

    override fun getItems(): IntArray {
        return intArrayOf(ItemId.BLACKSTONE_FRAGMENT, ItemId.BLACKSTONE_FRAGMENT_28357)
    }
}
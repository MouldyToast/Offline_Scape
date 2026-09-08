package com.near_reality.game.content.dt2.npc.leviathan

import com.near_reality.game.content.dt2.npc.get
import com.near_reality.game.content.dt2.npc.instanceArea
import com.zenyte.game.content.skills.agility.Shortcut
import com.zenyte.game.item.ids.*
import com.zenyte.game.task.WorldTask
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.ForceMovement
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.cutscene.FadeScreen
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject

class LeviathanHandholdsObjectAction : ObjectAction {

    fun Player.enter() {
        val right = position == instanceArea[exitRight]
        if (inventory.containsItem(AWAKENERS_ORB)) {
            dialogue {
                options("Consume the awakener's orb to awaken Leviathan?", "Yes.", "No.")
                    .onOptionOne {
                        inventory.deleteItem(AWAKENERS_ORB, 1)
                        enterRitualSite(true, right)
                    }.onOptionTwo {
                        enterRitualSite(false, right)
                    }
            }
        } else {
            enterRitualSite(false, right)
        }
    }

    fun Player.exit() {
        animation = Animation.LADDER_DOWN
        val right = position == instanceArea[entranceRight]
        lock(1)
        WorldTasksManager.schedule({ teleport(if (right) exitRight else exitLeft) }, 0)
    }

    fun Player.enterRitualSite(awakened: Boolean, right: Boolean) {
        animation = Animation.LADDER_UP

        val instance = LeviathanInstance.construct(awakened)

        lock(1)
        WorldTasksManager.schedule({
            teleport(instance[if (right) entranceRight else entranceLeft])
            sendFilteredMessage("The brain holds fall away as you use them.")
        }, 0)
    }

    override fun handleObjectAction(
        player: Player,
        obj: WorldObject,
        name: String,
        optionId: Int,
        option: String
    ) {
        val instance = player.instanceArea
        val position = player.position

        if (obj.id == ObjectId.HANDHOLDS_47594) {
            if (instance == null && position == exitRight) {
                player.enter()
            } else {
                player.exit()
            }
        } else if (obj.id == ObjectId.HANDHOLDS_47593) {
            if (instance == null) {
                player.enter()
            } else {
                player.exit()
            }
        }
    }

    private fun fadeTeleport(player: Player, location: Location) {
        val screen = FadeScreen(player) { player.setLocation(location) }
        screen.fade()
        WorldTasksManager.schedule({ screen.unfade() }, 2)
    }

    override fun getObjects(): Array<Any> {
        return arrayOf(
            ObjectId.HANDHOLDS_47594,
            ObjectId.HANDHOLDS_47593
        )
    }

    companion object {
        private val entranceLeft = Location(2071, 6368, 0)
        private val exitLeft = Location(2069, 6368, 0)

        private val entranceRight = Location(2090, 6380, 0)
        private val exitRight = Location(2092, 6380, 0)
    }
}


class LeviathanBoatsObjectAction : ObjectAction {

    override fun handleObjectAction(
        player: Player,
        obj: WorldObject,
        name: String,
        optionId: Int,
        option: String
    ) {
        if (obj.id == ObjectId.ROWBOAT_49212) {
            fadeTeleport(player, Location(2066, 6370, 0))
        } else if (obj.id == ObjectId.ROWBOAT_49213) {
            fadeTeleport(player, Location(2064, 6436, 0))
        } else if (obj.id == ObjectId.ABYSSAL_RIFT_49204) {
            fadeTeleport(player, Location(3613, 9472, 0))
        }
    }

    private fun fadeTeleport(player: Player, location: Location) {
        val screen = FadeScreen(player) { player.setLocation(location) }
        screen.fade()
        WorldTasksManager.schedule({ screen.unfade() }, 2)
    }

    override fun getObjects(): Array<Any> {
        return arrayOf(
            ObjectId.ROWBOAT_49212,
            ObjectId.ROWBOAT_49213,
        )
    }
}

class TheScarSteppingStones : Shortcut {

    companion object {
        private val STEP_ONE = Location(2029, 6430, 0)
        private val STEP_TWO = Location(2031, 6430, 0)
        private val STEP_THREE = Location(2033, 6430, 0)
        private val STEP_FOUR = Location(2035, 6430, 0)
    }

    override fun startSuccess(player: Player, obj: WorldObject) {
        val direction = player.x < obj.x
        player.faceObject(obj)

        WorldTasksManager.schedule(object : WorldTask {
            var ticks = 0

            override fun run() {
                when (ticks) {
                    0 -> {
                        player.setAnimation(Animation.JUMP)
                        player.forceMovement = ForceMovement(
                            player.location,
                            15,
                            obj,
                            35,
                            if (direction) ForceMovement.EAST else ForceMovement.WEST
                        )
                    }

                    1 -> {
                        player.setLocation(obj.position)
                    }

                    2 -> {
                        player.setAnimation(Animation.JUMP)
                        val nextStep = getNextStep(direction, ticks)
                        player.forceMovement = ForceMovement(
                            player.location,
                            15,
                            nextStep,
                            35,
                            if (direction) ForceMovement.EAST else ForceMovement.WEST
                        )
                    }

                    3 -> {
                        val nextStep = getNextStep(direction, ticks)
                        player.setLocation(nextStep)
                    }

                    4 -> {
                        player.setAnimation(Animation.JUMP)
                        val nextStep = if (direction) STEP_FOUR else STEP_ONE
                        player.forceMovement = ForceMovement(
                            player.location,
                            15,
                            nextStep,
                            35,
                            if (direction) ForceMovement.EAST else ForceMovement.WEST
                        )
                    }

                    5 -> {
                        val nextStep = if (direction) STEP_FOUR else STEP_ONE
                        player.setLocation(nextStep)

                        stop()
                    }
                }
                ticks++
            }

            private fun getNextStep(direction: Boolean, currentTick: Int): Location {
                return if (direction) {
                    when (currentTick) {
                        2, 3 -> STEP_THREE
                        else -> STEP_FOUR
                    }
                } else {
                    when (currentTick) {
                        2, 3 -> STEP_TWO
                        else -> STEP_ONE
                    }
                }
            }
        }, 0, 0)
    }

    override fun getLevel(obj: WorldObject): Int {
        return 75
    }

    override fun getObjectIds(): IntArray {
        return intArrayOf(ObjectId.STEPPING_STONE_49209)
    }

    override fun getDuration(success: Boolean, obj: WorldObject): Int {
        return 6
    }

    override fun getSuccessXp(obj: WorldObject): Double {
        return 0.0
    }

    override fun getRouteEvent(player: Player, obj: WorldObject): Location {
        return if (player.x < obj.x) STEP_ONE else STEP_FOUR
    }
}

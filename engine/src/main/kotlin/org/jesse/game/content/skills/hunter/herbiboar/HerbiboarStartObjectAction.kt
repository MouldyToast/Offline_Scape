package org.jesse.game.content.skills.hunter.herbiboar

import org.jesse.game.content.skills.hunter.herbiboar.Herbiboar.currentHerbiboarPath
import org.jesse.game.content.skills.hunter.herbiboar.Herbiboar.unharvestedHerbiboar
import org.jesse.game.item.ids.*
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.util.Utils
import org.jesse.game.world.World
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.Skills
import org.jesse.game.world.entity.player.dialogue.Dialogue
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.world.`object`.WorldObject

/**
 * @author Andys1814
 * @since 1/26/2025
 */
@Suppress("unused")
class HerbiboarStartObjectAction : ObjectAction {

    companion object {
        val SEARCH_ANIMATION = Animation(5216)
    }

    override fun handleObjectAction(
        player: Player,
        `object`: WorldObject,
        name: String,
        optionId: Int,
        option: String?
    ) {
        when (option) {
            "Inspect" -> {
                inspect(player, `object`)
                return
            }
            "Check count" -> {
                checkCount(player)
                return
            }
            "Toggle warning" -> {
                toggleWarning(player)
                return
            }
        }
    }

    private fun inspect(player: Player, `object`: WorldObject, overrideWarning: Boolean = false) {
        if (!Herbiboar.REGIONS.contains(player.position.regionId)) {
            player.sendMessage("You must be on Fossil Island in order to hunt Herbiboar.")
            return
        }

        if (player.skills.getLevel(Skills.HUNTER) < 80) {
            player.sendMessage("You require at least level 80 hunter to track here.")
            return
        }

        val isWarningEnabled = player.attributes.getOrDefault(Herbiboar.UNHARVESTED_WARNING_ATTRIBUTE, true) as Boolean
        if (player.unharvestedHerbiboar != null && isWarningEnabled && !overrideWarning) {
            player.dialogueManager.start(object: Dialogue(player) {
                override fun buildDialogue() {
                    val options = options("Are you sure you want to track a new Herbiboar? <br> You will lose the one you already found.", "No.", "Yes.", "Yes and don't ask again.")
                    options.onOptionTwo {
                        inspect(player, `object`, overrideWarning = true)
                    }
                    options.onOptionThree {
                        toggleWarning(player, message = false)
                        inspect(player, `object`, overrideWarning = true)
                    }
                }
            })
            return
        }

        if (player.unharvestedHerbiboar != null) {
            World.getNPCs().get(player.unharvestedHerbiboar!!)?.finish()
            player.unharvestedHerbiboar = null
            player.currentHerbiboarPath = null
        }

        val start = HerbiboarStart.withObjectId(`object`.id) ?: return
        val paths = HerbiboarTrails.POSSIBLE_PATHS[start] ?: return
        val path = paths.randomOrNull() ?: return

        // !! IMPORTANT since player state is held in the paths we need a unique copy per player.
        player.currentHerbiboarPath = path.copy()

        player.animation = SEARCH_ANIMATION
        player.lock(2)

        WorldTasksManager.schedule({
            val trail = path.trails.firstOrNull() ?: return@schedule

            player.varManager.sendBit(trail.trailVarbitId, trail.trailVarbitValue)
            player.varManager.sendBit(trail.spotVarbitId, trail.spotVarbitValue)

            player.sendMessage("Closer inspection reveals tracks leading away from you.")
        }, 1)
    }

    private fun checkCount(player: Player) {
        val harvested = player.notificationSettings.getKillcount("Herbiboar")
        player.dialogueManager.start(object: Dialogue(player) {
            override fun buildDialogue() {
                item(HERBIBOAR, "You have harvested $harvested Herbiboar${Utils.plural(harvested)}.")
            }
        })
    }

    private fun toggleWarning(player: Player, message: Boolean = true) {
        val isWarningEnabled = player.attributes.getOrDefault(Herbiboar.UNHARVESTED_WARNING_ATTRIBUTE, true) as Boolean
        player.attributes[Herbiboar.UNHARVESTED_WARNING_ATTRIBUTE] = !isWarningEnabled
        if (message) {
            if (isWarningEnabled) {
                player.sendMessage("You will no longer be warned if you try to track a second herbiboar before harvesting a caught one.")
            } else {
                player.sendMessage("You will now be warned if you try to track a second herbiboar before harvesting a caught one.")
            }
        }
    }

    override fun getObjects() = HerbiboarStart.OBJECT_IDS

}
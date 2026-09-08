package org.jesse.game.content.skills.hunter.herbiboar

import org.jesse.game.content.skills.hunter.herbiboar.Herbiboar.currentHerbiboarPath
import org.jesse.game.content.drops.DropTableBuilder
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.util.Utils
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.Skills
import org.jesse.game.world.`object`.ObjectAction
import org.jesse.game.world.`object`.WorldObject

/**
 * @author Andys1814
 * @since 1/26/2025
 */
@Suppress("unused")
class HerbiboarSearchSpotObjectAction : ObjectAction {

    companion object {

        private val SEARCH_ANIMATION = Animation(5216)

        private val randomSearchTable = DropTableBuilder()
            .append(21555, 10, 50, 500)
            .append(21562, 1, 400)
            .append(21564, 1, 300)
            .append(21566, 1, 205)
            .append(21568, 1, 100)
            .build()

    }

    override fun handleObjectAction(
        player: Player,
        `object`: WorldObject,
        name: String,
        optionId: Int,
        option: String
    ) {
        val spot = HerbiboarSpot.withObjectId(`object`.id) ?: return

        player.animation = SEARCH_ANIMATION
        player.lock(2)

        WorldTasksManager.schedule({
            val path = player.currentHerbiboarPath
            if (path == null) {
                player.sendMessage("Nothing seems to be out of place here.")
                return@schedule
            }

            val currentSpot = path.getCurrentTrail()?.spot
            if (spot != currentSpot) {
                player.sendMessage("Nothing seems to be out of place here.")
                return@schedule
            }

            // The messages in OSRS depend on what you're searching...
            if (HerbiboarSpot.MUSHROOMS.contains(currentSpot)) {
                if (Utils.randomBoolean()) {
                    player.sendFilteredMessage("This mushroom cap looks freshly eaten. Something has passed this way.")
                } else {
                    player.sendFilteredMessage("This mushroom is oozing where it has recently been bitten. Something has passed this way.")
                }
            } else if (HerbiboarSpot.PATCHES.contains(currentSpot)) {
                player.sendFilteredMessage("You find a partial footprint in the disturbed earth. Something has passed this way.")
            } else if (HerbiboarSpot.SEAWEED.contains(currentSpot)) {
                player.sendFilteredMessage("Something has disturbed this seaweed recently, it smells.")
            }

            if (path.currentSpotIndex == path.trails.size - 1) {
                player.varManager.sendBit(Herbiboar.HB_FINISH, path.tunnel.ordinal + 1)
                player.varManager.sendBit(path.tunnelVarbitId, path.tunnelVarbitValue)

                player.skills.addXp(Skills.HUNTER, player.getHerbiboarHarvestExperience().toDouble())
                path.currentSpotIndex++
            } else {
                val nextTrail = path.trails[++path.currentSpotIndex]

                player.varManager.sendBit(nextTrail.trailVarbitId, nextTrail.trailVarbitValue)
                player.varManager.sendBit(nextTrail.spotVarbitId, nextTrail.spotVarbitValue)

                if (Utils.random(100) <= 5) {
                    val item = randomSearchTable.rollItem()
                    player.inventory.addItem(item)
                }

                player.skills.addXp(Skills.HUNTER, 50.0)
            }
        }, 1)

    }

    private fun Player.getHerbiboarHarvestExperience(): Int {
        val hunterLevel = skills.getLevelForXp(Skills.HUNTER)
        return if (hunterLevel <= 95) {
            1860 + (hunterLevel - 77) * 30
        } else {
            1860 + (95 - 77) * 30 + (hunterLevel - 95) * 19
        }
    }

    override fun getObjects() = HerbiboarSpot.OBJECT_IDS

}
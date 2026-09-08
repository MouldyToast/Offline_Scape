package org.jesse.game.content.dt2.npc.theduke

import org.jesse.game.content.dt2.area.DukeSucellusInstance
import org.jesse.game.content.dt2.npc.findNpc
import org.jesse.game.content.offset
import org.jesse.game.content.spotlightTag
import org.jesse.game.task.TickTask
import org.jesse.game.task.WorldTasksManager.schedule
import org.jesse.game.util.Utils
import org.jesse.game.world.World
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.SoundEffect
import org.jesse.game.world.entity.masks.Animation
import it.unimi.dsi.fastutil.objects.ObjectArrayList

data class ExtremityGroup(
    var side: ExtremitySide,
    var arena: DukeSucellusInstance,
    var members: List<Extremity>,
    var cooldown: Int = 3
) {
    private val spotlights: MutableList<SpotlightNpc> = ObjectArrayList()

    fun process() {
        if (cooldown <= 0) {
            activateTask()
            cooldown = 20
        } else cooldown--
        checkSpotlight()
    }

    private val damageOffset =
        if (side == ExtremitySide.LEFT)
            Pair(2, 0)
        else
            Pair(16, 0)

    private fun checkSpotlight() {
        members.forEach { member ->
            val spotlightId = member.spotLightNPC
            getSpotlightLocation(member).findNpc(radius = 3) { id == spotlightId }?.let {
                schedule(2) {
                    getSpotlightLocation(member).findNpc(radius = 3) { id == spotlightId }?.let {
                        val spotlightLocation = it.middleLocation offset damageOffset
                        if (arena.player.location == it.middleLocation || spotlightLocation.withinDistance(arena.player, 1))
                            if (!arena.player.isStunned)
                                it spotlightTag arena.player
                    }
                }
            }
        }
    }

    private fun activateTask() {
        arena.player.addTemporaryAttribute("spotlight_tag", false)
        val pattern = getSpotlightPattern()
        schedule(object : TickTask() {
            override fun run() {
                when (ticks++) {
                    1 -> {
                        val index = pattern?.get(0) ?: 0
                        val extremity = members[index]
                        World.sendObjectAnimation(extremity.awakened(), Animation(extremity.animationId))
                        schedule(4) {
                            World.sendObjectAnimation(
                                extremity.awakened(),
                                Animation(extremity.restAnimationId)
                            )
                        }
                        addSpotlight(index)
                    }

                    5 -> {
                        removePreviousSpotlight()
                        val index = pattern?.get(1) ?: 1
                        val extremity = members[index]
                        World.sendObjectAnimation(extremity.awakened(), Animation(extremity.animationId))
                        schedule(4) {
                            World.sendObjectAnimation(
                                extremity.awakened(),
                                Animation(extremity.restAnimationId)
                            )
                        }
                        addSpotlight(index)
                    }

                    9 -> {
                        removePreviousSpotlight()
                        val index = pattern?.get(2) ?: 2
                        val extremity = members[index]
                        World.sendObjectAnimation(extremity.awakened(), Animation(extremity.animationId))
                        schedule(4) {
                            World.sendObjectAnimation(
                                extremity.awakened(),
                                Animation(extremity.restAnimationId)
                            )
                        }
                        addSpotlight(index)
                    }

                    13 -> {
                        removePreviousSpotlight()
                        stop()
                    }
                }
            }
        }, 0, 0)
    }

    private fun getSpotlightPattern(): Array<Int>? {
        val option1 = arrayOf(0, 1, 2)
        val option2 = arrayOf(1, 2, 0)
        val option3 = arrayOf(2, 0, 1)
        val options = arrayOf(option1, option2, option3)
        return Utils.random(options)
    }

    private fun removePreviousSpotlight() {
        if (spotlights.isNotEmpty()) {
            val light = spotlights[0]
            light.remove()
            spotlights.remove(light)
        }
        arena.player.addTemporaryAttribute("spotlight_tag", false)
    }

    private fun addSpotlight(index: Int) {
        val spotlight = getSpotlightNPC(index)
        spotlight.animation = Animation(members[index].gazeAnim)
        spotlight.spawn()
        spotlights.add(spotlight)

        World.sendSoundEffect(spotlight.location, SoundEffect(7181, 9, 0))
    }

    private fun getSpotlightNPC(index: Int): SpotlightNpc =
        SpotlightNpc(members[index].spotLightNPC, getSpotlightLocation(members[index]), side)

    private fun getSpotlightLocation(extremity: Extremity): Location =
        extremity.location offset getSpotlightOffset()


    private fun getSpotlightOffset(): Pair<Int, Int> = when (side) {
        ExtremitySide.LEFT -> Pair(0, -1)
        ExtremitySide.RIGHT -> Pair(-3, -1)
    }

    fun awaken() =
        members.forEach { member -> World.spawnObject(member.awakened()) }

    fun sleep() =
        members.forEach { member -> World.spawnObject(member.sleeping()) }
}
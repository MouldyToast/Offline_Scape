package org.jesse.game.content.dt2.npc.theduke

import org.jesse.game.content.dt2.npc.playSound
import org.jesse.game.content.offset
import org.jesse.game.GameInterface
import org.jesse.game.content.skills.slayer.SlayerEquipment
import org.jesse.game.task.TickTask
import org.jesse.game.task.WorldTasksManager
import org.jesse.game.util.Colour
import org.jesse.game.util.Utils
import org.jesse.game.world.Projectile
import org.jesse.game.world.World
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.Graphics
import org.jesse.game.world.entity.masks.Hit
import org.jesse.game.world.entity.masks.HitType
import org.jesse.game.world.entity.player.Player

/**
 * Mack wrote original logic - Kry rewrote in NR terms
 * @author John J. Woloszyk / Kryeus
 * @date 8.14.2024
 */
sealed interface DukeSpecialAttack {

    operator fun invoke(duke: DukeSucellusEntity, target: Player) {
        WorldTasksManager.schedule(object : TickTask() {
            override fun run() {
                target.sendDeveloperMessage("Scheduling Special Attack")
                duke.specialAttackActive = true
                execute(duke, target)
                duke.combat.combatDelay = 11
                stop()
            }
        }, 0, 0)
    }

    fun execute(duke: DukeSucellusEntity, target: Player)

    data object DeathGaze : DukeSpecialAttack {
        override fun execute(duke: DukeSucellusEntity, target: Player) {
            WorldTasksManager.schedule(object : TickTask() {
                fun Location.safeFromSpecial(): Boolean {
                    return this.x == duke.arena.leftBound.x ||
                            this.x == duke.arena.leftBound.x - 1 ||
                            this.x == duke.arena.rightBound.x ||
                            this.x == duke.arena.rightBound.x + 1
                }

                override fun run() {
                    if (duke.slumbering || duke.hitpoints == 0) {
                        stop()
                        return
                    }
                    when (ticks) {
                        0 -> {
                            duke.setModelCustomization(49194, 49193)
                            target.sendMessage(Colour.RS_PURPLE.wrap("Duke Sucellus turns his gaze upon you..."))
                        }

                        1 -> {
                            target.playSound(7215)
                            target.interfaceHandler.sendInterface(GameInterface.DUKE_GAZE)
                            target.packetDispatcher.sendComponentAnimation(
                                GameInterface.DUKE_GAZE.id,
                                2,
                                10216
                            )
                            target.packetDispatcher.sendClientScript(
                                3128, 4342689, 303 shl 16 or 1, 0, 120, 255, 150
                            )
                            duke.animation = Animation(10180)
                        }

                        5 -> {
                            target.packetDispatcher.sendComponentAnimation(
                                GameInterface.DUKE_GAZE.id,
                                2,
                                10215
                            )
                            target.packetDispatcher.sendClientScript(
                                1896,
                                4342689, 303 shl 16 or 1, 0, 30, 255
                            )

                            val safe = target.location.safeFromSpecial()
                            if (!safe) {
                                target.graphics = Graphics(369)
                                target.freeze(3, 0)
                                target.applyHit(Hit(duke, Utils.random(88, 101), HitType.TYPELESS))
                            } else {
                                target.playSound(168)
                                target.sendMessage(Colour.RS_GREEN.wrap("You manage to avoid Duke Sucellus' gaze."))
                            }

                        }

                        7 -> {
                            duke.resetBodyCustomization()
                            target.interfaceHandler.closeInterface(GameInterface.DUKE_GAZE)
                        }

                        8 -> {
                            duke.specialAttackActive = false
                            stop()
                        }
                    }
                    ticks++
                }

                override fun stop() {
                    super.stop()
                    target.interfaceHandler.closeInterface(GameInterface.DUKE_GAZE)
                }
            }, 0, 0)
        }
    }

    data object GasFlare : DukeSpecialAttack {

        private const val GAS_PROJ_TYPE: Int = 2436

        private val proj: Projectile = Projectile(GAS_PROJ_TYPE, 32, 10, 20, 60, 2, 32, 10)

        override fun execute(duke: DukeSucellusEntity, target: Player) {
            val ventsNearby = duke.arena.ventPositions.sortedBy { it.getDistance(target.position) }.iterator()

            duke.animation = Animation(10180)
            target.sendSound(5002)

            // At this point, the vent logic should be migrated into the `GasVent` object to allow
            // easily setting the state for whether its active or not as well as on-hit effects.
            val ventTask = object : TickTask() {
                var trigger = 99
                lateinit var coord: Location
                override fun run() {
                    when (ticks) {
                        0 -> {
                            coord = ventsNearby.next()
                            trigger = World.sendProjectile(duke.middleLocation offset Pair(-1, 0), coord, proj) + 1
                        }

                        trigger -> {
                            World.sendGraphics(Graphics(2431), coord)
                        }

                        trigger + 1 -> {
                            World.sendGraphics(Graphics(2432), coord)
                        }

                        trigger + 2 -> {
                            World.sendGraphics(Graphics(2433), coord)
                        }

                        trigger + 3 -> {
                            checkTargetForGas(target, coord)
                            World.sendGraphics(Graphics(2431), coord)
                        }

                        trigger + 4 -> {
                            checkTargetForGas(target, coord)
                            World.sendGraphics(Graphics(2432), coord)
                        }

                        trigger + 5 -> {
                            checkTargetForGas(target, coord)
                            World.sendGraphics(Graphics(2433), coord)
                        }

                        trigger + 6 -> {
                            duke.specialAttackActive = false
                            stop()
                        }
                    }
                    ticks++
                }

            }
            WorldTasksManager.schedule(ventTask, 0, 0)
        }

        private fun checkTargetForGas(target: Player, location: Location) {

            if (target.location.withinDistance(location, 2)) {
                target.applyHit(Hit(Utils.random(8, 15), HitType.POISON))
            }
        }

    }

    data object GasFlareEcho : DukeSpecialAttack {

        private const val GAS_PROJ_TYPE: Int = 2436

        private val proj: Projectile = Projectile(GAS_PROJ_TYPE, 32, 10, 20, 60, 2, 32, 10)

        override fun execute(duke: DukeSucellusEntity, target: Player) {
            val secondVent = duke.arena.ventPositions.sortedBy { it.getDistance(target.position) }[1]
            duke.animation = Animation(10180)
            target.sendSound(5002)

            val ventTask = object : TickTask() {
                var trigger = 99
                lateinit var coord: Location
                override fun run() {
                    when (ticks) {
                        0 -> {
                            coord = secondVent
                            trigger = World.sendProjectile(duke.middleLocation offset Pair(-1, 0), coord, proj) + 1
                        }

                        trigger -> {
                            World.sendGraphics(Graphics(2431), coord)
                        }

                        trigger + 1 -> {
                            World.sendGraphics(Graphics(2432), coord)
                        }

                        trigger + 2 -> {
                            World.sendGraphics(Graphics(2433), coord)
                        }

                        trigger + 3 -> {
                            checkTargetForGas(target, coord)
                            World.sendGraphics(Graphics(2431), coord)
                        }

                        trigger + 4 -> {
                            checkTargetForGas(target, coord)
                            World.sendGraphics(Graphics(2432), coord)
                        }

                        trigger + 5 -> {
                            checkTargetForGas(target, coord)
                            World.sendGraphics(Graphics(2433), coord)
                        }

                        trigger + 6 -> {
                            duke.specialAttackActive = false
                            stop()
                        }
                    }
                    ticks++
                }

            }
            WorldTasksManager.schedule(ventTask, 2, 0)
        }

        private fun checkTargetForGas(target: Player, location: Location) {

            if (target.location.withinDistance(location, 2)) {
                target.applyHit(Hit(Utils.random(8, 15), HitType.POISON))
            }
        }

    }
}

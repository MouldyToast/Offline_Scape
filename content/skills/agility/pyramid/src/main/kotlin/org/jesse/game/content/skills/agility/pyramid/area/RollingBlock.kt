package org.jesse.game.content.skills.agility.pyramid.area

import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager.schedule
import org.jesse.game.util.CollisionUtil
import org.jesse.game.util.Direction
import org.jesse.game.util.Utils
import org.jesse.game.world.World
import org.jesse.game.world.entity.ImmutableLocation
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.SoundEffect
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.ForceMovement
import org.jesse.game.world.entity.masks.Hit
import org.jesse.game.world.entity.masks.HitType
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.SkillConstants
import org.jesse.game.world.`object`.WorldObject
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sqrt

object RollingBlock {
    private val diveAnim = Animation(1115)
    private val slipAnim = Animation(3064)

    fun roll(player: Player, x: Int, y: Int) {
        if (player.isLocked()) {
            return
        }
        val playerZ = player.getPlane()
        for (block in RollingBlockObject.Companion.values) {
            val tile = block.swTile
            val blockZ = tile.plane
            if (playerZ != blockZ + 1) {
                continue
            }
            val blockX = tile.x
            val blockY = tile.y
            if (!CollisionUtil.collides(x, y, 1, blockX, blockY, 2)) {
                continue
            }
            val stoneObject: WorldObject = World.getObjectWithType(tile, 10)
            player.lock()
            player.faceObject(stoneObject)
            val xDiff = player.getX() - x
            val yDiff = player.getY() - y
            val distance = sqrt((xDiff * xDiff + yDiff * yDiff).toDouble())
            schedule(WorldTask {
                player.getVarManager().sendBit(stoneObject.definitions!!.getVarbitId(), true)
                player.sendSound(SoundEffect(1396))
                val level = player.getSkills().getLevel(SkillConstants.AGILITY)
                val baseRequirement = 30
                val baseChance = 75 //Base chance % to not fail minimum level.
                val neverFailLevel = 70
                val adjustmentPercentage = 100 - baseChance
                val successPerLevel = adjustmentPercentage.toFloat() / (neverFailLevel.toFloat() - baseRequirement)
                val successChance = baseChance + max(0, (level - baseRequirement)) * successPerLevel
                val success = Utils.random(100) < successChance
                val dirDiff: Int = abs(
                    stoneObject.faceDirection.getDirection() - Direction.getNPCDirection(player.getRoundedDirection())
                        .getDirection()
                )
                val forward = dirDiff <= 257 || dirDiff >= 1791
                if (forward && success) {
                    success(player, stoneObject)
                } else {
                    failure(player, stoneObject, !forward)
                }
            }, if (distance < 2) 0 else 1)
            break
        }
    }

    private fun success(player: Player, `object`: WorldObject) {
        player.sendSound(SoundEffect(2455, 1, 25))
        player.setAnimation(diveAnim)
        val forceMovement = ForceMovement(
            player.location.transform(`object`.faceDirection, 2),
            30,
            `object`.faceDirection.getDirection()
        )
        player.setForceMovement(forceMovement)
        schedule(WorldTask {
            player.getVarManager().sendBit(`object`.definitions!!.getVarbitId(), false)
            player.setLocation(forceMovement.getToFirstTile())
            player.getSkills().addXp(SkillConstants.AGILITY, 12.0)
            player.unlock()
        }, 1)
    }

    private fun failure(player: Player, blockObject: WorldObject, reverse: Boolean) {
        player.setAnimation(slipAnim)
        val forceMovement = createSlipMovement(player.location, blockObject)
        player.setForceMovement(forceMovement)
        schedule(WorldTask {
            player.getVarManager().sendBit(blockObject.definitions!!.getVarbitId(), false)
            player.setLocation(
                AgilityPyramidArea.Companion.getLowerTile(
                    forceMovement.getToFirstTile().transform(0, 0, 1)
                )
            )
            player.applyHit(Hit(if (reverse) 1 else 6, HitType.REGULAR))
            player.unlock()
        }, 1)
    }

    private fun createSlipMovement(location: Location, `object`: WorldObject): ForceMovement {
        val dir =
            if (`object`.rotation == 1) Direction.WEST else if (`object`.rotation == 0) Direction.SOUTH else if (`object`.rotation == 2) Direction.NORTH else Direction.EAST
        val destination = location.transform(dir, 2).transform(0, 0, -1)
        return ForceMovement(destination, 60, dir.getDirection())
    }

    private enum class RollingBlockObject(val swTile: ImmutableLocation) {
        FIRST_BLOCK(ImmutableLocation(3354, 2841, 0)), SECOND_BLOCK(ImmutableLocation(3374, 2835, 0)), THIRD_BLOCK(
            ImmutableLocation(3368, 2849, 1)
        ),
        FOURTH_BLOCK(ImmutableLocation(3048, 4699, 1)), FIFTH_BLOCK(ImmutableLocation(3044, 4699, 2));

        companion object {
            val values = entries.toTypedArray()
        }
    }
}

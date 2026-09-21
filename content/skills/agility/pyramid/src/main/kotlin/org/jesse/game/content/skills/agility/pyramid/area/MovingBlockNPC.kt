package org.jesse.game.content.skills.agility.pyramid.area

import org.jesse.game.task.WorldTask
import org.jesse.game.task.WorldTasksManager.schedule
import org.jesse.game.util.CollisionUtil
import org.jesse.game.util.Direction
import org.jesse.game.world.entity.Location
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.ForceMovement
import org.jesse.game.world.entity.masks.Hit
import org.jesse.game.world.entity.masks.HitType
import org.jesse.game.world.entity.npc.NPC
import org.jesse.game.world.entity.pathfinding.Flags
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.region.GlobalAreaManager.getArea


class MovingBlockNPC(id: Int, tile: Location?, facing: Direction?, radius: Int) : NPC(id, tile, facing, radius) {
    public override fun onMovement() {
        val area = getArea<AgilityPyramidArea>(AgilityPyramidArea::class.java)
        val players = area.getPlayers()
        if (players.isEmpty()) {
            return
        }
        for (player in players) {
            if (player.isLocked() || player.getPlane() != getPlane() || !CollisionUtil.collides(
                    player.getX(),
                    player.getY(),
                    player.getSize(),
                    getX(),
                    getY(),
                    getSize()
                )
            ) {
                continue
            }
            val horizontal = spawnDirection == Direction.EAST || spawnDirection == Direction.WEST
            val destTile = Location(
                if (horizontal) (respawnTile.x + 4) else player.getX(),
                if (horizontal) player.getY() else (respawnTile.y + 4),
                player.getPlane() - 1
            )
            push(player, destTile)
        }
    }

    override fun clipFlag(): Int {
        return Flags.OCCUPIED_BLOCK_NPC or Flags.OCCUPIED_BLOCK_PLAYER
    }

    fun slide(direction: Direction) {
        val destination: Location = location.transform(direction, 2)
        addWalkSteps(destination.x, destination.y, -1, false)
    }

    private fun push(player: Player, destination: Location) {
        player.lock()
        player.stopAll()
        val ticks = player.location.getDistance(destination).toInt()
        player.setAnimation(if (ticks == 1) shortFallAnim else longFallAnim)
        val backwardsDirection = spawnDirection.getCounterClockwiseDirection(4)
        player.faceDirection(backwardsDirection)
        player.setForceMovement(ForceMovement(destination, ticks * 30, backwardsDirection.getDirection()))
        schedule(WorldTask {
            player.setLocation(destination)
            player.applyHit(Hit(6, HitType.REGULAR))
            player.unlock()
        }, ticks)
    }

    companion object {
        private val longFallAnim = Animation(3066)
        private val shortFallAnim = Animation(3065)
    }
}

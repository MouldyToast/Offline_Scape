package org.jesse.game.content.skills.agility.pyramid.area;

import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.CollisionUtil;
import org.jesse.game.util.Direction;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.ForceMovement;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.pathfinding.Flags;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.region.GlobalAreaManager;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * @author Kris | 19/02/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MovingBlockNPC extends NPC {
    private static final Animation longFallAnim = new Animation(3066);
    private static final Animation shortFallAnim = new Animation(3065);

    public MovingBlockNPC(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public void onMovement() {
        final AgilityPyramidArea area = GlobalAreaManager.getArea(AgilityPyramidArea.class);
        final Set<Player> players = area.getPlayers();
        if (players.isEmpty()) {
            return;
        }
        for (final Player player : players) {
            if (player.isLocked() || player.getPlane() != getPlane() || !CollisionUtil.collides(player.getX(), player.getY(), player.getSize(), getX(), getY(), getSize())) {
                continue;
            }
            final boolean horizontal = spawnDirection == Direction.EAST || spawnDirection == Direction.WEST;
            final Location destTile = new Location(horizontal ? (respawnTile.getX() + 4) : player.getX(), horizontal ? player.getY() : (respawnTile.getY() + 4), player.getPlane() - 1);
            push(player, destTile);
        }
    }

    @Override
    protected int clipFlag() {
        return Flags.OCCUPIED_BLOCK_NPC | Flags.OCCUPIED_BLOCK_PLAYER;
    }

    void slide(@NotNull final Direction direction) {
        final Location destination = getLocation().transform(direction, 2);
        addWalkSteps(destination.getX(), destination.getY(), -1, false);
    }

    private void push(final Player player, final Location destination) {
        player.lock();
        player.stopAll();
        final int ticks = (int) player.getLocation().getDistance(destination);
        player.setAnimation(ticks == 1 ? shortFallAnim : longFallAnim);
        final Direction backwardsDirection = spawnDirection.getCounterClockwiseDirection(4);
        player.faceDirection(backwardsDirection);
        player.setForceMovement(new ForceMovement(destination, ticks * 30, backwardsDirection.getDirection()));
        WorldTasksManager.schedule(() -> {
            player.setLocation(destination);
            player.applyHit(new Hit(6, HitType.REGULAR));
            player.unlock();
        }, ticks);
    }
}

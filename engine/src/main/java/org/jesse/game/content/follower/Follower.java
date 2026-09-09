package org.jesse.game.content.follower;

import org.jesse.game.content.follower.impl.BossPet;
import org.jesse.game.util.CollisionUtil;
import org.jesse.game.util.Utils;
import org.jesse.game.world.Position;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.pathfinding.events.npc.NPCEntityEvent;
import org.jesse.game.world.entity.pathfinding.strategy.EntityStrategy;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Kris | 2. nov 2017 : 21:19.28
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public class Follower extends NPC {
	private final transient Player owner;
	private final transient int[][] checkNearDirs;

	public Follower(final int petId, final Player owner) {
		super(petId, new Location(owner.getLocation()), false);
		this.owner = owner;
		checkNearDirs = Utils.getCoordOffsetsNear(getSize());
		if (petId == BossPet.NEXLING.getPetId()) {
			setRun(true);
		}
	}

	public Follower(final int petId, final Player owner, final Location tile) {
		super(petId, tile, false);
		this.owner = owner;
		checkNearDirs = Utils.getCoordOffsetsNear(getSize());
		if (petId == BossPet.NEXLING.getPetId()) {
			setRun(true);
		}
	}

	@Override
	public boolean isEntityClipped() {
		return false;
	}

	@Override
	public void processNPC() {
		if (isLocked()) {
			return;
		}
		if (!getLocation().withinDistance(owner.getLocation(), 12) && !owner.isTeleported()) {
			call();
			return;
		}
		if (getFaceEntity() != owner.getClientIndex()) {
			setFaceEntity(owner);
		}
		if (colliding()) {
			//TODO: Change into a more efficent pathfinding formula or write a non-pf structure.
			setRouteEvent(new NPCEntityEvent(this, new EntityStrategy(owner)));
			return;
		}
		appendMovement();
	}

	private boolean colliding() {
		return !owner.hasWalkSteps() && CollisionUtil.collides(getX(), getY(), getSize(), owner.getX(), owner.getY(), owner.getSize());
	}

	private boolean appendMovement() {
		final boolean melee = getCombatDefinitions().isMelee();
		final int maxDistance = isForceFollowClose() || melee ? 0 : getAttackDistance();
		if (isProjectileClipped(owner, true) || outOfRange(owner, maxDistance, owner.getSize(), melee)) {
			resetWalkSteps();
			calcFollow(owner, isRun() ? 2 : 1, true, isIntelligent(), isEntityClipped());
		}
		return true;
	}

	@Override
	public boolean isIntelligent() {
		return false;
	}

	boolean outOfRange(final Position targetPosition, final int maximumDistance, final int targetSize, final boolean checkDiagonal) {
		final Location target = targetPosition.getPosition();
		final int distanceX = getX() - target.getX();
		final int distanceY = getY() - target.getY();
		final int npcSize = getSize();
		if (checkDiagonal) {
			if (distanceX == -npcSize && distanceY == -npcSize || distanceX == targetSize && distanceY == targetSize || distanceX == -npcSize && distanceY == targetSize || distanceX == targetSize && distanceY == -npcSize) {
				return true;
			}
		}
		return distanceX > targetSize + maximumDistance || distanceY > targetSize + maximumDistance || distanceX < -npcSize - maximumDistance || distanceY < -npcSize - maximumDistance;
	}

	public void call() {
		final int size = getSize();
		Location teleTile = null;
		for (int dir = 0; dir < checkNearDirs[0].length; dir++) {
			final Location tile = new Location(new Location(owner.getX() + checkNearDirs[0][dir], owner.getY() + checkNearDirs[1][dir], owner.getPlane()));
			if (World.isTileFree(tile, size)) {
				teleTile = tile;
				break;
			}
		}
		if (teleTile == null) {
			return;
		}
		setLocation(teleTile);
	}

	public Pet getPet() {
		return PetWrapper.getByPet(id);
	}

	public Player getOwner() {
		return owner;
	}
}
